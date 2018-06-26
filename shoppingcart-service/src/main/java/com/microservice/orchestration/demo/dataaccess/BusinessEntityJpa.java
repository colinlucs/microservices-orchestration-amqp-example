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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
@Entity
@Table(name = "BusinessEntity")
public class BusinessEntityJpa {

	@Id
	private String id;

	@Column(nullable = false)
	private String entityType;

	@Column(nullable = false)
	private String entitySpecification;

	private String name;

	private String status;

	@JoinTable(name = "entityRelation", joinColumns = {
			@JoinColumn(name = "referencingEntity", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "referencedEntity", referencedColumnName = "id", nullable = false) })
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<BusinessEntityJpa> relatedEntities = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BusinessEntityJpa withId(String id) {
		this.id = id;
		return this;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public BusinessEntityJpa withEntityType(String entityType) {
		this.entityType = entityType;
		return this;
	}

	public String getEntitySpecification() {
		return entitySpecification;
	}

	public void setEntitySpecification(String entitySpecification) {
		this.entitySpecification = entitySpecification;
	}

	public BusinessEntityJpa withEntitySpecification(String entitySpecification) {
		this.entitySpecification = entitySpecification;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BusinessEntityJpa withName(String name) {
		this.name = name;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BusinessEntityJpa withStatus(String status) {
		this.status = status;
		return this;
	}

	public List<BusinessEntityJpa> getRelatedEntities() {
		return relatedEntities;
	}

	public void setRelatedEntities(List<BusinessEntityJpa> relatedEntities) {
		this.relatedEntities = relatedEntities;
	}

	public BusinessEntityJpa withRelatedEntities(List<BusinessEntityJpa> relatedEntities) {
		this.relatedEntities = relatedEntities;
		return this;
	}
}
