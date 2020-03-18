package cn.ljtnono.re.ftp;

import cn.ljtnono.re.enumeration.CommonFileTypeEnum;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReFtpClientTest {

    @Test
    void uploadFile() {
        ReFtpClient reFtpClient = new ReFtpClient();
        reFtpClient.init();
        try {
            reFtpClient.connect();
//            Map<String, Boolean> stringBooleanMap = reFtpClient.downloadFile("images", "C:\\Users\\ljt\\Desktop", "var_copy.png");
//            stringBooleanMap.forEach((k, v) -> {
//                System.out.println(k + ":" + v);
//            });
//            boolean b = reFtpClient.uploadFile("/files", "myblog.sql", new FileInputStream("C:\\Users\\ljt\\Desktop\\myblog.sql"));
//            System.out.println(b);
            String originName = "C:\\Users\\ljt\\Desktop\\re.sql";
            String extName = originName.substring(originName.lastIndexOf(".") + 1);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(originName));
            String filePath = "";
            for (CommonFileTypeEnum commonFileTypeEnum : CommonFileTypeEnum.values()) {
                if (extName.equalsIgnoreCase(commonFileTypeEnum.getValue())) {
                    int type = commonFileTypeEnum.getType();
                    switch (type) {
                        case 1 :
                            filePath = "images";
                            break;
                        case 2 :
                            filePath = "music";
                            break;
                        case 3 :
                            filePath = "documentation";
                            break;
                        case 4 :
                            filePath = "video";
                            break;
                        case 5 :
                            filePath = "pdf";
                            break;
                        case 6 :
                            filePath = "markdown";
                            break;
                        case 7 :
                            filePath = "code";
                            break;
                        default:
                            filePath = "other";
                    }
                }
            }
            reFtpClient.uploadFile(filePath, "re.sql", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}