package cn.ljtnono.re.common.util;

import java.io.*;

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

}
