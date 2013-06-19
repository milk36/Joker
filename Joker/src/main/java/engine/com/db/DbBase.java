package com.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.config.Config;

public class DbBase {
	private QueryRunner queryRunner;

	private static Map<Class, BeanHandler> beanHandlerMap = new HashMap<Class, BeanHandler>();
	private static Map<Class, BeanListHandler> beanListHandlerMap = new HashMap<Class, BeanListHandler>();

	private String db_name;

	public DbBase(String dbName) {
		this.db_name = dbName;
		DataSource ds = Config.getDataSources(db_name);
		if (ds == null)
			throw new RuntimeException("dataSource is not exist fo db_name:" + db_name);
		queryRunner = new QueryRunner(ds);
	}

	private final static BeanListHandler getBeanListHandler(Class beanClass) {
		BeanListHandler listHandler = beanListHandlerMap.get(beanClass);
		if (listHandler == null) {
			listHandler = new BeanListHandler(beanClass);
			beanListHandlerMap.put(beanClass, listHandler);
		}
		return listHandler;
	}

	private final static BeanHandler getBeanHandler(Class beanClass) {
		BeanHandler bh = beanHandlerMap.get(beanClass);
		if (bh == null) {
			bh = new BeanHandler(beanClass);
			beanHandlerMap.put(beanClass, bh);
		}
		return bh;
	}

	public <T extends Pojo> List<T> query(Class<T> beanClass, String sql, Object... params) throws SQLException {
		List<T> result = queryRunner.query(/* getConnection(), */sql, getBeanListHandler(beanClass), params);
		return result;
	}

	/**
	 * 更新对象
	 * 
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	public int update(Pojo obj, String update_str, String where_Str, Object where_var) throws SQLException {
		Map<String, Object> pojo_bean = obj.listInsertableFields();
		String[] fields = pojo_bean.keySet().toArray(new String[pojo_bean.size()]);
		Object[] values = new Object[fields.length + 1];
		StringBuilder sql = new StringBuilder();
		sql.append(update_str).append(" set ");
		for (int i = 0; i < fields.length; i++) {
			if (i > 0)
				sql.append(',');
			sql.append(fields[i]).append(" = ? ");
			values[i] = pojo_bean.get(fields[i]);
		}
		values[values.length - 1] = where_var;
		sql.append(" ").append(where_Str);
		int rs = queryRunner.update(/* getConnection(), */sql.toString(), values);
		return rs;
	}

}
