package com.bingo.learn.domain;

import lombok.Data;

/**
 * Created by ing on 2022/1/7 11:11
 */
@Data
public class R {
    private Object data;
    private Boolean success;
    private String msg;

    public R(){

    }
    public R(Object data, Boolean success) {
        this(success);
        this.data = data;
    }

    public R(Boolean success) {
        this.success = success;
    }
    public R(Boolean success,String msg){
        this(success);
        this.msg=msg;
    }

    public R(String msg) {
        this.success = false;
        this.msg = msg;
    }
}
