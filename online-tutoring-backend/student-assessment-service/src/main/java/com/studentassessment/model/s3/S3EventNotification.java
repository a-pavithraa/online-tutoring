package com.studentassessment.model.s3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class S3EventNotification {

    @JsonProperty("Records")
    public List<NotificationRecord> records;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bucket{
        private String name;
        private OwnerIdentity ownerIdentity;
        private String arn;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class S3Object {
        private String key;
        private int size;
        private String eTag;
        private String sequencer;

        public String getKey() {
            String result = null;
            try {
                result = java.net.URLDecoder.decode(key, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            return result;
        }
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OwnerIdentity{
        private String principalId;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NotificationRecord {
        private String eventVersion;
        private String eventSource;
        private String awsRegion;
        private Date eventTime;
        private String eventName;
        private UserIdentity userIdentity;
        private RequestParameters requestParameters;
        private ResponseElements responseElements;
        private S3 s3;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequestParameters{
        private String sourceIPAddress;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseElements{
        @JsonProperty("x-amz-request-id")
        private String xAmzRequestId;
        @JsonProperty("x-amz-id-2")
        private String xAmzId2;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class S3{
        private String s3SchemaVersion;
        private String configurationId;
        private Bucket bucket;
        private S3Object object;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserIdentity{
        private String principalId;
    }


}
