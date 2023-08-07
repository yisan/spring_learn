package com.bingo.learn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"test.prop=222"},args = {"--test.prop=333"})
class SpringbootConfigurationApplicationTests {
    @Value("${test.prop}")
    private String prop;
    @Test
    void testProperties() {
        System.out.println(prop);
    }
}
