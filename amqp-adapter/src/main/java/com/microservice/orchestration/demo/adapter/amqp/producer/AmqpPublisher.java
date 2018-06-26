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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.microservice.orchestration.demo.entity.ServiceRequest;
import com.microservice.orchestration.demo.entity.ServiceResponse;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
public class AmqpPublisher {
	private static final Logger LOG = LoggerFactory.getLogger(AmqpPublisher.class);

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private TopicExchange pubExchange;

	public void publishEvent(ServiceRequest request) {
		LOG.trace("Exchange: {} ", pubExchange);
		LOG.trace("Service Request: {}", request);
		String routingKey = request.getServiceName() + "." + request.getServiceAction();
		template.convertAndSend(pubExchange.getName(), routingKey, request);
	}

	public void publishError(ServiceResponse errorResponse, String routingKey) {
		LOG.trace("Exchange: {}", pubExchange);
		LOG.trace("Service Error: {}", errorResponse);
		template.convertAndSend(pubExchange.getName(), routingKey, errorResponse);
	}
}
