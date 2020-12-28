package com.zzy.cloudblogblog.dto;

import com.zzy.cloudblogblog.entity.Blog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 传递到前端的Type对象，和entity的type的区别是此TypeDTO里含有List<Blog>
 *
 * @author zzy
 * @Date 2020/12/28 20:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeDTO {

    /**
     * 类型名称——和前端对应
     */
    private String typeName;
    /**
     * 类型的标识——和前端对应
     */
    private Integer typeId;
    /**
     * 包含的博客——命名和前端对应
     */
    private List<Blog> blogs;

}
