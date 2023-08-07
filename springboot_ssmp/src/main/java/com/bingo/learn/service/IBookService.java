package com.bingo.learn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bingo.learn.domain.Books;



/**
 * Created by ing on 2022/1/7 15:43
 * @author ing
 */
public interface IBookService extends IService<Books> {
    IPage<Books> getPage(int current,int pageSize,Books book);
}
