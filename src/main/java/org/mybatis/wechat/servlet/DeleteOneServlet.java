package org.mybatis.wechat.servlet;

import org.mybatis.wechat.service.MessageService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 删除单条记录的Servlet
 *
 * @author: dong
 * @create: 2017-01-01-14:56
 */
public class DeleteOneServlet extends HttpServlet {

    // 声明执行数据库操作的Service对象.
    private MessageService messageService = new MessageService();

    /**
     * 对于get方式提交删除操作的处理.
     * */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String recordId = req.getParameter("id");
        // 当从前台的Servlet请求中传递过来的参数id不为空的时候,调用service进行逻辑处理.
        if(recordId != null && !"".equals(recordId.trim())) {
            this.messageService.deleteOne(recordId.trim());
        }
        // 当对于单条记录的参数操作执行成功之后,将请求重定向到List.action,在后台重新找到要展示数据.
        req.getRequestDispatcher("/ListServlet.action").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }
}
