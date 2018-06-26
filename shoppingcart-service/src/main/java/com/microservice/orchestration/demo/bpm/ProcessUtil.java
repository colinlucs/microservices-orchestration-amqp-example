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
package com.microservice.orchestration.demo.bpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import com.microservice.orchestration.demo.entity.BusinessEntity;
import com.microservice.orchestration.demo.entity.ServiceRequest;
import com.microservice.orchestration.demo.entity.ServiceResponse;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
public class ProcessUtil {
	private static final String SC_ERROR = "SC_ERROR";

	private ProcessUtil() {
	}

	public static ServiceRequest buildServiceRequest(BusinessEntity shoppingCart, String serviceName,
			String serviceAction) {
		ServiceRequest sr = new ServiceRequest().withId(shoppingCart.getId()).withCreatedBy(serviceName)
				.withCreatedDate(new Date()).withServiceName(serviceName).withServiceAction(serviceAction);
		String entityType = getEntityTypeForService(serviceName);
		if (ProcessConstants.ENTITY_TYPE_SHOPPINGCART.equals(entityType)) {
			List<BusinessEntity> items = new ArrayList<>();
			items.add(shoppingCart);
			sr.setItems(items);
		} else {
			List<BusinessEntity> items = shoppingCart.getRelatedEntities().stream()
					.filter(e -> entityType.equals(e.getEntityType())).collect(Collectors.toList());
			sr.setItems(items);
		}
		return sr;
	}

	public static void processResponse(DelegateExecution ctx, ServiceResponse serviceResponse) throws Exception {
		ctx.setVariable(ProcessConstants.VAR_RESPONSE, serviceResponse);
		if (!Response.Status.OK.toString().equals(serviceResponse.getStatusCode())) {
			ProcessContext pctx = (ProcessContext) ctx.getVariable(ProcessConstants.VAR_CTX);
			pctx.setError(serviceResponse.getErrorMessage());
			throw new BpmnError(SC_ERROR);
		}
	}

	private static String getEntityTypeForService(String serviceName) {
		if (ProcessConstants.SERVICE_NAME_LOCATION.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_LOCATION;
		} else if (ProcessConstants.SERVICE_NAME_PAYMENT.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_PAYMENT;
		} else if (ProcessConstants.SERVICE_NAME_INVENTORY.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_PRODUCT;
		} else if (ProcessConstants.SERVICE_NAME_ORDER.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_PRODUCT;
		} else if (ProcessConstants.SERVICE_NAME_CUSTOMER.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_SHOPPINGCART;
		} else {
			return ProcessConstants.UNKNOWN;
		}
	}

}
