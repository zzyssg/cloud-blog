package com.zzy.cloudblogblog.dao.blog;

import com.zzy.cloudblogblog.entity.blog.Blog;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zzy
 */
public interface BlogMapper extends Mapper<Blog> {

    /**
     * 返回所有博客
     * @return
     */
    List<Blog> listAllBlogs();

    /**
     * 查询某类下的所有博客
     * @param typeId
     * @return
     */
    List<Blog> listBlogsByTypeId(Long typeId);

    /**
     * 按查询条件查询符合要求的博客
     * @param blog
     * @return
     */
    List<Blog> listBlogsByCondition(Blog blog);
}