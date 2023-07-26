package com.txd.isouhub.esdao;

import com.txd.isouhub.model.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 帖子 ES 操作
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
    /**
     * 通过用户名查询帖子
     *
     * @param userId
     * @return
     */
    List<PostEsDTO> findByUserId(Long userId);

    /**
     * 通过标题查询帖子
     *
     * @param title
     * @return
     */
    List<PostEsDTO> findByTitle(String title);

}