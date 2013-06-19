package com.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class Config {
	private static PropertiesConfiguration configProperties = new PropertiesConfiguration();
	private static String webroot = System.getProperty("user.dir");
	private static Map<String, DataSource> dataSources = new HashMap<String, DataSource>();

	private static BoneCPConfig boneCpConfig;

	// static {
	// init();
	// }

	public static void init() {
		try {

			FileInputStream bonecpStream = new FileInputStream(webroot + "/res/bonecp-config.xml");// 连接池配置
			FileInputStream log4jStream = new FileInputStream(webroot + "/res/log4j.properties");
			Properties props = new Properties();
			props.load(log4jStream);
			log4jStream.close();
			PropertyConfigurator.configure(props); // 装入log4j配置信息

			configProperties.setFile(new File(webroot + "/res/config.properties"));
			configProperties.load();
			
			loadDataSources(bonecpStream);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unabled to init config.properties...");
		}
	}

	/**
	 * 
	 * @Title: loadDataSources
	 * @Description: 加载数据源
	 * @param bonecpStream
	 *            void
	 */
	public static void loadDataSources(FileInputStream bonecpStream) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		// 根据bonecp-config.xml配置连接池
		for (int i = 1; i < 5; i++) {
			try {
				String db_name=configProperties.getString("db" + i + ".name");
				if (db_name == null)
					continue;
				BoneCPConfig config = new BoneCPConfig(bonecpStream, db_name);
				BoneCPDataSource dataSource = new BoneCPDataSource(config);
				dataSources.put(db_name, dataSource);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 直接设置属性
		// for (int i = 1; i < 5; i++) {
		// String db_name = configProperties.getString("db" + i + ".name");
		// String db_user = configProperties.getString("db" + i + ".jdbc.user");
		// String db_password = configProperties.getString("db" + i +
		// ".jdbc.password");
		// String db_jdbc_url = configProperties.getString("db" + i +
		// ".jdbc.jdbcUrl");
		// BoneCPDataSource dataSource = new BoneCPDataSource();
		// dataSource.setUsername(db_user);
		// dataSource.setPassword(db_password);
		// dataSource.setJdbcUrl(db_jdbc_url);
		// dataSource.setMaxConnectionsPerPartition(10);// 设置每个分区中的最大连接数
		// dataSource.setMinConnectionsPerPartition(5);// 设置每个分区中的最小连接数
		// dataSource.setIdleConnectionTestPeriodInMinutes(1);//
		// 设置每一分钟检查数据库中的空闲连接数
		// dataSource.setIdleMaxAgeInMinutes(4);// 设置连接空闲时间 4分钟
		// dataSource.setAcquireIncrement(5);// 当连接池中的连接耗尽的时候 BoneCP一次同时获取的连接数
		// dataSource.setReleaseHelperThreads(3);// 连接释放处理
		// dataSource.setPartitionCount(1);// 设置分区 分区数为1
		// dataSource.setLogStatementsEnabled(true);// 开启sql打印
		//
		// dataSources.put(db_name, dataSource);
		// }
	}

	public static DataSource getDataSources(String db_name) {
		return dataSources.get(db_name);
	}

	/**
	 * 
	 * @Title: closeDataSource
	 * @Description: 关闭数据源 void
	 */
	public static void closeDataSource() {
		for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
			BoneCPDataSource ds = (BoneCPDataSource) entry.getValue();
			ds.close();
		}
	}
}
