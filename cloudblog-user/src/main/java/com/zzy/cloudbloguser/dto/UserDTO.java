package com.zzy.cloudbloguser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzy
 * @Date 2020/12/2 17:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    /**
     * 用户标识
     */
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
}
