package com.zzy.cloudblogblog.service;

import com.zzy.cloudblogblog.dao.blog.BlogMapper;
import com.zzy.cloudblogblog.dto.BlogDTO;
import com.zzy.cloudblogblog.dto.UserDTO;
import com.zzy.cloudblogblog.entity.blog.Blog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/2 16:52
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogService {
    private final BlogMapper blogMapper;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    public BlogDTO queryById(Integer blogId) {
        Blog blog = this.blogMapper.selectByPrimaryKey(blogId);
        //根据blog的userId查询出user
        //TODO 根据blog的userId获取user的详细信息，如username和avatar
        log.info("template请求user服务...");
//        List<ServiceInstance> userServiceInstances = discoveryClient.getInstances("user-service");
//        String des = userServiceInstances.stream()
//                .map(serviceInstance -> serviceInstance.getUri().toString() + "user/{id}")
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("未找到服务组件..."));
//        log.info("请求的地址是{}...", des);
        UserDTO userDTO = restTemplate.getForObject("http://user-service/user/{userId}", UserDTO.class, blog.getUserId());
        return BlogDTO.builder()
                .title(blog.getTitle())
                .userName(userDTO.getUsername())
                .blogId(blog.getBlogId())
                .typeId(blog.getTypeId())
                .build();
    }

}
