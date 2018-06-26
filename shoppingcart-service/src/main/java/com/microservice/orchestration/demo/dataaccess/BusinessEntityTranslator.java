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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.microservice.orchestration.demo.entity.BusinessEntity;

/**
 * @author <a href="mailto:colinlucs@gmail.com">Colin Lu</a>
 */
public class BusinessEntityTranslator {
	private BusinessEntityTranslator() {
	}

	public static BusinessEntity fromJpa(BusinessEntityJpa jpaEntity) {
		if (jpaEntity == null) {
			return null;
		}

		return new BusinessEntity().withId(jpaEntity.getId()).withName(jpaEntity.getName())
				.withStatus(jpaEntity.getStatus()).withEntitySpecification(jpaEntity.getEntitySpecification())
				.withEntityType(jpaEntity.getEntityType()).withRelatedEntities(
						translateToList(jpaEntity.getRelatedEntities(), BusinessEntityTranslator::fromJpa));
	}

	public static BusinessEntityJpa toJpa(BusinessEntity entity) {
		if (entity == null) {
			return null;
		}

		return new BusinessEntityJpa().withId(entity.getId()).withName(entity.getName()).withStatus(entity.getStatus())
				.withEntitySpecification(entity.getEntitySpecification()).withEntityType(entity.getEntityType())
				.withRelatedEntities(translateToList(entity.getRelatedEntities(), BusinessEntityTranslator::toJpa));
	}

	public static <F, T> List<T> translateToList(Collection<F> froms, Function<F, T> translate) {
		return Optional.ofNullable(froms).orElse(Collections.emptyList()).stream().map(translate)
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

}
