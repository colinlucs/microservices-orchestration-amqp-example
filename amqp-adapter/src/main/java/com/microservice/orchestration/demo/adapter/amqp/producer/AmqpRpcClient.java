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

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.microservice.orchestration.demo.entity.ErrorMessage;
import com.microservice.orchestration.demo.entity.ServiceRequest;
import com.microservice.orchestration.demo.entity.ServiceResponse;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
public class AmqpRpcClient {
	private static final Logger LOG = LoggerFactory.getLogger(AmqpRpcClient.class);

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private TopicExchange rpcExchange;

	public ServiceResponse invokeService(ServiceRequest request) {
		LOG.trace("Exchange: {} ", rpcExchange);
		LOG.trace("Service Request: {}", request);
		String routingKey = request.getServiceName() + "." + request.getServiceAction();
		ServiceResponse response = (ServiceResponse) template.convertSendAndReceive(rpcExchange.getName(), routingKey,
				request);
		LOG.trace("Service Response: {}", response);
		return Optional.ofNullable(response).orElse(generateTimedoutResponse(request));
	}

	private ServiceResponse generateTimedoutResponse(ServiceRequest request) {
		ServiceResponse response = new ServiceResponse().withId(UUID.randomUUID().toString()).withCreatedBy("System")
				.withCreatedDate(new Date())
				.withErrorMessage(new ErrorMessage().withCode("ERR_SERVICE_UNAVAIL").withMessage("Service Unavaialble")
						.withDetails("Internal Error: we are sorry, the " + request.getServiceName()
								+ " is not reachable. Please try again later."))
				.withStatusCode(Response.Status.SERVICE_UNAVAILABLE.toString()).withRelatedRequest(request.getId());
		LOG.trace("Service timeout response: {}", response);
		return response;
	}
}
