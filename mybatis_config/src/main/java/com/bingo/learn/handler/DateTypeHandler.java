package com.bingo.learn.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by ing on 2021/11/27 00:37
 */
public class DateTypeHandler extends BaseTypeHandler<Date> {
    // 将Java类型转换成 数据库需要的类型
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType jdbcType) throws SQLException {
        long time = date.getTime();
        preparedStatement.setLong(i,time);
    }

    /**
     *  将数据库中类型转换从Java类型
     * @param resultSet 查询成的结果集
     * @param s  要转换的字段名称
     * @return
     * @throws SQLException
     */
    public Date getNullableResult(ResultSet resultSet, String s) throws SQLException {
        // 获得结果集中需要的数据（long）转成Date类型 返回
        long aLong = resultSet.getLong(s);
        Date date = new Date(aLong);
        return date;
    }
    // 将数据库中类型转换从Java类型
    public Date getNullableResult(ResultSet resultSet, int i) throws SQLException {
        long aLong = resultSet.getLong(i);
        Date date = new Date(aLong);
        return date;
    }
    // 将数据库中类型转换从Java类型
    public Date getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        long aLong = callableStatement.getLong(i);
        Date date = new Date(aLong);
        return date;
    }
}
