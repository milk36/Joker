package com.db;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public abstract class Pojo implements Serializable {

	public Map<String, Object> listInsertableFields() {
		try {
			Map<String, Object> props = BeanUtils.describe(this);
			props.remove("id");
			props.remove("class");
			return props;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException("Exception when Fetching fields of " + this);
		} catch (Exception e) {
			throw new RuntimeException("Exception when Fetching fields of " + this);
		}
	}
}
