package com.zzy.cloudblogblog.rocketmq;

import com.alibaba.fastjson.JSON;
import com.zzy.cloudblogblog.dao.midtransaction.BlogRocketmqTransactionLogMapper;
import com.zzy.cloudblogblog.entity.blog.Blog;
import com.zzy.cloudblogblog.entity.midtransaction.BlogRocketmqTransactionLog;
import com.zzy.cloudblogblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @author zzy
 * @Date 2020/12/4 14:26
 */
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "blog2user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBlogTransactionListener implements RocketMQLocalTransactionListener {
    private final BlogService blogService;
    private final BlogRocketmqTransactionLogMapper blogRocketmqTransactionLogMapper;

    /**
     * 发送半消息--确认半消息--【执行本地事务】--二次确认--回查--返回rollback或者commit
     *
     * @param message
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        //从参数里得到 1消息体、2中间事务Id、3本次表Id
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get("transactionId");
        Integer blogId = Integer.valueOf((String) headers.get("blogId"));
        Blog blog = JSON.parseObject((String) headers.get("blog"), Blog.class);
        //注入用到的service——调用事务方法
        //注入中间表mapper——记录中间表，回查
        try {
            //执行本地事务 + 向中间事务表中添加记录——checkLocalTransaction时会用到
            log.info("即将执行本地事务...");
            this.blogService.updateBlogWithRocketTransaction(blog, transactionId.toString());
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.info("执行本地事务时发生异常：{}", e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 发送半消息--确认半消息--执行本地事务--二次确认--【回查】--返回rollback或者commit
     *
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        String transactionId = (String) message.getHeaders().get("transactionId");
        //回查时向【中间事务表】查找transactionId的记录是否存在，若存在，则提交commit，否则提交rollback
        BlogRocketmqTransactionLog blogRocketmqTransactionLog = this.blogRocketmqTransactionLogMapper.selectByPrimaryKey(transactionId);
        if (blogRocketmqTransactionLog != null) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

    }
}
