package com.zzy.cloudblogblog.service;

import com.alibaba.fastjson.JSON;
import com.zzy.cloudblogblog.dao.blog.BlogMapper;
import com.zzy.cloudblogblog.dao.midtransaction.BlogRocketmqTransactionLogMapper;
import com.zzy.cloudblogblog.dto.BlogDTO;
import com.zzy.cloudblogblog.dto.UserDTO;
import com.zzy.cloudblogblog.entity.blog.Blog;
import com.zzy.cloudblogblog.entity.midtransaction.BlogRocketmqTransactionLog;
import com.zzy.cloudblogblog.feignclient.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author zzy
 * @Date 2020/12/2 16:52
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogService {
    private final BlogMapper blogMapper;
    private final UserServiceFeignClient userServiceFeignClient;
    private final BlogRocketmqTransactionLogMapper blogRocketmqTransactionLogMapper;
    private final Source source;


    public BlogDTO queryById(Integer blogId) {
        Blog blog = this.blogMapper.selectByPrimaryKey(blogId);
        //根据blog的userId查询出user
        //TODO 根据blog的userId获取user的详细信息，如username和avatar
        log.info("feign发送Http请求...");
        UserDTO userDTO = userServiceFeignClient.findById(blog.getUserId());
        log.info("userDTO: {}", userDTO);
        return BlogDTO.builder()
                .title(blog.getTitle())
                .userName(userDTO.getUsername())
                .blogId(blog.getBlogId())
                .typeId(blog.getTypeId())
                .build();
    }


    public Blog doWithUpdateBlog(Integer blogId) {
        //do something--某种业务
        Blog blog = this.blogMapper.selectByPrimaryKey(blogId);
        if (blog != null) {
            blog.setTitle("我是一个经过事务的blog.");
            //某种业务执行完毕，供下游使用，发送半消息
            String transactionId = UUID.randomUUID().toString();
            this.source.output().send(
                    MessageBuilder.withPayload(
                            BlogDTO.builder()
                                    .title("业务完成，发送给下游消息...")
                                    .userName("me")
                                    .build()
                    )
                            //多次setHeader，可以传参
                            .setHeader("transactionId", transactionId)
                            .setHeader("blogId", blog.getBlogId())
                            .setHeader("blog", JSON.toJSONString(blog))
                            .build()
            );
        } else {
            log.info("查询的blog不存在，无法给下游业务提供消息...");
        }
        return blog;
    }

    /**
     * 参数包含执行本地事务(LocalTransaction)需要的参数
     * 以及向中间事务表t_blog_rocketmq_transaction_log插入日志记录的id
     *
     * @param blog
     * @param transactionId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateBlogWithRocketTransaction(Blog blog, String transactionId) {
        //本地事务 + 记录中间事务表 —— 两者同时成功或者失败，回查时本地事务是否成功的标志
        this.blogMapper.updateByPrimaryKeySelective(blog);
        this.blogRocketmqTransactionLogMapper.insertSelective(
                BlogRocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .log("本地事务在执行addBlog事务...")
                        .build()
        );
        return blog.toString();
    }

}
