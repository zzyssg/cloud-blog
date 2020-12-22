package com.zzy.cloudblogblog.service;

import com.zzy.cloudblogblog.dto.BlogDTO;
import com.zzy.cloudblogblog.entity.Blog;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/21 14:42
 */
public interface BlogService {

    /**
     * 按id查询博客
     * @param blogId
     * @return
     */
    BlogDTO queryById(Integer blogId);

    /**
     * 更新博客时附加操作
     * @param blogId
     * @return
     */
    Blog doWithUpdateBlog(Integer blogId);

    /**
     * 更新博客时附带事务
     * @param blog
     * @param transactionId
     * @return
     */
    String updateBlogWithRocketTransaction(Blog blog, String transactionId);

    /**
     * 查询所有博客
     * @return
     */
    List<Blog> listAllBlogs();

    /**
     * 查询具体某类型下的所有博客
     * @param typeId
     * @return
     */
    List<Blog> listBlogsByTypeId(Long typeId);

    /**
     * 按条件查询
     * @param blog
     * @return
     */
    List<Blog> listBlogsByCondition(Blog blog);


    /**
     * 根据唯一标识查询博客
     * @param blogId
     * @return
     */
    Blog getBlogById(Long blogId);

    /**
     * 根据参数新增博客
     * @param blog
     * @return
     */
    Boolean insertBlog(Blog blog);

    /**
     * 根据参数更新博客
     * @param blog
     * @return
     */
    Boolean updateBlog(Blog blog);

    /**
     * 删除博客
     * @param blog
     * @return
     */
    Boolean deleteBlog(Blog blog);
}
