package org.mybatis.wechat.service;

import org.mybatis.wechat.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * Created by dong on 2017/1/8.
 * 与Message配置文件相对应的接口.
 */
public interface IMessage {

    // 想代表那条sql,就将那条sql作为定义你方法的方法名.
    // 1.参数的类型为调用sql使用的参数类型.
    // 2.返回值的类型为查询sql返回的类型.
    public List<Message> queryMessageList(Message message);

    /**
     * mybatis的分页实现的原理,其中param代表的是传递给sql的预处理参数.
     * */
    public List<Message> queryMessageByPage(Map param);

    /**
     * 该方法用来统计Message数据表中记录数.
     * */
    public int countRecord(Message message);
}
