package com.zzy.cloudblogblog.dto;

import com.zzy.cloudblogblog.entity.Blog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/29 9:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogArchiveDTO {

    /**
     * 当前年份
     */
    private Integer curYear;

    /**
     * 同一年的所有博客
     */
    private List<Blog> blogsOfYear;


}
