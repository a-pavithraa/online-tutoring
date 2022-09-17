package com.awsutitility;

import com.awsutitility.util.S3EventNotification;
import io.awspring.cloud.sqs.support.converter.MessagingMessageConverter;
import io.awspring.cloud.sqs.support.converter.SqsHeaderMapper;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.Assert;

import java.util.function.Function;

@Configuration
public class PayloadMapperImpl extends SqsMessagingMessageConverter {



    public void setPayloadTypeMapper (Function<Message<?>, Class<?>> payloadTypeMapper) {
        SqsMessagingMessageConverter messageConverter = new SqsMessagingMessageConverter();
        System.out.println("Inside Payload message converter");
// Configure Type Header
        messageConverter.setPayloadTypeHeader("myTypeHeader");

// Configure Header Mapper
        SqsHeaderMapper headerMapper = new SqsHeaderMapper();
        headerMapper.setAdditionalHeadersFunction(((sqsMessage, accessor) -> {
            accessor.setHeader("myCustomHeader", "myValue");
            return accessor.toMessageHeaders();
        }));
        messageConverter.setHeaderMapper(headerMapper);

// Configure Payload Converter
        MappingJackson2MessageConverter payloadConverter = new MappingJackson2MessageConverter();
        payloadConverter.setPrettyPrint(true);
        messageConverter.setPayloadMessageConverter(payloadConverter);
        messageConverter.setPayloadTypeMapper(message -> {
            System.out.println("message=="+message);
            String eventTypeHeader = message.getHeaders().get("myEventTypeHeader", String.class);
            return S3EventNotification.class;
        });
    }}
