package com.bingo.learn.impl;

import com.bingo.learn.dao.BookDao;
import org.springframework.stereotype.Repository;

/**
 * Created by ing on 2023/8/6 01:47
 */
@Repository
public class BookDaoImpl implements BookDao {
    @Override
    public void save() {
        System.out.println("save book...");
    }
}
