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

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqConfig {
	private static final String DEFAULT_EVENT_QUEUE = "DefaultEventQueue";
	private static final String DEFAULT_SERVICE_QUEUE = "DefaultServiceQueue";
	private List<String> serviceRoutings = new ArrayList<>();
	private List<String> eventRoutings = new ArrayList<>();

	public List<String> getServiceRoutings() {
		return this.serviceRoutings;
	}

	public List<String> getEventRoutings() {
		return this.eventRoutings;
	}

	public String getServiceQueueName() {
		return serviceRoutings.stream().findFirst().map(this::getQueue).orElse(DEFAULT_SERVICE_QUEUE);
	}

	public String getEventQueueName() {
		return eventRoutings.stream().findFirst().map(this::getQueue).orElse(DEFAULT_EVENT_QUEUE);
	}

	public String getExchangeName(String routing) {
		return routing.split("->")[0];
	}

	public String getRoutingKey(String routing) {
		return routing.split("->")[1];
	}

	public String getQueue(String routing) {
		return routing.split("->")[2];
	}
}
