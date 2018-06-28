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

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.orchestration.demo.dataaccess.ShoppingCartManager;
import com.microservice.orchestration.demo.entity.BusinessEntity;
import com.microservice.orchestration.demo.entity.ServiceResponse;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
@Component
public class CloseShoppingCartActivity implements JavaDelegate {
	private static final Logger LOG = LoggerFactory.getLogger(CloseShoppingCartActivity.class);
	public static final String SERVICE_ACTION = "close";

	@Autowired
	ShoppingCartManager shoppingCartManager;

	@Override
	public void execute(DelegateExecution ctx) throws Exception {
		LOG.info("execute {} - {}", ProcessConstants.SERVICE_NAME_SHOPPINGCART, SERVICE_ACTION);
		BusinessEntity sc = (BusinessEntity) ctx.getVariable(ProcessConstants.VAR_SC);
		ServiceResponse response = shoppingCartManager.closeShoppingCart(sc);
		ProcessUtil.processResponse(ctx, response);
	}
}
