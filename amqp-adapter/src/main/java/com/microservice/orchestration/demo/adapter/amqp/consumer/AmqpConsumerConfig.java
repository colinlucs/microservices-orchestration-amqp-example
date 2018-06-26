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
package com.microservice.orchestration.demo.adapter.amqp.consumer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
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
@Profile({ "amqp-consumer" })
@Configuration
public class AmqpConsumerConfig {
	@Value("#{'${spring.rabbitmq.exchange.rpc}'.split(',')}")
	private List<String> exchangesRpc;

	@Value("#{'${spring.rabbitmq.exchange.pub}'.split(',')}")
	public List<String> exchangesPub;

	@Value("${spring.rabbitmq.queueName}")
	private String queueName;

	@Value("${spring.rabbitmq.routingKey}")
	private String routingKey;

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Profile("server")
	private class ServerConfig {

		@Bean
		public Queue serverQueue() {
			return new Queue(queueName);
		}

		@Bean
		public List<Binding> bindings() {
			return exchanges().stream().map(e -> BindingBuilder.bind(serverQueue()).to(e).with(routingKey))
					.collect(Collectors.toList());
		}

		@Bean
		public List<TopicExchange> exchanges() {
			return exchangesRpc.stream().map(TopicExchange::new).collect(Collectors.toList());
		}

		@Bean
		public AmqpRpcServer server() {
			return new AmqpRpcServer();
		}

	}

	@Profile("subscriber")
	private class SubscriberConfig {

		@Bean
		public Queue subscriberQueue() {
			return new Queue(queueName);
		}

		@Bean
		public List<Binding> bindings() {
			return exchanges().stream().map(e -> BindingBuilder.bind(subscriberQueue()).to(e).with(routingKey))
					.collect(Collectors.toList());
		}

		@Bean
		public List<TopicExchange> exchanges() {
			return exchangesPub.stream().map(TopicExchange::new).collect(Collectors.toList());
		}

		@Bean
		public AmqpSubscriber subscriber() {
			return new AmqpSubscriber();
		}

	}

}
