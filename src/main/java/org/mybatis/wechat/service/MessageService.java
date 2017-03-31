package org.mybatis.wechat.service;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.wechat.entity.Message;
import org.mybatis.wechat.utils.DBUtils;
import org.mybatis.wechat.utils.MybatisUtils;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Description: Service服务类，该类用来调用底层的JDBC来获取数据
 * @author dong
 * @create 2016-12-20-22:01
 */
public class MessageService {

    private DBUtils dbUtils = null;

    private MybatisUtils mybatisUtils = null;

    public MessageService() {
        dbUtils = new DBUtils();
        mybatisUtils = new MybatisUtils();
    }

    /**
     * 调用底层JDBC来查询出所有的数据,并且对查询到的记录进行封装成对象.
     * */
    public List<Message> getAllMessages() throws SQLException {
        List<Message> mesList = new ArrayList<Message>();
        ResultSet rs = null;
        Message message = null;

        // 表示从数据库中查询所有Message信息的sql语句
        String sql = "select id, command, description, content from message";
        // 调用底层的DBUtils从数据库中查询出数据
        rs = this.dbUtils.executeQuery(sql);
        // 取出查询到的所有记录，并且将其封装成为Message对象
        while(rs.next()) {
            message = new Message();
            // 通过rs.get方法从记录中找出所有的信息
            message.setId(rs.getInt("id"));
            message.setCommand(rs.getString("command"));
            message.setContent(rs.getString("content"));
            message.setDescription(rs.getString("description"));
            // 将查询到的记录信息封装成为的对象添加到List中
            mesList.add(message);
        }
        return mesList;
    }

    /**
     * 根据查询的条件拼接sql并且在程序中执行.
     * */
    public List<Message> getMessagesByCondition(HashMap<String, Object> map) throws SQLException {
        ResultSet rs = null;
        Message message = null;
        // 定义的List集合用来在ArrayList中添加对象.
        List<Message> messageList = new ArrayList<Message>();
        // 创建List<Object>列表对象对sql语句进行预处理.
        List<Object> params = new ArrayList<Object>();
        // 声明StringBuilder动态字符串用于拼接sql语句.
        StringBuilder sql = new StringBuilder(
                    "select id, command, description, content from message where 1 = 1 ");

        // 根据map中传递的key-value键值对拼接sql查询的字符串.
        if(map.get("command") != null) {
            sql.append(" and command = ?");
            params.add(map.get("command"));
        }
        if(map.get("description") != null) {
            sql.append(" and description like ?");
            params.add(map.get("description"));
        }
        System.out.println(sql.toString());
        // 调用底层DBUtils中的executeSql执行sql语句,在进行传参的时候，需要将List转换成为Array.
        rs = this.dbUtils.executeQuery(sql.toString(), params.toArray());
        // 取出从JDBC中查询sql返回的结果集
        while(rs.next()) {
            message = new Message();
            // 通过rs.get方法从记录中找出所有的信息
            message.setId(rs.getInt("id"));
            message.setCommand(rs.getString("command"));
            message.setContent(rs.getString("content"));
            message.setDescription(rs.getString("description"));
            // 将查询到的记录信息封装成为的对象添加到List中
            messageList.add(message);
        }
        return messageList;
    }

    /**
     * 该方法是使用mybatis作为底层实现来完成查询的操作.
     * 同时可以动态的拼接sql按条件进行查询
     * */
    public List<Message> mybatisQuery(Map<String, Object> param) {
        // 声明SqlSession对象,当执行查询完毕之后用于关闭数据库连接的资源.
        SqlSession sqlSession = null;
        List<Message> messageList = null;
        try {
        /*    // 创建一个Message对象,用于向Mybatis中传递信息
            Message message = new Message();
            if(param.get("command") != null) {
                System.out.println("command: " + (String)param.get("command"));
                message.setCommand((String)param.get("command"));
            }
            if(param.get("description") != null) {
                System.out.println("description: " + (String)param.get("description"));
                message.setDescription("%"+(String)param.get("description")+"%");
            } */
            // 从MybatisUtils中获取一个SqlSession对象用来执行查询的语句.
            sqlSession = this.mybatisUtils.getSqlSession();
            // 1.通过sqlSession调用Message.xml文件中配置的<select>查询sql语句
            // messageList = sqlSession.selectList("Message.queryMessageList", message);
            // 2.通过Mybatis面向接口的编程方式来执行sql语句.
            IMessage iMessage = sqlSession.getMapper(IMessage.class);
            // 3.通过iMessage.queryMessageList的方法调用执行sql语句;经过了动态代理.
            // messageList = iMessage.queryMessageList(message);
            // 3.使用mybatis的分页查询进行查询数据.
            messageList = iMessage.queryMessageByPage(param);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(sqlSession != null) {
                sqlSession.close();
            }
        }
        return messageList;
    }

    /**
     *  mybatis中统计Message数据表记录信息.
     * */
    public int countRecord(Message message) {
        SqlSession sqlSession = null;
        int recordNum = -1;

        try {
            // 调用底层的mybatisUtils创建SqlSession对象.
            sqlSession = this.mybatisUtils.getSqlSession();
            IMessage iMessage = sqlSession.getMapper(IMessage.class);
            recordNum = iMessage.countRecord(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return  recordNum;
    }


    /**
     * 通过传递的主键id的值,删除Message数据表中的单条记录.
     * */
    public void deleteOne(String id) {
        int idrecord = 0;
        // 声明Mybatis只想数据库回话的SqlSession对象.
        SqlSession sqlSession = null;
        if(id != null && !"".equals(id)) {
            // 当从前台传递过来的id编号不为空的时候,对其进行类型的转换,将String类型转换为int类型.
            idrecord = Integer.valueOf(id.trim());
        }
        try {
            // 调用mybatis执行删除的操作
            sqlSession = this.mybatisUtils.getSqlSession();
            // 使用sqlSession调用在mapper中配置的delete删除语句.
            sqlSession.delete("Message.deleteOne", idrecord);
            // 在使用JDBC的时候,事务是默认提交的,在Mybatis中需要显示的执行commit的操作.
            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 当执行完事务操作之后,关闭session对象,释放数据库连接资源.
            if(null != sqlSession) {
                sqlSession.close();
            }
        }
    }

    /**
     * 该方法是进行批量删除操作的方法,接受的参数是从前台传递过来的记录的id编号.
     * */
    public void deleteBatch(String[] ids) {
        // 声明Mybatis只想数据库回话的SqlSession对象.
        SqlSession sqlSession = null;
        List<Integer> idrecords = new ArrayList<Integer>();
        // 在Message.xml问价中我们配置的是List的类型,因而需要将String类型的对象转换为Integer类型.
        for(String idstr : ids) {
            if(idstr != null && !"".equals(idstr.trim())) {
                // 将String类型的id转换成为Integer类型.
                idrecords.add(Integer.parseInt(idstr));
            }
        }
        try {
            // 调用mybatis执行删除的操作
            sqlSession = this.mybatisUtils.getSqlSession();
            // 使用sqlSession调用在mapper中配置的delete删除语句.
            sqlSession.delete("Message.deleteBatch", idrecords);
            // 在使用JDBC的时候,事务是默认提交的,在Mybatis中需要显示的执行commit的操作.
            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 当执行完事务操作之后,关闭session对象,释放数据库连接资源.
            if(null != sqlSession) {
                sqlSession.close();
            }
        }
    }
}
