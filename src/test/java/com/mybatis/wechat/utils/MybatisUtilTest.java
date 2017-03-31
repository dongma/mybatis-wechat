package com.mybatis.wechat.utils;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.mybatis.wechat.utils.MybatisUtils;

import java.io.IOException;

/**
 * Description: 该类是对MybatisUtils工具类是否返回一个SqlSession进行测试
 *
 * @author: dong
 * @create: 2016-12-31-15:52
 */
public class MybatisUtilTest {

    @Test
    public void isGetSqlSession() {
        SqlSession sqlSession = null;
        try {
            MybatisUtils mybatisUtils = new MybatisUtils();
            sqlSession = mybatisUtils.getSqlSession();
            // 向控制台打印出得到的SqlSession对象
            System.out.println("sqlSession:" + sqlSession);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // sqlSession.close();;
        }
    }

}
