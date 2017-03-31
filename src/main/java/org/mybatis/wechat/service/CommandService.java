package org.mybatis.wechat.service;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.wechat.entity.Command;
import org.mybatis.wechat.utils.MybatisUtils;

import java.io.IOException;
import java.util.List;

/**
 * Description: 与新的指令相关的Service服务类
 *
 * @author: dong
 * @create: 2017-01-02-11:54
 */
public class CommandService {

    // 声明MybatisUtils用来获取SqlSession对象来执行sql语句.
    private MybatisUtils mybatisUtils = new MybatisUtils();

    /**
     * 根据指令command和描述信息description查询Command内容.
     * */
    public List<Command> queryCommandList(String command, String description) {
        // 声明一个Command对象用来封装请求的信息
        Command commandObj = new Command();
        // 声明一个SqlSession对象,用来只想数据库会话
        SqlSession sqlSession = null;
        // 声明List对象用来存储从数据库查询得到的所有Command对象.
        List<Command> commandList = null;

        // 将请求的内容封装到commandObj对象中.
        if(command != null && !"".equals(command.trim())) {
            commandObj.setName(command);
        }
        if(description != null && !"".equals(description.trim())) {
            commandObj.setDescription(description);
        }
        try {
            // 调用底层的MybatisUtils对象获取SqlSession.
            sqlSession = this.mybatisUtils.getSqlSession();
            // 执行案条件查询的sql语句,返回查询之后的结果集.
            commandList = sqlSession.selectList("Command.queryCommnadList", commandObj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != sqlSession) {
                // 语句执行完成之后关闭数据库的会话,释放数据库连接资源.
                sqlSession.close();
            }
        }
        return commandList;
    }

}
