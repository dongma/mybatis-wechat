package org.mybatis.wechat.servlet;

import org.mybatis.wechat.service.QueryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Description: 执行自动回复功能的Servlet
 *
 * @author: dong
 * @create: 2017-01-01-17:47
 */
public class AutoReplyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Ajax请求处理,在req中设置要返回的数据格式编码
        resp.setContentType("text/html;charset=utf-8");
        // 获取用于向前台发送数据的PrintWriter对象
        PrintWriter out = resp.getWriter();
        // 创建QueryService类用于调用服务.
        QueryService queryService = new QueryService();
        // 调用service服务根据指令查询出其对应的说明.
        String content = queryService.queryByCommand(req.getParameter("content"));
        // 使用out将数据写出去
        out.write(content);
        // 当内容输出完成之后,关闭out输出流
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
