package com.xzj.stu.java.util;

import com.emc.ecs.nfsclient.nfs.NfsSetAttributes;
import com.emc.ecs.nfsclient.nfs.io.Nfs3File;
import com.emc.ecs.nfsclient.nfs.io.NfsFileInputStream;
import com.emc.ecs.nfsclient.nfs.io.NfsFileOutputStream;
import com.emc.ecs.nfsclient.nfs.nfs3.Nfs3;
import com.emc.ecs.nfsclient.rpc.CredentialUnix;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * nfs文件上传下载
 * @author zhijunxie
 * @date 2020/5/27 15:08
 */
public class NfsTransferFile {
    public static Logger logger = LoggerFactory.getLogger(PoiExcel2007EventReadHandler.class);
    private static final String NFS_IP = "10.143.143.76";
    private static final String NFS_DIR = "test";
    private static final String DIAGONAL = "/";
    private static final String NFS_DEFAULT_DIR = "/app/uploadfiles";

    //上传本地文件到Nfs服务器指定目录

    /**
     * 上传本地文件到Nfs服务器指定目录
     *
     * @param nfsPath NFS存储的相对地址 "/app/uploadfiles"
     * @param fileName 本地文件地址 "/app/uploadfiles/test.txt"
     */
    public static void uploadFileToNfs(String nfsPath, String fileName) {
        File localFile = new File(fileName);
        if (StringUtils.isBlank(nfsPath)) {
            nfsPath = NFS_DEFAULT_DIR;
        }
        uploadFileToNfs(nfsPath, localFile);
    }

    /**
     * 上传文件到Nfs服务器指定目录
     *
     * @param nfsPath NFS存储的相对地址 "/app/uploadfiles"
     * @param file 文件 "/app/uploadfiles/test.txt"
     */
    public static void uploadFileToNfs(String nfsPath, File file) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            //获取文件的文件名，此名字用于在远程的Nfs服务器上指定目录创建同名文件(不带目录)
            String fileName = file.getName();
            Nfs3 nfs3 = new Nfs3(NFS_IP, NFS_DIR, new CredentialUnix(0, 0, null), 3);
            //首先判断目录是否存在，如果不存在则进行创建目录
            nfsMkdir(nfs3, nfsPath);

            //创建远程服务器上Nfs文件对象
            Nfs3File nfs3File = new Nfs3File(nfs3, nfsPath + DIAGONAL + fileName);
            //打开一个文件输入流
            inputStream = new BufferedInputStream(new FileInputStream(file));
            //打开一个远程Nfs文件输出流，将文件复制到的目的地
            outputStream = new BufferedOutputStream(new NfsFileOutputStream(nfs3File));

            //缓冲内存
            byte[] buffer = new byte[1024];
            while ((inputStream.read(buffer)) != -1) {
                outputStream.write(buffer);
            }
            logger.info("文件上传完成！");
        } catch (Exception ex) {
            logger.error("上传文件到Nfs服务器异常", ex);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                logger.error("关闭文件流异常", ex);
            }
        }
    }

    /**
     * 从Nfs服务器上下载指定的文件到本地目录
     * @param filePath nfs文件地址 "/app/uploadfiles/test.txt"
     * @param localDir 下载到本地目录 "/app/uploadfiles"
     */
    public static void downLoadFileFromNfs(String filePath, String localDir) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            Nfs3 nfs3 = new Nfs3(NFS_IP, NFS_DIR, new CredentialUnix(0, 0, null), 3);
            //创建远程服务器上Nfs文件对象
            Nfs3File nfsFile = new Nfs3File(nfs3, filePath);
            String localFileName = localDir + DIAGONAL + nfsFile.getName();
            //创建一个本地文件对象
            File localFile = new File(localFileName);
            //打开一个文件输入流
            inputStream = new BufferedInputStream(new NfsFileInputStream(nfsFile));
            //打开一个远程Nfs文件输出流，将文件复制到的目的地
            outputStream = new BufferedOutputStream(new FileOutputStream(localFile));

            //缓冲内存
            byte[] buffer = new byte[1024];

            while (inputStream.read(buffer) != -1) {
                outputStream.write(buffer);
            }
            logger.info("文件下载完成！");
        } catch (IOException ex) {
            logger.error("文件下载异常",ex);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception ex) {

                logger.error("文件下载关闭文件流异常",ex);
            }
        }
    }

    /**
     * 判断目录是否存在，如果不存在则进行创建目录
     *
     * @param nfs3
     * @param nsfPath
     * @throws IOException
     */
    private static void nfsMkdir(Nfs3 nfs3, String nsfPath) throws IOException {
        NfsSetAttributes nfsSetAttr = new NfsSetAttributes();
        nfsSetAttr.setMode((long) (0x00100 + 0x00080 + 0x00040 + 0x00020 + 0x00010 + 0x00008 + 0x00004 + 0x00002));

        String[] paths = null;
        if (nsfPath.startsWith(DIAGONAL)) {
            //去掉第一个/之后进行分割处理
            paths = nsfPath.substring(1).split(DIAGONAL);
        } else {
            paths = nsfPath.split(DIAGONAL);
        }

        StringBuilder p = new StringBuilder();
        //首先判断目录是否存在，如果不存在则进行创建目录
        for(String s:paths){
            p.append(DIAGONAL).append(s);
            Nfs3File filePath = new Nfs3File(nfs3, p.toString());
            if (!filePath.exists()) {
                filePath.mkdir(nfsSetAttr);
            }
        }
    }
}
