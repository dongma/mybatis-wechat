package org.mybatis.wechat.interceptor;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.mybatis.wechat.entity.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

/**
 * Description: Mybatis所有分页查询的拦截器
 * @author: dong
 *
 * @create: 2017-01-14-18:08
 */
@Intercepts({
        /**
         * Mybatis支持对Executor、StatementHandler、ParameterHandler、ResultsetHandler这四个对象进行拦截.
         * */
        // @Signature表示的是拦截对象 type[类型]  method[拦截方法] args[方法的参数]
        @Signature(
            type = StatementHandler.class,
            method = "prepare",
            args = {Connection.class}
        )
})
public class PageInterceptor implements Interceptor {

    /**
     * intercept方法是实现拦截逻辑的地方,内部要通过invocation.process()显示的推进责任链前进,
     * 也就是调用下一个拦截器拦截目标方法;
     * */
    public Object intercept(Invocation invocation) throws Throwable {
        /**
         *  Invocation类的属性有: Object target //被代理的对象
         *                       Method method // 被调用的方法
         *                       Object[] args // 调用的参数.
         * */
        // 从invocation中获取拦截的StatementHandler对象.
        StatementHandler handler = (StatementHandler)invocation.getTarget();
        // DefaultReflectorFactory应该是之前在SystemMetaObject定义的常量 SystemMetaObject.DEFAULT_REFLECTOR_FACTORY
        MetaObject metaObject = MetaObject.forObject(handler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        // 从metaObject中获取得到MappedStatement对象
        MappedStatement statement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
        // 获取配置文件中sql语句的Id编号.
        String id = statement.getId();
        if(id.matches(".+ByPage$")) {
            BoundSql boundSql = handler.getBoundSql();
            // 原始的sql语句
            String sql = boundSql.getSql();
            // 查询总条数的sql语句
            String countSql = "select count(*) from (" + sql  +") a";
            // 获取数据连接对象Connection
            Connection conn = (Connection)invocation.getArgs()[0];
            // 对计数的sql进行预处理.
            PreparedStatement countStatment = conn.prepareStatement(countSql);
            // 通过MetaObject.getValue方法获取预处理的参数.
            ParameterHandler paramHandler = (ParameterHandler)metaObject.getValue("delegate.parameterHandler");
            paramHandler.setParameters(countStatment);
            // 执行executeQuery()方法进行查询.
            ResultSet rs = countStatment.executeQuery();

            // 获取参数,对其进行替换.
            Map<?,?> parameter = (Map<?,?>)boundSql.getParameterObject();
            Page page = (Page) parameter.get("page");
            if(rs.next()) {
                page.setTotalNumber(rs.getInt(1));
            }
            // 改造后带分页查询的SQL语句
            String pageSql = sql + " limit " + page.getDbIndex() + "," + page.getDbNumber();
            // 对sql语句进行替换
            metaObject.setValue("delegate.boundSql.sql", pageSql);
        }
        // 对于调用其他类的方法直接对其进行释放,使用的是拦截器栈stack的方式进行拦截.
        // proceed()方法表示的是当前拦截器栈已经完成,程序流程进入下一个拦截器interceptor.
        return invocation.proceed();
    }

    /**
     * 是当前拦截器生成目标对象target对象的代理,实际上是通过Plugin.wrap(target,this)方法来完成的,
     * 把目标target和拦截器this传递给包装函数.
     * */
    public Object plugin(Object target) {
        // 调用Plugin.wrap方法.
        return Plugin.wrap(target, this);
    }

    /**
     * setProperties用来设置额外的参数,参数配置在拦截器的Properties文件里.
     * */
    // 该方法主要是加载配置在xml文件中<plugin>标签内部的属性.
    public void setProperties(Properties properties) {

    }
}
