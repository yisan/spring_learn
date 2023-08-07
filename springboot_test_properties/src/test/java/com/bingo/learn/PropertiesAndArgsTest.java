package com.bingo.learn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
// properties 属性可以为当前测试用例添加专用的临时属性配置
// args 属性可以为当前测试用例添加专用的临时的命令行参数
@SpringBootTest(properties = {"test.prop=222"},args = {"--test.prop=333"})
class PropertiesAndArgsTest {
    @Value("${test.prop}")
    private String prop;
    @Test
    void testPropertiesAndArgs() {
        System.out.println(prop);
    }
}
