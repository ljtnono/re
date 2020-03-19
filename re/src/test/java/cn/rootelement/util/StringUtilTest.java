package cn.rootelement.util;

import org.junit.jupiter.api.Test;

class StringUtilTest {

    @Test
    void getUUIDWithoutJoiner() {
        for (int i = 0; i < 10; i++) {
            String uuidWithoutJoiner = StringUtil.getUUIDWithoutJoiner();
            System.out.println(uuidWithoutJoiner);
        }
    }
}