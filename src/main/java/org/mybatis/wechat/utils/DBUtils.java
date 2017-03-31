package org.mybatis.wechat.utils;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;


/**
 * Created by 莫忘初衷 on 2016/12/19.
 */
public class DBUtils {

    private static String username;
    private static String password;
    private static String url;
    private static String driver;

    // 静态初始化块,在类加载的时候从配置文件中读取数据库连接信息
    static {
        try {
            // 创建读取文件的输入流信息
            // InputStream in = new FileInputStream(new File("/conn.properties"));
            InputStream in = DBUtils.class.getResourceAsStream("/conn.properties");
            Properties prop = new Properties();
            // Properties加载配置文件
            prop.load(in);

            // 从配置文件中读取出数据库连接信息
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            url = prop.getProperty("url");
            driver = prop.getProperty("driverClass");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * 构造函数Constructor
     * */
    public DBUtils() {

    }


    /**
     *  从配置文件中加载连接数据库的信息,获取数据库连接.
     * */
    public Connection getConnction() {
        Connection conn = null;
        try {
            // 通过反射加载数据库连接的url
            Class.forName(driver);
            // 通过DriverManager获取连接对象
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }


    /**
     *  该方法主要用来在数据库中执行delete和update操作.
     *  使用jdbc的PrpperedStatement对sql语句进行预处理的操作,返回执行sql结束之后受影响的行数.
     * */
    public int executeSql(String sql, Object... params) throws SQLException {
        int effect = -1;        // 当前变量表示的是执行sql语句之后受影响的行数.
        // 声明一个预处理的对象PreparedStatement
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = this.getConnction();
            // 对sql语句进行预处理
            pstmt = conn.prepareStatement(sql);
            // 通过对变长数组参数进行处理来实现对sql脚本进行传参
            if(params.length > 0) {
                for(int i = 0; i < params.length; i++) {
                    pstmt.setObject(i+1, params[i]);
                }
            }
            // 执行sql语句返回受影响的行数
            effect = pstmt.executeUpdate();
        } finally {
            // 关闭连接数据库的资源.
            this.closeAll(conn, pstmt, null);
        }
        return effect;
    }


    // 当执行完查看操作之后,关闭数据链接释放资源.
    public void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 该方法用来执行查询的sql语句,对sql语句进行预处理同时通过params进行传递参数.
     * */
    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        // 声明一个预处理的PreparedStatement
        PreparedStatement pstmt = null;
        Connection conn = null;
        // 声明查询之后的结果集ResultSet
        ResultSet rs = null;

        // 获取数据库的连接对象.
        conn = this.getConnction();
        // 对sql语句进行预处理
        pstmt = conn.prepareStatement(sql);
        System.out.println(Arrays.toString(params));
        // 通过对变长数组参数进行处理来实现对sql脚本进行传参
        if(params.length > 0) {
            for(int i = 0; i < params.length; i++) {
                pstmt.setObject(i+1, params[i]);
            }
        }
        rs = pstmt.executeQuery();
        // 返回执行查询之后返回的结果集对象
        return rs;
    }
}
