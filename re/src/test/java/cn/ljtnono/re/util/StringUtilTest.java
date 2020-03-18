package cn.ljtnono.re.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void getUUIDWithoutJoiner() {
        for (int i = 0; i < 10; i++) {
            String uuidWithoutJoiner = StringUtil.getUUIDWithoutJoiner();
            System.out.println(uuidWithoutJoiner);
        }
    }
}