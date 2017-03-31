package org.mybatis.wechat.servlet;

import org.mybatis.wechat.entity.Message;
import org.mybatis.wechat.entity.Page;
import org.mybatis.wechat.service.QueryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by 莫忘初衷 on 2016/11/16.
 * 通过页面跳转展示所有回复信息的列表
 */
public class ListServlet extends HttpServlet {
    // 1.MessageService类只初始化一次，当url第一次访问web服务器的时候.
    // private MessageService service = new MessageService();
    // 2.使用QueryService调用mybatis的服务.
    QueryService service = new QueryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                                        throws ServletException, IOException {
        // 定义HashMap键值对object/key
        // HashMap<String, Object> hashMap = new HashMap<String, Object>();
        // 从request作用域中获取页面的参数
        String command = req.getParameter("command");
        // 判断其是否为空
        if(command != null && !"".equals(command.trim())) {
            // hashMap.put("command", command.trim());
            // 将command放在request作用域中,向页面传值
            req.setAttribute("command", command);
        }
        String description = req.getParameter("description");
        if(description != null && !"".equals(description.trim())) {
            // 1.对于JDBC的条件查询.
            // hashMap.put("description", "%" +description+ "%");
            // hashMap.put("description", description.trim());
            // 将descrption放在request作用域中,向页面传值
            req.setAttribute("description", description);
        }
        String currentPage = req.getParameter("currentPage");
        // 创建分页对象Page,并且使用正则表达式对currentPage参数进行校验.
        Page page = new Page();
        Pattern pattern = Pattern.compile("[0-9]{1,9}");
        // 当currentPage不符合正则表达式的时候,对其设置默认值1;当符合的时候使用Inrteger.valueOf()进行转型.
        if(currentPage == null || !pattern.matcher(currentPage).matches()) {
            page.setCurrentPage(1);
        } else {
            page.setCurrentPage(Integer.valueOf(currentPage));
        }

        try {
            // 1.使用JDBC接口对所有Message信息进行查询.
            // List<Message> listMessage =  service.getAllMessages();
            // 2.通过调用service的getMessagesByCondition方法对信息进行模糊查询.
            // List<Message> listMessage = service.getMessagesByCondition(hashMap);
            // 3使用Mybatis作为底层实现对Message信息进行查询.
            // List<Message> listMessage = service.mybatisQuery(hashMap);

            // 4.实现mybatis的分页技术,调用service服务进行分页的查询.
            List<Message> listMessage = service.queryMessagesByPage(command, description, page);
            // 将Page放置在request作用域中,将分页的信息在页面上进行展示.
            req.setAttribute("page", page);
            // 将查询得到的所有Message的信息放到List集合中.
            req.setAttribute("mesList", listMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 当前的servlet接收到客户端的url地址访问的时候,将用户的请求转发到到安全目录下的list.jsp
        req.getRequestDispatcher("/WEB-INF/jsps/back/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                        throws ServletException, IOException {
        doGet(req, resp);
    }
}
