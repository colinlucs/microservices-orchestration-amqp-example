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
package com.microservice.orchestration.demo.stub;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.microservice.orchestration.demo.adapter.amqp.consumer.EventHandler;
import com.microservice.orchestration.demo.entity.ErrorMessage;
import com.microservice.orchestration.demo.entity.ServiceRequest;
import com.microservice.orchestration.demo.entity.ServiceResponse;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
@Service
public class StubEventHandler implements EventHandler {
	private static final String INVALID_REQ = "invalid";
	private static final String ROLLBACK_ACTION = "release";
	private static final Logger LOG = LoggerFactory.getLogger(StubEventHandler.class);

	@Override
	public ServiceResponse processRequest(ServiceRequest request) {
		String serviceName = request.getServiceName();
		String serviceAction = request.getServiceAction();
		String requestId = request.getId();
		ServiceResponse response = generateResponse(request,
				requestId.equals(INVALID_REQ + "-" + serviceName) && (!ROLLBACK_ACTION.equals(serviceAction)));
		LOG.trace("RPC response {}", response);
		return response;
	}

	@Override
	public void processEvent(ServiceRequest request) {
		LOG.trace("Event is consumed by subscriber.");
	}

	private ServiceResponse generateResponse(ServiceRequest request, boolean isInvalid) {
		if (isInvalid) {
			return new ServiceResponse().withCreatedBy(request.getServiceName()).withCreatedDate(new Date())
					.withStatusCode(Response.Status.FORBIDDEN.toString()).withRelatedRequest(request.getId())
					.withErrorMessage(buildErrorMessage(request.getServiceName()));
		}
		return new ServiceResponse().withId(UUID.randomUUID().toString()).withCreatedBy(request.getServiceName())
				.withCreatedDate(new Date()).withStatusCode(Response.Status.OK.toString())
				.withRelatedRequest(request.getId());
	}

	private ErrorMessage buildErrorMessage(String serviceName) {
		if ("LocationService".equals(serviceName)) {
			return new ErrorMessage().withCode("ERR_INVALID_ADDRESS").withMessage("Invalid address.")
					.withDetails("Invalid address.");
		} else if ("PaymentService".equals(serviceName)) {
			return new ErrorMessage().withCode("ERR_PAYMENT_FAILURE").withMessage("Failed to collect payment.")
					.withDetails("Failed to collect payment.");
		} else if ("InventoryService".equals(serviceName)) {
			return new ErrorMessage().withCode("ERR_OUT_OF_STOCK").withMessage("Product out of stock.")
					.withDetails("Product out of stock.");
		} else if ("OrderService".equals(serviceName)) {
			return new ErrorMessage().withCode("ERR_ORDER_FAILURE").withMessage("Unable to process order.")
					.withDetails("Unable to process order.");
		} else {
			return new ErrorMessage().withCode("ERR_BAD_REQUEST").withMessage("Invalid service request.")
					.withDetails("Invalid service request.");
		}
	}

}
