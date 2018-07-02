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
package com.microservice.orchestration.demo.dataaccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.orchestration.demo.bpm.ProcessConstants;
import com.microservice.orchestration.demo.entity.BusinessEntity;
import com.microservice.orchestration.demo.entity.ErrorMessage;
import com.microservice.orchestration.demo.entity.ServiceResponse;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
@Component
public class ShoppingCartManager {
	@Autowired
	BusinessEntityRepository repository;

	public ServiceResponse getShoppingCart(String id) {
		BusinessEntity sc = BusinessEntityTranslator
				.fromJpa(repository.findById(id).orElseGet(() -> generateShoppingCart(id)));
		return validateShoppingCart(sc);
	}

	public ServiceResponse validateShoppingCart(BusinessEntity shoppingCart) {
		boolean isInvalidSC = false;
		if (ProcessConstants.SC_STATUS_CLOSED.equalsIgnoreCase(shoppingCart.getStatus())) {
			isInvalidSC = true;
		}
		return generateResponse(shoppingCart, isInvalidSC);
	}

	public ServiceResponse updateShoppingCart(BusinessEntity shoppingCart) {
		repository.save(BusinessEntityTranslator.toJpa(shoppingCart));
		return generateResponse(shoppingCart, false);
	}

	public ServiceResponse closeShoppingCart(BusinessEntity shoppingCart) {
		boolean isInvalidSC = "invalid-ShoppingCart".equalsIgnoreCase(shoppingCart.getId());
		if (!isInvalidSC) {
			shoppingCart.setStatus(ProcessConstants.SC_STATUS_CLOSED);
			repository.save(BusinessEntityTranslator.toJpa(shoppingCart));
		}
		return generateResponse(shoppingCart, isInvalidSC);
	}

	private ServiceResponse generateResponse(BusinessEntity sc, boolean isInvalid) {
		List<BusinessEntity> items = new ArrayList<>();
		items.add(sc);
		if (isInvalid) {
			return new ServiceResponse().withCreatedBy("DataAccessService").withCreatedDate(new Date())
					.withStatusCode(Response.Status.FORBIDDEN.toString()).withRelatedRequest(sc.getId())
					.withErrorMessage(new ErrorMessage().withCode("ERR_INVALID_SHOPPINGCART")
							.withMessage("Invalid Shopping Cart.")
							.withDetails("Shopping cart is invalid. Please contact the system administrator."))
					.withItems(items);
		}
		return new ServiceResponse().withId(UUID.randomUUID().toString()).withCreatedBy("DataAccessService")
				.withCreatedDate(new Date()).withStatusCode(Response.Status.OK.toString())
				.withRelatedRequest(sc.getId()).withItems(items);
	}

	private BusinessEntityJpa generateShoppingCart(String id) {
		BusinessEntityJpa sc = new BusinessEntityJpa().withId(id).withName("MySchoppingCart_" + id)
				.withStatus(ProcessConstants.SC_STATUS_OPEN).withEntityType(ProcessConstants.ENTITY_TYPE_SHOPPINGCART)
				.withEntitySpecification("consumerSC");
		BusinessEntityJpa addr = new BusinessEntityJpa().withId(UUID.randomUUID().toString())
				.withName("MyShippingAddress_" + id).withEntityType(ProcessConstants.ENTITY_TYPE_LOCATION)
				.withEntitySpecification("shippingAddr");
		BusinessEntityJpa payment = new BusinessEntityJpa().withId(UUID.randomUUID().toString())
				.withName("MyPayment_" + id).withEntityType(ProcessConstants.ENTITY_TYPE_PAYMENT)
				.withEntitySpecification("creditCartPayment");
		BusinessEntityJpa product = new BusinessEntityJpa().withId(UUID.randomUUID().toString())
				.withName("MyProduct_" + id).withEntityType(ProcessConstants.ENTITY_TYPE_PRODUCT)
				.withEntitySpecification("iphoneX_Gold_128G");
		BusinessEntityJpa product2 = new BusinessEntityJpa().withId(UUID.randomUUID().toString())
				.withName("MyProduct_" + id).withEntityType(ProcessConstants.ENTITY_TYPE_PRODUCT)
				.withEntitySpecification("iphoneX_Case");
		sc.getRelatedEntities().add(addr);
		sc.getRelatedEntities().add(payment);
		sc.getRelatedEntities().add(product);
		sc.getRelatedEntities().add(product2);
		repository.save(sc);
		repository.save(addr);
		repository.save(payment);
		repository.save(product);
		repository.save(product2);
		return sc;
	}

}
