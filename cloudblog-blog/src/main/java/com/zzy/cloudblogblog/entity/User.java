package com.zzy.cloudblogblog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zzy
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "t_user")
public class User {
    /**
     * 用户标识
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 联系邮件
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型:1-admin、2-user、3-guest
     */
    private Integer type;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新用户时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}