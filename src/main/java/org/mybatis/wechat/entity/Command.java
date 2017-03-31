package org.mybatis.wechat.entity;

import java.util.List;

/**
 * Description: 将message数据表拆分后的实体类，用来指代具体的命令Command
 *
 * @author: dong
 * @create: 2017-01-02-9:48
 */
public class Command {

    /**
     * 指令表Command的主键
     * */
    private String id;

    /**
     * 用来表示指令的名称
     * */
    private String name;

    /**
     * 用来表示指令的描述信息
     * */
    private String description;

    /**
     * 一条指令对应的自动回复内容列表.
     * */
    private List<CommandContent> contentList;

    /**
     * 私有属性的getters和setters.
     * */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CommandContent> getContentList() {
        return contentList;
    }

    public void setContentList(List<CommandContent> contentList) {
        this.contentList = contentList;
    }
}
