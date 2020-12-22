package com.zzy.cloudblogblog.dao;

import com.zzy.cloudblogblog.entity.Blog;
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

    /**
     * 根据博客标识删除其余相关表的内容
     * @param blog
     */
    void deleteRelativeBlog(Blog blog);

    /**
     * 向t_blog的相关表t_blog_type、t_blog_user中添加记录
     * @param blog
     */
    void insertRelativeBlog(Blog blog);
}