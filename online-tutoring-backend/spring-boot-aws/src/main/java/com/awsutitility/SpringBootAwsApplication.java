package com.awsutitility;


import com.awsutitility.util.S3EventNotification;
import io.awspring.cloud.sqs.support.converter.SqsHeaderMapper;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.apache.catalina.mapper.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;


import java.util.Collections;

@SpringBootApplication
public class SpringBootAwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAwsApplication.class, args);
	}
	/*@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory() {
		QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
		MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();

		//set strict content type match to false
		messageConverter.setStrictContentTypeMatch(false);
		factory.setArgumentResolvers(Collections.<HandlerMethodArgumentResolver>singletonList(new PayloadArgumentResolver(messageConverter)));
		return factory;
	}*/
	@Bean
	public SqsMessagingMessageConverter getSQSMessagingConverter(){
		SqsMessagingMessageConverter messageConverter = new SqsMessagingMessageConverter();
		messageConverter.setPayloadTypeMapper(message -> {
			String eventTypeHeader = message.getHeaders().get("myEventTypeHeader", String.class);
			return S3EventNotification.class;
		});
		return messageConverter;
	}

}
