package org.mybatis.wechat.service;

import org.mybatis.wechat.entity.CommandContent;

import java.util.List;

/**
 * Description: IComContent的接口
 *
 * @author: dong
 * @create: 2017-01-19-23:01
 */
public interface IComContent {

    // 进行批量插入的方法insertBatch.
    public void insertBatch(List<CommandContent> comContentList);
}
