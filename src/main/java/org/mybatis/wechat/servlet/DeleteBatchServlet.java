package org.mybatis.wechat.servlet;

import org.mybatis.wechat.service.MessageService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 对于后台的记录进行批量删除的Servlet操作
 *
 * @author: dong
 * @create: 2017-01-01-15:46
 */
public class DeleteBatchServlet extends HttpServlet{

    // 声明MessageService对象,调用此服务进行业务逻辑的调用.
    private MessageService service = new MessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
        super.doGet(req, resp);
    }

    /**
     * 对于前台记录尽享批量删除的操作使用post方式进行提交.
     * */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
        // 从前台的jsp页面获取多个值,获取得到的是多个记录的id信息.
        String[] ids = req.getParameterValues("id");


    }
}
