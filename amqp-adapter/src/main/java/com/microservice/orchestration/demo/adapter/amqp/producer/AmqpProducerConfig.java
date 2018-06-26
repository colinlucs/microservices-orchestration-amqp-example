/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microservice.orchestration.demo.adapter.amqp.producer;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
@Profile({ "amqp-producer" })
@Configuration
public class AmqpProducerConfig {
	@Value("${spring.rabbitmq.exchange.rpc}")
	private String exchangeRpc;

	@Value("${spring.rabbitmq.exchange.pub}")
	public String exchangePub;

	@Bean
	public TopicExchange rpcExchange() {
		return new TopicExchange(exchangeRpc);
	}

	@Bean
	public AmqpRpcClient rpcClient() {
		return new AmqpRpcClient();
	}

	@Bean
	public TopicExchange pubExchange() {
		return new TopicExchange(exchangePub);
	}

	@Bean
	public AmqpPublisher pubClient() {
		return new AmqpPublisher();

	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}
