package hdyoon.bbs.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hdyoon.bbs.dto.BbsPostDto;
import hdyoon.bbs.wrapper.SnsMessageWrapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqsMessagingService {

    private final ObjectMapper objectMapper;
    private final BbsService bbsService;

    @SqsListener(value = "${aws.sqs.queue-name:}")
    public void receiveMessage(
            @Payload String message){
        try{
            log.info("SQS에서 메세지 수신: {}", message);

            SnsMessageWrapper snsMessageWrapper = objectMapper.readValue(message, SnsMessageWrapper.class);

            String actualMessage = snsMessageWrapper.getMessage();
            BbsPostDto bbsPostDto = objectMapper.readValue(actualMessage, BbsPostDto.class);

            processBbsPost(bbsPostDto);

        }catch (Exception e){
            log.error("SQS 메시지 처리 중 오류 발생", e);
            throw new RuntimeException(e);
        }
    }

    private void processBbsPost(BbsPostDto bbsPostDto){
        String result = bbsService.createPost(bbsPostDto);
        log.info("게시글 처리 결과: {}", result);
    }
}