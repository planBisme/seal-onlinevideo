package com.seal.onlinevideo.dao.repository;

import com.seal.onlinevideo.domain.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoRepository extends ElasticsearchRepository<Video, Long> {
    /**
     *
     * @param keyword
     * @return video
     */
    Video findByTitleLike(String keyword);
}
