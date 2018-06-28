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

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
public final class ProcessConstants {

	public static final String PROCESS_KEY_SUBMIT_SC = "submitShoppingCart";

	public static final String VAR_CTX = "context";
	public static final String VAR_SC_ID = "shoppingCartId";
	public static final String VAR_ADDRESS = "address";
	public static final String VAR_PAYMENT = "payment";
	public static final String VAR_SC = "shoppingCart";
	public static final String VAR_PRODUCT = "product";
	public static final String VAR_RESPONSE = "response";
	public static final String VAR_PAYMENT_RESERVED = "paymentReserved";
	public static final String VAR_INVENTORY_ALLOCATED = "inventoryAllocated";
	public static final String VAR_ORDER_PLACED = "orderPlaced";

	public static final String SERVICE_NAME_LOCATION = "LocationService";
	public static final String SERVICE_NAME_PAYMENT = "PaymentService";
	public static final String SERVICE_NAME_INVENTORY = "InventoryService";
	public static final String SERVICE_NAME_ORDER = "OrderService";
	public static final String SERVICE_NAME_CUSTOMER = "CustomerService";
	public static final String SERVICE_NAME_BACKOFFICE = "BackofficeService";
	public static final String SERVICE_NAME_SHOPPINGCART = "ShoppingCartService";

	public static final String ENTITY_TYPE_SHOPPINGCART = "SHOPPINGCART";
	public static final String ENTITY_TYPE_LOCATION = "LOCATION";
	public static final String ENTITY_TYPE_PAYMENT = "PAYMENT";
	public static final String ENTITY_TYPE_PRODUCT = "PRODUCT";
	public static final String ENTITY_TYPE_ERROR = "ERROR";

	public static final String SC_STATUS_OPEN = "OPEN";
	public static final String SC_STATUS_CLOSED = "CLOSED";

	public static final String UNKNOWN = "UNKNOWN";

	private ProcessConstants() {
	}
}
