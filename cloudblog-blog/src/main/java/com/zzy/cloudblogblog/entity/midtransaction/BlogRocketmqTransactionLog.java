package com.zzy.cloudblogblog.entity.midtransaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "t_blog_rocketmq_transaction_log")
public class BlogRocketmqTransactionLog {
    /**
     * lod_id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * blog事务id
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * blog事务日志
     */
    private String log;

}