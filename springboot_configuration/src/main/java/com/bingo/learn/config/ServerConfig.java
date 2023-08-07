package com.bingo.learn.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by ing on 2022/1/12 10:53
 * @author ing
 */
@Data
@ConfigurationProperties(prefix = "servers")
@Validated
public class ServerConfig {
    private String ip;
    @Max(value = 8888,message = "最大值不能超过8888")
    @Min(value = 202,message = "最小值不能小于202")
    private int port;
}
