package com.bingo.learn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bingo.learn.domain.Books;
import com.bingo.learn.mapper.BookMapper;
import com.bingo.learn.service.IBookService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * Created by ing on 2022/1/7 15:44
 * @author ing
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Books> implements IBookService {
    @Autowired
    private BookMapper bookMapper;
    @Override
    public IPage<Books> getPage(int current, int pageSize, Books book) {
        LambdaQueryWrapper<Books> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(book.getType()), Books::getType,book.getType());
        lqw.like(Strings.isNotEmpty(book.getName()), Books::getName,book.getName());
        lqw.like(Strings.isNotEmpty(book.getDescription()), Books::getDescription,book.getDescription());
        IPage<Books> page = new Page<>(current, pageSize);
        bookMapper.selectPage(page,lqw);
        return page;
    }
}
