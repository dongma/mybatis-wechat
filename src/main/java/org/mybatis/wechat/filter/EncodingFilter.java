package org.mybatis.wechat.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * Created by 莫忘初衷 on 2016/11/16.
 */
public class EncodingFilter implements Filter {
    // 当前字符串string用于读取从web.xml文件中配置的字符编码
    private String encoding;

    public void init(FilterConfig filterConfig) throws ServletException {
        // 从web.xml文件中获取初始化参数
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 对于响应和请求进行设置编码
        if(encoding != null) {
            request.setCharacterEncoding(encoding);
            // 设置response的字符编码
            response.setContentType("text/html;charset=utf-8");
        }
        // 在处理完编码的问题之后,对用户的请求进行放行
        chain.doFilter(request, response);
    }

    public void destroy() {

    }
}
