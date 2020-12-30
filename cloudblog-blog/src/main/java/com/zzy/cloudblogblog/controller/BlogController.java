package com.zzy.cloudblogblog.controller;

import com.zzy.cloudblogblog.dto.BlogArchiveDTO;
import com.zzy.cloudblogblog.dto.BlogDTO;
import com.zzy.cloudblogblog.dto.TypeDTO;
import com.zzy.cloudblogblog.entity.Blog;
import com.zzy.cloudblogblog.entity.ResponseBean;
import com.zzy.cloudblogblog.entity.Type;
import com.zzy.cloudblogblog.enums.ResponseEnum;
import com.zzy.cloudblogblog.exception.CommonException;
import com.zzy.cloudblogblog.service.BlogService;
import com.zzy.cloudblogblog.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/2 10:16
 */
@Slf4j
@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogController {

    private final BlogService blogService;
    private final TypeService typeService;


    @GetMapping("/{blogId}")
    public BlogDTO getBlog(@PathVariable Integer blogId) {
        return blogService.getBlogById(blogId);
    }

    @GetMapping("/testTrsc/{id}")
    public BlogDTO updateBlog(@PathVariable Integer id) {
        log.info("进入controller——addBlog,操作的博客ID是{}", id);
        Blog blog = this.blogService.doWithUpdateBlog(id);
        BlogDTO blogDTO = this.blogService.getBlogById(id);
        return blogDTO;
    }

    //TODO 改为GetMapping

    /**
     * 查询所有的博客,返回blogVOs
     *
     * @return
     */
    @GetMapping("/getAllBlogVOs")
    public ResponseBean queryAllBlogDTOs() {
        ResponseBean result;
        List<Blog> blogs = blogService.listAllBlogs();
        if (blogs == null) {
            log.error("目前暂无博客!");
            throw new CommonException(ResponseEnum.BLOG_IS_NULL.getCode(),
                    ResponseEnum.BLOG_IS_NULL.getMsg());
        }
        List<BlogDTO> blogDTOs = new ArrayList<>(blogs.size());
        // TODO 删除BlogDTO
        for (Blog blog : blogs) {
            BlogDTO blogDTO = BlogDTO.builder()
                    .typeId(blog.getType().getTypeId())
                    .blogId(blog.getBlogId())
                    .title(blog.getTitle())
                    .content(blog.getContent())
                    .description(blog.getDescription())
                    .firstPicture(blog.getFirstPicture())
                    .recommend(blog.getRecommend())
                    .sharement(blog.getSharement())
                    .type(blog.getType())
                    .updateTime(blog.getUpdateTime())
                    .build();
            blogDTOs.add(blogDTO);
        }
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                blogDTOs);
        return result;
    }

    //TODO 改为GetMapping

    /**
     * 查询所有博客
     *
     * @return
     */
    @PostMapping("/findAllBlogs")
    public ResponseBean queryAllBlogs() {
        ResponseBean result;
        List<Blog> allBlogs = blogService.listAllBlogs();
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(), allBlogs);
        return result;
    }


    /**
     * 查询某种类型下的所有博客
     *
     * @return
     */
    @GetMapping("/queryBlogsByTypeId/{typeId}")
    public ResponseBean queryBlogsByTypeId(@PathVariable Long typeId) {
        ResponseBean result;
        //TODO 查询所有类型 + 查询某用户的所有博客 = 某用户的某类型下的所有博客
        List<Blog> blogsByTypeId = blogService.listBlogsByTypeId(typeId);
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                blogsByTypeId);
        return result;
    }

    /**
     * 条件查询博客列表
     *
     * @param blog
     * @return
     */
    @PostMapping("/queryBlogVOsByCondition")
    public ResponseBean queryBlogDTOsByCondition(@RequestBody Blog blog) {
        ResponseBean result;
        List<Blog> blogsByCondition = blogService.listBlogsByCondition(blog);
        if (blogsByCondition == null) {
            log.error("条件查询下的博客为空！");
            throw new CommonException(ResponseEnum.BLOG_CONDITION_IS_NULL.getCode(),
                    ResponseEnum.BLOG_CONDITION_IS_NULL.getMsg());
        }
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                blogsByCondition);
        return result;
    }

    /**
     * 根据主键查询博客
     *
     * @param blogId
     * @return
     */
    @GetMapping("/queryBlogById/{blogId}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean queryBlogById(@PathVariable Long blogId) {
        ResponseBean result;
        Blog blogById = blogService.getBlogById(blogId);
        if (blogById == null) {
            log.error("未查到id为{}的博客！", blogId);
            throw new CommonException(ResponseEnum.BLOG_ONID_IS_NULL.getCode(),
                    ResponseEnum.BLOG_ONID_IS_NULL.getMsg());
        }
        //TODO 将浏览量 + 1，更新blog
        blogById.setViews(blogById.getViews() + 1);
        blogService.updateBlog(blogById);
        return new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                blogById);

    }

    @GetMapping("/findAllBlogsByYear")
    public ResponseBean findAllBlogsByYear(){
        ResponseBean result;
        List<BlogArchiveDTO> blogArchiveDTOS = blogService.listAllBlogsByYear();
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                blogArchiveDTOS);
        return result;
    }

    /**
     * 查询所有的类型和标签，用于首页展示标签云和所有类型
     *
     * @return
     */
    @GetMapping("/findAllTypesAndTags")
    public ResponseBean queryAllTypesAndTags() {
        ResponseBean result;
        /*
         * 1、调用typeMapper，查询types
         * 2、调用tagsMapper，查询tags
         * 3、ArrayList，放入types、tags，
         * */
        List<TypeDTO> allTypes = typeService.listAllTypes();
        if (allTypes == null) {
            log.info("博客类型为空...");
            throw new CommonException(ResponseEnum.TYPE_IS_NULL.getCode(),
                    ResponseEnum.TYPE_IS_NULL.getMsg());
        }
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                allTypes);
        return result;
    }

    @PostMapping("/addBlog")
    public ResponseBean addBlog(@RequestBody Blog blog) {
        ResponseBean result;
        if (blog == null) {
            log.error("前端传来的blog对象为空!");
            throw new CommonException(ResponseEnum.BLOG_FROM_FRONT_IS_NULL.getCode(),
                    ResponseEnum.BLOG_FROM_FRONT_IS_NULL.getMsg());
        }
        //blogId已存在,执行更新操作
        if (blog.getBlogId() != null && !blog.getBlogId().equals(-1)) {
            blogService.updateBlog(blog);
            Blog blogById = blogService.getBlogById(blog.getBlogId().longValue());
            return new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                    ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                    blogById);
        }
        //执行添加操作
        /*新增操作——1、title不重复 2、重复*/
        /*查询所有的title，判断是否存在：若存在，*/
        if (blogService.insertBlog(blog)) {
            log.info("新增博客成功!博客信息为:{}", blog);
        }
        return new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                blog);
    }

    @GetMapping("/deleteBlogById")
    public ResponseBean deleteBlogById(@RequestParam("blogId") Long blogId) {
        ResponseBean result;
        //先查询此博客是否存在
        Blog blogById = blogService.getBlogById(blogId);
        if (blogById == null) {
            throw new CommonException(ResponseEnum.BLOG_ONID_IS_NULL.getCode(),
                    ResponseEnum.BLOG_ONID_IS_NULL.getMsg());
        }
        Boolean isDeleted = blogService.deleteBlog(blogById);
        if (!isDeleted) {
            log.error(ResponseEnum.DELETE_BLOG_ERROR.getMsg());
            return new ResponseBean(ResponseEnum.RESPONSE_FAILED.getCode(),
                    ResponseEnum.RESPONSE_FAILED.getMsg(),
                    blogById);
        }
        return new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getCode(),
                ResponseEnum.RESPONSE_SUCCESS.getMsg(),
                blogById);
    }

}
