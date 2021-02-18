package cn.ljtnono.re.common.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 *
 * @author Ling, Jiatong
 * Date: 2021/1/25 1:06
 */
public class FileUtil {

    private FileUtil() {}

    public static FileUtil getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final FileUtil INSTANCE = new FileUtil();
    }

    /**
     * 上传文件
     *
     * @param bytes 文件字节流
     * @param savePath 文件存储路径，全路径（带文件名）
     * @throws IOException 当出现IO异常时抛出
     * @author Ling, Jiatong
     */
    public String uploadFile(byte[] bytes, String savePath) throws IOException {
        File file = new File(savePath);
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(savePath));
        outputStream.write(bytes);
        outputStream.close();
        return savePath;
    }

    /**
     * 上传文件
     *
     * @param inputStream 文件输入流
     * @param savePath 文件存储路径，全路径（带文件名）
     * @throws IOException 当出现IO异常时抛出
     * @author Ling, Jiatong
     */
    public String uploadFile(InputStream inputStream, String savePath) throws IOException{
        return uploadFile(new byte[inputStream.available()], savePath);
    }

    /**
     * 校验文件是否正确
     *
     * @param fileName 文件完整路径（含文件名）
     * @return 非法返回false，合法返回true
     * @author Ling, Jiatong
     */
    public boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255) {
            return false;
        } else {
            return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
        }
    }

    /**
     * 压缩多个文件成一个zip文件
     *
     * @param fileList 源文件列表
     * @param zipFile 压缩后的zip文件
     * @author Ling, Jiatong
     */
    public void zipFiles(List<File> fileList, File zipFile) {
        byte[] buf = new byte[1024];
        try {
            // 如果不存在，则创建新文件
            if (!zipFile.exists()) {
                zipFile.getParentFile().mkdirs();
                zipFile.createNewFile();
            }
            ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File file : fileList) {
                FileInputStream in = new FileInputStream(file);
                outputStream.putNextEntry(new ZipEntry(file.getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.closeEntry();
                in.close();
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压缩多个文件
     *
     * @param zipFile 需要解压缩的文件
     * @param descDir 解压后的目标目录
     */
    public static void unZipFiles(File zipFile, String descDir) {
        try {
            ZipFile zf = new ZipFile(zipFile);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param outputStream 文件输出流
     * @param file 下载的文件
     * @author Ling, Jiatong
     */
    public void downLoadFile(OutputStream outputStream, File file) {
        byte[] buf = new byte[1024];
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            int readLength = 0;
            while ((readLength = bufferedInputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, buf.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将文件压缩为.tar格式
     *
     * @param tarFile tar文件
     * @param fileList 要压缩的文件列表
     * @author Ling, Jiatong
     */
    public File tarFiles(List<File> fileList, File tarFile) {
        TarArchiveOutputStream tar = null;
        try {
            tar = new TarArchiveOutputStream(new FileOutputStream(tarFile));
            for (File file : fileList) {
                TarArchiveEntry archiveEntry = new TarArchiveEntry(file);
                tar.putArchiveEntry(archiveEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (tar != null) {
                    tar.closeArchiveEntry();
                    tar.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tarFile;
    }


    /**
     * 将文件列表压缩成gz格式
     *
     *
     * @author Ling, Jiatong
     *
     */
    public File gzFiles(List<File> fileList, File gzFile) {

        return new File("");
    }

}
