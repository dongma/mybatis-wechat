package org.mybatis.wechat.service;

import org.mybatis.wechat.entity.Command;
import org.mybatis.wechat.entity.CommandContent;
import org.mybatis.wechat.entity.Message;
import org.mybatis.wechat.entity.Page;
import org.mybatis.wechat.utils.Iconst;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Description: 查询相关的Service服务类
 *
 * @author: dong
 * @create: 2017-01-01-17:50
 */
public class QueryService {

    // 创建MessageService类用来执行sql查询.
    private MessageService service = new MessageService();
    // 创建CommandService用来执行sql语句.
    private CommandService commandService = new CommandService();

    /**
     * 通过指令查询自动回复的功能.
     * */
    public String queryByCommand(String command) {
        // 1.用来存储根据指令command查询得到的所有Message集合.
        // List<Message> messageList = null;

        // 2.modify:声明list对象用来存储查询得到的Command
        List<Command> commandList = null;

        // 当用户输入的是"帮助"的command的时候,在数据库中查询出所有的帮助提示
        if(Iconst.HELP_COMMAND.equals(command)) {
            // 1.从数据库中查询出所有的帮助列表信息.
            // messageList = this.queryMessageList(null, null);

            // 2.modify:调用CommandService从数据库中查询对应的指令的集合.
            commandList = this.commandService.queryCommandList(null, null);
            // 创建StringBuilder对象对所有查询到的命令进行拼接.
            StringBuilder result = new StringBuilder();
            for(int i = 0; i < commandList.size(); i++) {
                if(i != 0) {
                    result.append("<br/>");
                }
                result.append("回复[" + commandList.get(i).getName() + "]可以查看"
                        + commandList.get(i).getDescription());
            }
            return result.toString();
        }
        // 1.当是使用其他的命令进行查询的时候,将查询得到的结果进行返回.
        // messageList = this.queryMessageList(command, null);

        // 2.modify:调用CommandService来查询命令信息
        commandList = this.commandService.queryCommandList(command, null);
        // 当出现了同一个指令command对应多个内容的时候,默认只是会取得第一个查询到的结果.
        if(commandList.size() > 0) {
            // 1.返回查询得到的内容.
            // return messageList.get(0).getContent();
            // 2.modify: 随机生成回复的内容.
            List<CommandContent> contentList = commandList.get(0).getContentList();
            // 获取得到随机生成的命令内容的索引.
            int index = new Random().nextInt(contentList.size());
            // 返回查询得到对应command下方的描述信息description.
            return  contentList.get(index).getContent();
        }
        // 当输入的指令系统无法识别的时候,返回无法识别的字样.
        return Iconst.NO_MACHINE_CONTENT;
    }

    /**
     * 根据指令command和描述信息description查询Message内容.
     * */
    public List<Message> queryMessageList(String command, String description) {
        // 创建HashMap用来存放进行查询的参数
        HashMap<String, Object> params = new HashMap<String, Object>();
        // 将进行查询的参数放置在Map中.
        if(command != null && !"".equals(command.trim())) {
            params.put("command", command);
        }
        if(description != null && !"".equals(description.trim())) {
            params.put("description", description);
        }
        return service.mybatisQuery(params);
    }

    /**
     *  重载queryMessageList方法,增加分页的功能.
     * */
    public List<Message> queryMessagesByPage(String command, String description, Page page) {
        Map<String, Object> parameter = new HashMap<String, Object>();
        // 组织消息对象
        Message message = new Message();
        message.setCommand(command);
        message.setDescription(description);
        // 将获取得到的参数放置到HashMap中.
        parameter.put("message", message);
        // 调用底层的service层查询记录的总条数.
        int totalNumber = service.countRecord(message);
        // 组织分页查询参数.
        page.setTotalNumber(totalNumber);

        parameter.put("page", page);
        // 调用MessageService的mybatisQuery方法查询并返回结果.
        return service.mybatisQuery(parameter);
    }
}
