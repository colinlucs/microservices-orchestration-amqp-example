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
package com.microservice.orchestration.demo.rest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.orchestration.demo.bpm.ProcessConstants;
import com.microservice.orchestration.demo.bpm.ProcessContext;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartRestController {

	@Autowired
	private ProcessEngine camunda;

	@RequestMapping(value = "/{scId}/submit", method = RequestMethod.POST)
	public ResponseEntity<?> placeOrderPOST(@PathVariable("scId") String scId) {
		ProcessContext context = new ProcessContext();
		submitShoppingCart(scId, context);
		if (context.getError() != null) {
			return new ResponseEntity<>(context.getError(), HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>(context.getResponse(), HttpStatus.OK);

	}

	private ProcessInstance submitShoppingCart(String scId, ProcessContext context) {
		return camunda.getRuntimeService().startProcessInstanceByKey(//
				"submitShoppingCart", //
				Variables //
						.putValue(ProcessConstants.VAR_SC_ID, scId).putValue(ProcessConstants.VAR_CTX, context));
	}
}
