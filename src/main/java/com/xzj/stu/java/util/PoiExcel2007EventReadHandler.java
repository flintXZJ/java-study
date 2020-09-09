package com.xzj.stu.java.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * poi eventusermodel 模块
 * 适用于处理大量数据的excel文件
 *
 */
public class PoiExcel2007EventReadHandler {
    public static Logger logger = LoggerFactory.getLogger(PoiExcel2007EventReadHandler.class);

    /**
     * 处理文件地址
     */
    private String filePath = "";

    /**
     * 表格默认处理器
     */
    private ISheetContentHandler contentHandler = new DefaultSheetHandler();
    /**
     * 读取数据
     */
    private List<String[]> datas = new ArrayList<String[]>();

    public PoiExcel2007EventReadHandler(String filePath) {
        this.filePath = filePath;
    }

    public List<String[]> readExcel(int sheetIndex) throws FileNotFoundException, IOException {
        List<String[]> lists = new ArrayList<>();
        try {
            lists = parse((sheetIndex + 1)).datas;
        } catch (Exception e) {
            logger.error("解析xlsx异常， e={}", e);
        }
        return lists;
    }

    /**
     * @param sheetId:为要遍历的sheet索引，从1开始
     * @return
     * @throws InvalidFormatException
     * @throws IOException
     * @throws ParseException
     */
    private synchronized PoiExcel2007EventReadHandler parse(int sheetId)
            throws InvalidFormatException, IOException, ParseException {
        // 每次转换前都清空数据
        datas.clear();
        // 打开表格文件输入流
        OPCPackage pkg = OPCPackage.open(this.filePath, PackageAccess.READ);
        try {
            // 创建表阅读器
            XSSFReader reader;
            try {
                reader = new XSSFReader(pkg);
            } catch (OpenXML4JException e) {
                logger.error("读取表格出错");
                throw new ParseException(e.fillInStackTrace());
            }

            // 转换指定单元表
            InputStream shellStream = reader.getSheet("rId" + sheetId);
            try {
                InputSource sheetSource = new InputSource(shellStream);
                StylesTable styles = reader.getStylesTable();
                ReadOnlySharedStringsTable sst = new ReadOnlySharedStringsTable(pkg);
                // 设置读取出的数据
                getContentHandler().init(datas);
                // 获取转换器
                XMLReader parser = getSheetParser(styles, sst);
                parser.parse(sheetSource);
            } catch (SAXException e) {
                logger.error("读取表格出错");
                throw new ParseException(e.fillInStackTrace());
            } finally {
                shellStream.close();
            }
        } finally {
            pkg.close();

        }
        return this;
    }

    /**
     * 获取读取表格的转换器
     *
     * @return 读取表格的转换器
     * @throws SAXException SAX错误
     */
    protected XMLReader getSheetParser(StylesTable styles, ReadOnlySharedStringsTable strings) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, getContentHandler(),new CustomDataFormatter(), false));
        return parser;
    }

    public class CustomDataFormatter extends DataFormatter {
        @Override
        public String formatRawCellContents(double value, int formatIndex, String formatString, boolean use1904Windowing) {
            // Is it a date?
            if (org.apache.poi.ss.usermodel.DateUtil.isADateFormat(formatIndex, formatString)) {
                if (org.apache.poi.ss.usermodel.DateUtil.isValidExcelDate(value)) {
                    Date d = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value, use1904Windowing);
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd").format(d);
                    } catch (Exception e) {
                        logger.error("Bad date value in Excel: " + d, e);
                    }
                }
            }
            return super.formatRawCellContents(value, formatIndex, formatString, use1904Windowing);
        }
    }

    public ISheetContentHandler getContentHandler() {
        return contentHandler;
    }

    /**
     * 表格转换错误
     */
    public class ParseException extends Exception {
        private static final long serialVersionUID = -2451526411018517607L;

        public ParseException(Throwable t) {
            super("表格转换错误", t);
        }

    }

    public interface ISheetContentHandler extends XSSFSheetXMLHandler.SheetContentsHandler {

        /**
         * 设置转换后的数据集，用于存放转换结果
         *
         * @param datas 转换结果
         */
        void init(List<String[]> datas);
    }

    /**
     * 默认表格解析handder
     */
    public class DefaultSheetHandler implements ISheetContentHandler {
        /**
         * 读取数据
         */
        private List<String[]> datas;
        private int columsLength;
        // 读取行信息
        private String[] readRow;
        private ArrayList<String> fristRow = new ArrayList<String>();

        @Override
        public void init(List<String[]> datas) {
            this.datas = datas;
        }

        @Override
        public void startRow(int rowNum) {
            if (rowNum != 0) {
                readRow = new String[columsLength];
            }
        }

        @Override
        public void endRow(int rowNum) {
            //将Excel第一行表头的列数当做数组的长度，要保证后续的行的列数不能超过这个长度，这是个约定。
            if (rowNum == 0) {
                columsLength = fristRow.size();
                readRow = fristRow.toArray(new String[columsLength]);
            } else {
                readRow = fristRow.toArray(new String[columsLength]);
            }

            datas.add(readRow.clone());
            readRow = null;
            fristRow.clear();
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            //转换A1,B1,C1等表格位置为真实索引位置
            int index = getCellIndex(cellReference);
            try {
                fristRow.set(index, formattedValue);
            } catch (IndexOutOfBoundsException e) {
                int size = fristRow.size();
                for (int i = index - size + 1; i > 0; i--) {
                    fristRow.add(null);
                }
                fristRow.set(index, formattedValue);
            }
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {
        }

        /**
         * 转换表格引用为列编号
         *
         * @param cellReference 列引用
         * @return 表格列位置，从0开始算
         */
        public int getCellIndex(String cellReference) {
            String ref = cellReference.replaceAll("\\d+", "");
            int num = 0;
            int result = 0;
            for (int i = 0; i < ref.length(); i++) {
                char ch = cellReference.charAt(ref.length() - i - 1);
                num = (int) (ch - 'A' + 1);
                num *= Math.pow(26, i);
                result += num;
            }
            return result - 1;
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        PoiExcel2007EventReadHandler poiExcel2007EventReadHandler = new PoiExcel2007EventReadHandler("C:\\Users\\Administrator.20180329-135459\\Desktop\\台账20200512.xlsx");
        List<String[]> lists = poiExcel2007EventReadHandler.readExcel(0);

        System.out.println("数据行数："+lists.size());
        System.out.println("耗时："+(System.currentTimeMillis()-start)+"ms");
    }
}
