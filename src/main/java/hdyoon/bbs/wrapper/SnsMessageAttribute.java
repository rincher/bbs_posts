package hdyoon.bbs.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class SnsMessageAttribute {
        @JsonProperty("Type")
        private String type;

        @JsonProperty("Value")
        private String value;
}
