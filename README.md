# mybatis-wechat 使用mybatis实现数据访问
>项目简介：整个项目实现的是类似微信公众号的功能，整个项目分为前后台两个部分：前台`talk.jsp`页面类似于微信公众号的界面，用户输入指令后，系统会根据指令自动随机回复出输入指令的内容。
>
>后台`list.jsp`页面是一个列表展示所有的指令对应以及其指令对应回复内容的列表。整个系统逻辑是当用户在前台输入指令后，系统会匹配指令回复指令对应的内容。   
### 项目使用到的技术点:
- JSP和Servlet：在项目中使用原生Http处理web端请求，借助于JSTL在JSP页面进行数据展示；
- MySQL数据库：在项目中使用MySQL作为数据库的存储技术，在系统中主要使用3张数据表：`command`数据表存放的是所有的指令，`command_content`数据表存放的是指令表对应指令的描述，`message`数据表是后期MyBatis接口化编程以及实现分页而是创建的一张数据表，将之前两张表的信息整合到了一块；
- MyBatis：在项目中前期使用JDBC作为DAO层的实现，后期使用MyBatis框架进行升级。使用的技术点主要包括有：MyBatis声明式接口编程，Dynamic SQL动态的SQL拼接包括常用的`<where>、<if>、<foreach>`等标签的使用，通过`<Plugin>`实现MyBatis的分页；
### 技术点原理分析：
- ***Mapper接口编程***：在MyBatis官方文档中介绍调用Mapper中xml配置文件的方式是通过`sqlSession`调用MyBatis的API接口通过ClassName.id对配置在xml中的sql语句进行调用：
```java
  // 1.通过sqlSession调用Message.xml文件中配置的<select>查询sql语句
  messageList = sqlSession.selectList("Message.queryMessageList", message);
```
使用MyBatis映射器Mapper自动实现DAO API： `<T> getMapper(Class<T> type)；`通过配置Mapper文件的namespace属性，Configuration配置类在解析Xml的时候会将其添加到其内置的Map中，其内部会通过Java的JDK动态代理生成接口的代理类。当调用getMapper(clazz); 方法的时候会生成接口的代理类，在Mapper文件中
的配置方式：

```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <!-- mapper标签中的namespace属性是用来区分不同的命名域,
        在不同的namespace中对于sql语句id命名就没有那么严格,可以有相同的id标识 -->
  <mapper namespace="org.mybatis.wechat.service.IMessage">
    ...其他SQL语句配置
  </mapper>
```
调用getMapper(Class clazz)的流程：`SqlSession(interface) --> 默认实现 DefalutSqlSession --> 配置类Configuration类--> MapperProxyFactory<T>.newInstance()方法；` MapperProxy类实现了JDK默认的`InvocationHandler`接口实现动态代理。
在MapperProxyFactory.newInstance()方法中通过`Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]
{ mapperInterface }, mapperProxy);方法生成动态代理的类；`以前通过sqlSession实现的DAO的方式现在也只需要
通过声明DAO接口就可以，接口中方法的名称也就是xml中SQL的ID：

```java
public interface IMessage {
    // 想代表那条sql,就将那条sql作为定义你方法的方法名.
    // 1.参数的类型为调用sql使用的参数类型.
    // 2.返回值的类型为查询sql返回的类型.
    public List<Message> queryMessageList(Message message);
    /**
     * mybatis的分页实现的原理,其中param代表的是传递给sql的预处理参数.
     * */
    public List<Message> queryMessageByPage(Map param);
    /**
     * 该方法用来统计Message数据表中记录数.
     * */
    public int countRecord(Message message);
}
```
***MyBatis拦截器分析***：在MyBatis官方文档中存在`Executor，ParameterHandler，ResultSetHandler，StatementHandler`四种拦截器，其中通过StatementHandler拦截器可以实现分页的功能。在`PageInterceptor`的interceptor中实现了分页的行为。首先拦截器需要实现`Interceptor`接口，同时在实现类上添加@Intercepts的注解：

```java
  @Intercepts({
        // Mybatis支持对Executor、StatementHandler、ParameterHandler、ResultsetHandler这四个对象进行拦截.
        // @Signature表示的是拦截对象 type[类型]  method[拦截方法] args[方法的参数]
        @Signature(
            type = StatementHandler.class,
            method = "prepare",
            args = {Connection.class}
        )
  })
```
在Intercept方法的内部通过Invocation获得拦截的对象，`MetaObject`类似于Struts2的OGNL表达式获取值
delegate.mappedStatement获取被拦截的Statement对象，最后将sql语句进行替换，然后调用invocation.proceed()进行放行；
