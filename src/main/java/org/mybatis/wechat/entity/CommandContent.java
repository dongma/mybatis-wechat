package org.mybatis.wechat.entity;

/**
 * Description: 将Message数据表拆分后的实体类，用来指代指令的内容
 *
 * @author: dong
 * @create: 2017-01-02-9:49
 */
public class CommandContent {

    /**
     * 指令内容对应的主键.
     * */
    private String id;

    /**
     * 自动回复的功能.
     * */
    private String content;

    /**
     * 关联主键表的id.
     * */
    private String commandId;

    /**
     * CommandContent私有属性的setter和getter方法.
     * */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
