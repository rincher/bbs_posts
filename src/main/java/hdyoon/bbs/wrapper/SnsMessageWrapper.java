package hdyoon.bbs.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SnsMessageWrapper {
    @JsonProperty("Type")
    private String type;

    @JsonProperty("MessageId")
    private String messageId;

    @JsonProperty("TopicArn")
    private String topicArn;

    @JsonProperty("Subject")
    private String subject;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("MessageAttributes")
    private Map<String, SnsMessageAttribute> messageAttributes;
}
