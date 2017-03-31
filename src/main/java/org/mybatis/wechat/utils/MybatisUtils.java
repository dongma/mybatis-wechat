package org.mybatis.wechat.utils;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description: 使用Mybatis替换JDBC来执行底层的sql脚本
 *
 * @author: dong
 * @create: 2016-12-31-15:28
 */
public class MybatisUtils {

    public SqlSession getSqlSession() throws IOException {
        /**
         * 在maven项目中获取main/resources目录下文件的方式.
         * 1. MyClass.class.getClassLoder().getResource(FILE_NAME).getPath();直接获取文件的路径
         * 2. MyClass.class.getClassLoder().getResource("").getPath();获取classpath
         * */


        // 获取得到mybatis核心配置文件的路径
        // String resource = "/conf/mybatis-config.xml";
        /* String resource = this.getClass().getClassLoader()
                                .getResource("mybatis-config.xml").getPath();
           // 通过Resource类加载配置文件信息
           InputStream inputStream = Resources.getResourceAsStream(resource);   */

        InputStream inputStream = MybatisUtils.class.getResourceAsStream("/conf/mybatis-config.xml");
        // 通过加载配置文件InputStream获取得到SqlSessionFactory,Ctrl+t查找某个类的子类.
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 通过SqlSessionFactory得到一个SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }

}
