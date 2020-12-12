package com.zzy.cloudblogblog.controller;

import com.zzy.cloudblogblog.dao.blog.BlogMapper;
import com.zzy.cloudblogblog.dto.BlogDTO;
import com.zzy.cloudblogblog.entity.blog.Blog;
import com.zzy.cloudblogblog.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zzy
 * @Date 2020/12/2 10:16
 */
@Slf4j
@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogController {

    private final BlogService blogService;


    @GetMapping("/{blogId}")
    public BlogDTO getBlog(@PathVariable Integer blogId) {
        return blogService.queryById(blogId);
    }

    @GetMapping("/testTrsc/{id}")
    public BlogDTO updateBlog(@PathVariable Integer id) {
        log.info("进入controller——addBlog,操作的博客ID是{}", id);
        Blog blog = this.blogService.doWithUpdateBlog(id);
        BlogDTO blogDTO = this.blogService.queryById(id);
        return blogDTO;
    }
}
