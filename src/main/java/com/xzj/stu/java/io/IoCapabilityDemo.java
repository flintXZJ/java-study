package com.xzj.stu.java.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 各种IO操作性能测试
 *
 * @author zhijunxie
 * @date 2019/8/21 11:33
 */
public class IoCapabilityDemo {
    private static final Logger logger = LoggerFactory.getLogger(IoCapabilityDemo.class);

    private static int FILE_SIZE = 1024;

    /**
     * 无缓存压缩
     *
     * @param sourcePath
     * @param targetPath
     */
    public static void zipNoBuffer(String sourcePath, String targetPath, String path) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(targetPath)))){
            zipOperationNoBuffer(zipOutputStream, new File(sourcePath), path);
        } catch (Exception e) {
            logger.error("zipNoBuffer exception", e);
        }
    }

    private static void zipOperationNoBuffer(ZipOutputStream zipOutputStream, File file, String path) {
        // 如果是目录，则递归进行处理
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                zipOperationNoBuffer(zipOutputStream, tempFile, path + "\\"+tempFile.getName());
            }
        } else {
            // 如果是单个文件，再进行压缩
            try (FileInputStream inputStream = new FileInputStream(file)) {
                zipOutputStream.putNextEntry(new ZipEntry(path));

                int len;
                byte[] buffer = new byte[FILE_SIZE];
                while ((len = inputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }

                zipOutputStream.closeEntry();
            } catch (Exception e) {
                logger.error("zipOperationNoBuffer exception", e);
            }
        }
    }

    /**
     * 使用缓存压缩
     *
     * @param sourcePath
     * @param targetPath
     */
    public static void zipWithBuffer(String sourcePath, String targetPath, String path) {
        //获取该目录下所有文件以及文件夹
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(new File(targetPath))))) {
            zipOperationWithBuffer(zipOutputStream, new File(sourcePath), path);
        } catch (Exception e) {
            logger.error("zipWithBuffer exception", e);
        }
    }

    /**
     * 压缩具体操作
     *
     * @param zipOutputStream
     * @param file
     * @param path
     */
    private static void zipOperationWithBuffer(ZipOutputStream zipOutputStream, File file, String path) {
        // 如果是目录，则递归进行处理
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                zipOperationWithBuffer(zipOutputStream, tempFile, path + "\\"+tempFile.getName());
            }
        } else {
            // 如果是单个文件，再进行压缩
            try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                zipOutputStream.putNextEntry(new ZipEntry(path));

                int len;
                byte[] buffer = new byte[FILE_SIZE];
                while ((len = inputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }

                zipOutputStream.closeEntry();
            } catch (Exception e) {
                logger.error("zipOperationWithBuffer exception", e);
            }
        }
    }

    /**
     * 使用NIO channel通道压缩
     *
     * @param sourcePath
     * @param targetPath
     */
    public static void zipChannel(String sourcePath, String targetPath, String path) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(targetPath)))){
            WritableByteChannel writableByteChannel = Channels.newChannel(zipOutputStream);
            zipOperationChannel(zipOutputStream, writableByteChannel, new File(sourcePath), path);
        } catch (Exception e) {
            logger.error("zipChannel exception", e);
        }
    }

    private static void zipOperationChannel(ZipOutputStream zipOutputStream, WritableByteChannel writableByteChannel, File file, String path) {
        // 如果是目录，则递归进行处理
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                zipOperationChannel(zipOutputStream, writableByteChannel, tempFile, path + "\\"+tempFile.getName());
            }
        } else {
            // 如果是单个文件，再进行压缩
            try (FileChannel channel = new FileInputStream(file).getChannel()) {
                ZipEntry entry = new ZipEntry(path);
                zipOutputStream.putNextEntry(entry);
                channel.transferTo(0, file.length(), writableByteChannel);

                zipOutputStream.closeEntry();
            } catch (Exception e) {
                logger.error("zipOperationChannel exception", e);
            }
        }
    }

    /**
     * 使用内存映射文件压缩
     *
     * @param sourcePath
     * @param targetPath
     */
    public static void zipWithMap(String sourcePath, String targetPath, String path) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(targetPath)))){
            WritableByteChannel writableByteChannel = Channels.newChannel(zipOutputStream);
            zipOperationMap(zipOutputStream, writableByteChannel, new File(sourcePath), path);
        } catch (Exception e) {
            logger.error("zipWithMap exception", e);
        }
    }

    private static void zipOperationMap(ZipOutputStream zipOutputStream, WritableByteChannel writableByteChannel, File file, String path) {
        // 如果是目录，则递归进行处理
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                zipOperationMap(zipOutputStream, writableByteChannel, tempFile, path + "\\"+tempFile.getName());
            }
        } else {
            // 如果是单个文件，再进行压缩
            try {
                zipOutputStream.putNextEntry(new ZipEntry(path));

                MappedByteBuffer mappedByteBuffer = new RandomAccessFile(file, "r").getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, file.length());

                writableByteChannel.write(mappedByteBuffer);

                zipOutputStream.closeEntry();
            } catch (Exception e) {
                logger.error("zipOperationMap exception", e);
            }
        }
    }

    /**
     * 使用Pipe通道压缩
     *
     * @param sourcePath
     * @param targetPath
     */
    public static void zipWithPipe(String sourcePath, String targetPath, String path) {
        try (WritableByteChannel targetChannel = Channels.newChannel(new FileOutputStream(new File(targetPath)))){
            Pipe pipe = Pipe.open();

            CompletableFuture.runAsync(() -> runTask(pipe, new File(sourcePath), path));

            //获取读通道
            ReadableByteChannel readableByteChannel = pipe.source();
            ByteBuffer buffer = ByteBuffer.allocate(FILE_SIZE*10);
            while (readableByteChannel.read(buffer) >= 0) {
                buffer.flip();
                targetChannel.write(buffer);
                buffer.clear();
            }
            readableByteChannel.close();
        }catch (Exception e) {
            logger.error("zipWithPipe exception", e);
        }
    }

    private static void runTask(Pipe pipe, File file, String path) {
        //写入管道
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
            WritableByteChannel zipOutputChannel = Channels.newChannel(zipOutputStream);
            zipOperationPipe(zipOutputStream, zipOutputChannel, file, path);

            zipOutputChannel.close();
            zipOutputStream.close();
        } catch (Exception e) {
            logger.error("zipWithPipe runTask", e);
        }
    }

    private static void zipOperationPipe(ZipOutputStream zipOutputStream, WritableByteChannel zipOutputChannel, File file, String path) {
        // 如果是目录，则递归进行处理
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tempFile : files) {
                zipOperationPipe(zipOutputStream, zipOutputChannel, tempFile, path + "\\"+tempFile.getName());
            }
        } else {
            try {
                zipOutputStream.putNextEntry(new ZipEntry(path));

                FileChannel channel = new FileInputStream(file).getChannel();
                channel.transferTo(0, file.length(), zipOutputChannel);
                channel.close();

                zipOutputStream.closeEntry();
            } catch (Exception e) {
                logger.error("zipOperationPipe exception", e);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        logger.info("开始");
        long start = System.currentTimeMillis();
        IoCapabilityDemo.zipNoBuffer("D:\\ziptest","D:\\zipNoBuffer.zip", "ziptest");
        logger.info("zipNoBuffer 用时{}ms", (System.currentTimeMillis()-start));

        start = System.currentTimeMillis();
        IoCapabilityDemo.zipWithBuffer("D:\\ziptest","D:\\zipWithBuffer.zip", "ziptest");
        logger.info("zipWithBuffer 用时{}ms", (System.currentTimeMillis()-start));

        start = System.currentTimeMillis();
        IoCapabilityDemo.zipChannel("D:\\ziptest","D:\\zipChannel.zip", "ziptest");
        logger.info("zipChannel 用时{}ms", (System.currentTimeMillis()-start));

        start = System.currentTimeMillis();
        IoCapabilityDemo.zipWithMap("D:\\ziptest","D:\\zipWithMap.zip", "ziptest");
        logger.info("zipWithMap 用时{}ms", (System.currentTimeMillis()-start));

        start = System.currentTimeMillis();
        IoCapabilityDemo.zipWithPipe("D:\\ziptest","D:\\zipWithPipe.zip", "ziptest");
        logger.info("zipWithPipe 用时{}ms", (System.currentTimeMillis()-start));

        Thread.sleep(10000L);
    }
}
