package com.mybatis.wechat.utils;

import org.junit.Test;
import org.mybatis.wechat.utils.DBUtils;

import java.sql.Connection;

/**
 * Created by 莫忘初衷 on 2016/12/19.
 */
public class DBUtilsTest {

    @Test
    public void testGetConnection() {
        DBUtils dbUtils = new DBUtils();
        Connection conn = dbUtils.getConnction();
        System.out.println("数据库连接对象:" + conn);
    }

}
