package org.mybatis.wechat.service;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.wechat.entity.CommandContent;
import org.mybatis.wechat.utils.MybatisUtils;

import java.io.IOException;
import java.util.List;

/**
 * Description: ComContentService的服务类
 *
 * @author: dong
 * @create: 2017-01-19-22:56
 */
public class ComContentService {

    public void insertBatch(List<CommandContent> contentList) {
        SqlSession sqlSession = null;
        MybatisUtils utils = new MybatisUtils();
        try {
            // 对sqlSession进行实例化.
            sqlSession = utils.getSqlSession();
            // 通过SqlSession.getMapper方法获取得到方法的实例对象.
            IComContent iContent = sqlSession.getMapper(IComContent.class);
            // 调用批量新增的方法.
            iContent.insertBatch(contentList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();     // 关闭sqlSession对话对象,释放内存资源.
        }
    }
}
