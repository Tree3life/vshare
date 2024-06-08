package com.tree3.service.mp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tree3.pojo.vo.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class VideoConsumer {

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public VideoConsumer(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    /**
     * 当接收到上传文件的消息时
     * 将该视频的消息进行分析，并将对应的分析结果存储到es对应的索引库中
     *
     * @param message
     * @throws IOException
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(name = "videos", type = "fanout")
            )
    )
    public void receiveUploadedVideoMsg(String message) throws IOException {
        log.info("MQ接收的video信息为: {}", message);

        //1.反序列化，将mq中video的json格式数据转为一个videoVo对象
        VideoVO videoVO = new ObjectMapper().readValue(message, VideoVO.class);

        //2.创建es 的 `索引请求对象`  参数1:操作索引  参数2:操作类型  参数3:文档id
        IndexRequest indexRequest = new IndexRequest("video", "video",videoVO.getId().toString());
        //3.设置ES文档的内容
        indexRequest.source(message, XContentType.JSON);
        //4.执行索引操作
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        log.info("video信息录入ES的状态为: {}", indexResponse.status());
    }
}