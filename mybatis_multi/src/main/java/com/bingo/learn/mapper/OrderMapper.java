package com.bingo.learn.mapper;

import com.bingo.learn.domain.Order;

import java.util.List;

/**
 * Created by ing on 2021/11/28 23:48
 */
public interface OrderMapper {
    List<Order> findAll();
}
