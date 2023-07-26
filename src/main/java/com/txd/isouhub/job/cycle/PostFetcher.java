package com.txd.isouhub.job.cycle;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.txd.isouhub.model.entity.Post;
import com.txd.isouhub.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时抓取帖子数据
 */
//@Component
@Slf4j
public class PostFetcher {

    @Resource
    private PostService postService;

    private static final int MAX_CURRENT = 5;

    private int current = 1;

    /**
     * 每10小时执行一次
     */
    @Scheduled(fixedRate = 36000 * 1000)
    public void run() {
        if (current > MAX_CURRENT) {
            log.info("定时任务执行结束");
            return;
        }
        // 发送请求，获取响应数据
        String requestBody = "{\n" +
                "  \"current\": " + current + ",\n" +
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
        if (b) {
            log.info("文章数据添加成功，条数：{}", postList.size());
        } else {
            log.error("文章数据添加失败");
        }
        current++;
    }
}
