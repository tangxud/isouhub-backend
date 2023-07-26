package com.txd.isouhub.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txd.isouhub.model.dto.post.PostQueryRequest;
import com.txd.isouhub.model.entity.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 帖子服务测试
 */
@SpringBootTest
class PostServiceTest {

    @Resource
    private PostService postService;

    @Test
    void searchFromEs() {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setUserId(1L);
        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
        Assertions.assertNotNull(postPage);
    }

    @Test
    void testFetchPassage() {
        // 发送请求，获取响应数据
        String requestBody = "{\n" +
                "  \"current\": 1,\n" +
                "  \"pageSize\": 8,\n" +
                "  \"sortField\": \"_score\",\n" +
                "  \"sortOrder\": \"descend\",\n" +
                "  \"category\": \"简历\",\n" +
                "  \"reviewStatus\": 1\n" +
                "}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(requestBody)
                .execute().body();
        // 处理响应数据
        Map map = JSONUtil.toBean(result, Map.class);
        JSONObject jsonObj = (JSONObject) map.get("data");
        List<Post> postList = new ArrayList<>();
        JSONArray records = (JSONArray) jsonObj.get("records");
        for (Object record :
                records) {
            JSONObject data = (JSONObject) record;
            Post post = new Post();
            post.setTitle((String) data.get("title"));
            post.setContent((String) data.get("content"));
            JSONArray tags = (JSONArray) data.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(tagList.toString());
            post.setUserId(1659072136301559809L);
            postList.add(post);
        }
        // 数据入库
        boolean b = postService.saveBatch(postList);
        Assertions.assertTrue(b);
    }

}