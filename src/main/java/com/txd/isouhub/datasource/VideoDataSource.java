package com.txd.isouhub.datasource;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txd.isouhub.model.entity.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VideoDataSource implements DataSource<Video> {
    @Override
    public Page<Video> doSearch(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        if (pageSize > 30) {
            pageSize = 30;
        }
        String queryUrl;
        if (searchText == null || "".equals(searchText))
            searchText = "rabbitmq";
        queryUrl = String.format("https://api.bilibili.com/x/web-interface/wbi/search/type?search_type=video&keyword=%s&page=%d&dynamic_offset=%d", searchText, pageNum, current);
        log.info("queryUrl:{}", queryUrl);
        String result = HttpRequest.get(queryUrl).cookie(
                new HttpCookie("SESSDATA", "b160f8f5%2C1705658287%2Cd770a%2A71OKqhgEOAvoy-80QxP9O5kFD-YaAOKAZk6XX0GAUkeHraQocFpiFvddl3NuwfPlXuqnAl9gAAHQA"),
                new HttpCookie("buvid3", "8250AB9C-9E1B-EA69-7196-4CB386D0E93D93988infoc")
        ).execute().body();

        // 处理响应数据
        Map map = JSONUtil.toBean(result, Map.class);
        JSONObject jsonObj = (JSONObject) map.get("data");
        List<Video> videoList = new ArrayList<>();
        JSONArray records = (JSONArray) jsonObj.get("result");
        for (Object record :
                records) {
            JSONObject data = (JSONObject) record;
            Video video = new Video();
            video.setPic((String) data.get("pic"));
            video.setArcurl((String) data.get("arcurl"));
            video.setTitle((String) data.get("title"));
            videoList.add(video);
            if (videoList.size() >= pageSize) {
                break;
            }
        }
        Page<Video> page = new Page<>(current, pageSize);
        page.setRecords(videoList);
        return page;
    }
}
