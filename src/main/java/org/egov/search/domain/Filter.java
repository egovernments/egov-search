package org.egov.search.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public abstract class Filter {
	protected String fieldName;

	protected Filter(String fieldName) {
		this.fieldName = fieldName;
	}

	public String field() {
		return fieldName;
	}

	public QueryBuilder queryBuilder() {
		return QueryBuilders.matchAllQuery();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public static Filter queryStringFilter(String fieldName, String value) {
		if (StringUtils.isEmpty(value) || StringUtils.isEmpty(fieldName)) {
			return new NoOpFilter();
		}

		return new QueryStringFilter(fieldName, value);
	}

	public static Filter rangeFilter(String fieldName, String from, String to) {
		if ((StringUtils.isEmpty(from) && StringUtils.isEmpty(to))
				|| StringUtils.isEmpty(fieldName)) {
			return new NoOpFilter();
		}
		return new RangeFilter(fieldName, from, to);
	}

	public static Filter rangeFilterFrom(String fieldName, String from) {
		if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(from)) {
			return new NoOpFilter();
		}
		return rangeFilter(fieldName, from, null);
	}

	public static Filter rangeFilterTo(String fieldName, String to) {
		if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(to)) {
			return new NoOpFilter();
		}
		return rangeFilter(fieldName, null, to);
	}

}
