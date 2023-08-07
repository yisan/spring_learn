package com.bingo.learn.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ing on 2021/11/30 17:39
 */
public class MyBatisUtils {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        InputStream is = null;
        try {
            is = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获得SqlSession
     */
    public static SqlSession openSqlSession() throws IOException {
        return sqlSessionFactory.openSession();
    }

    /**
     * 提交释放资源
     */
    public static void commitAndClose(SqlSession sqlSession) {
        if (sqlSession != null) {
            sqlSession.commit();
            sqlSession.close();
        }
    }

    /**
     * 回滚释放资源
     */
    public static void rollbackAndClose(SqlSession sqlSession) {
        if (sqlSession != null) {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
}

