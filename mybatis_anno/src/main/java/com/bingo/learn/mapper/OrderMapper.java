package com.bingo.learn.mapper;

import com.bingo.learn.domain.Order;
import com.bingo.learn.domain.User;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by ing on 2021/11/29 17:56
 */
public interface OrderMapper {
    @Select("select * from orders where uid = #{uid}")
    List<Order> findByUid(String uid);
    //对应association的写法
    @Select("select * from orders")
    @Results({
            @Result(column = "oid", property = "id"),
            @Result(column = "ordertime", property = "ordertime"),
            @Result(column = "total", property = "total"),
            @Result(
                    property = "user", //要封装的属性名称
                    column = "uid",// 根据上面查询结果中的哪个字段去查user表的数据
                    javaType = User.class,// 要封装的实体类
                    one = @One(select = "com.bingo.learn.mapper.UserMapper.findById" //参数就是colum对应的值
                    )
            )
    })
    List<Order> findAll();
    // @Select("SELECT *,o.id oid from orders o LEFT JOIN  user u ON uid = u.id\n")
    // @Results({
    //         @Result(column = "oid", property = "id"),
    //         @Result(column = "ordertime", property = "ordertime"),
    //         @Result(column = "total", property = "total"),
    //         @Result(column = "uid", property = "user.id"),
    //         @Result(column = "name", property = "user.name"),
    //         @Result(column = "address", property = "user.address"),
    //         @Result(column = "birthday", property = "user.birthday")
    // })
    // List<Order> findAll();
}
