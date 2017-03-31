package org.mybatis.wechat.entity;

/**
 * Created by 莫忘初衷 on 2016/12/19.
 * 与消息表对应的实体类
 */
public class Message {

    // 主键
    private Integer id;

    // 指令的名称
    private String command;

    // 指令内容的描述
    private String description;

    // 指令的内容
    private String content;

    // 私有属性的getter和setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", command='" + command + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
