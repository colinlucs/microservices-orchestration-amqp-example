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

import java.io.Serializable;

import com.microservice.orchestration.demo.entity.BusinessEntity;
import com.microservice.orchestration.demo.entity.ErrorMessage;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
public class ProcessContext implements Serializable {
	private static final long serialVersionUID = 1L;
	private BusinessEntity response;

	public BusinessEntity getResponse() {
		return response;
	}

	public void setResponse(BusinessEntity response) {
		this.response = response;
	}

	public ErrorMessage getError() {
		return error;
	}

	public void setError(ErrorMessage error) {
		this.error = error;
	}

	private ErrorMessage error;
}
