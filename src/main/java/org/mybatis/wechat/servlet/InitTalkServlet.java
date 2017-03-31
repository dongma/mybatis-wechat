package org.mybatis.wechat.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description: 初始化Talk谈论的页面,向对话页面进行跳转.
 *
 * @author: dong
 * @create: 2017-01-01-17:08
 */
public class InitTalkServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 向对话的页面talk.jsp进行跳转
        req.getRequestDispatcher("/WEB-INF/jsps/front/talk.jsp").forward(req, resp);
    }
}
