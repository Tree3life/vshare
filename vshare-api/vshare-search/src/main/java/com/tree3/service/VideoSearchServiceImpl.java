package com.tree3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tree3.pojo.vo.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/16 16:29 </p>
 */
@Slf4j
@Service
public class VideoSearchServiceImpl implements VideoSearchService {
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public VideoSearchServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public Map<String, Object> videos(String q, Integer page, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();

        //1.构建搜索请求
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //2.构建`查询类型`
        TermQueryBuilder termQuery = QueryBuilders.termQuery("title", q);

        sourceBuilder
                //分页查询
                .from((page - 1) * pageSize)
                .size(pageSize)
                //设置查询类型
                .query(termQuery);

        //3.指明要检索的索引库
        // `索引`= video  设置搜索类型video  设置搜索条件
        searchRequest.indices("video").types("video").source(sourceBuilder);

        SearchResponse search = null;

        try {
            //4.执行搜索
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            //5.获取符合条件总数
            long totalHits = search.getHits().totalHits;
            result.put("total_count", totalHits);

            //6.封装结果
            if (totalHits > 0) {

                //7.获取符合条件文档数组
                SearchHit[] hits = search.getHits().getHits();

                //8.封装结果
                List<VideoVO> videoVOS = Arrays.stream(hits)
                        .map((item) -> {
                            //10.获取文件字符串表现形式  就是json格式
                            String sourceAsString = item.getSourceAsString();
                            log.info("符合条件的结果: {}", sourceAsString);
                            //11.通过jackson将json格式转为videoVo对象
                            VideoVO videoVO = null;
                            try {
                                videoVO = new ObjectMapper().readValue(sourceAsString, VideoVO.class);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            //12.设置videoVo文档id
                            videoVO.setId(Integer.parseInt(item.getId()));
                            return videoVO;
                        })
                        .collect(Collectors.toList());

                result.put("items", videoVOS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
