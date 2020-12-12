package com.zzy.cloudbloguser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzy
 * @Date 2020/12/2 17:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogDTO {
    private Integer blogId;

    /**
     * 博客名称
     */
    private String title;

    /**
     * 用户标识
     */
    private String userName;

    /**
     * 博客类型标识
     */
    private Integer typeId;

}
