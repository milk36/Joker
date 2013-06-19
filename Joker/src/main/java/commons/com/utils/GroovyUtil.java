package com.utils;

import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class GroovyUtil {
	/**
	 * 脚本对象缓存
	 */
	private static Map<String, Object> scriptCache = new ConcurrentHashMap<String, Object>();

	private static GroovyScriptEngine scriptEngine;
	static {
		try {
			scriptEngine = new GroovyScriptEngine(System.getProperty("user.dir") + "/data/groovy");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Object> getScriptCache() {
		return scriptCache;
	}

	public static void clearScriptCache() {
		scriptCache.clear();
	}

	public static void clearOneKeyScriptCache(String key) {
		scriptCache.remove(key);
	}
	
	/**
	 * 
	 * @Title: reloadScript
	 * @Description: 重新加载脚本
	 */
	public static void reloadScript() {
		clearScriptCache();
	}

	/**
	 * 
	 * @Title: invokeMethod
	 * @Description: 调用脚本方法
	 * @param scriptName
	 *            脚本名(省略.groovy后缀)
	 * @param methodName
	 *            调用的方法名
	 * @param flag
	 *            是否采取缓存策略
	 * 
	 * @param args
	 *            参数
	 * @return Object 返回值
	 */
	public static Object invokeMethod(String scriptName, String methodName,boolean flag, Object... args) {
		Script script=getScriptObject(scriptName,flag);
		return script.invokeMethod(methodName, args);
	}

	/**
	 * 
	 * @Title: getScriptObject
	 * @Description: 获取脚本对象
	 * @param scriptName
	 *            脚本名
	 * @param flag
	 *            是否采取缓存策略
	 * @return Script
	 */
	public static Script getScriptObject(String scriptName,boolean flag) {
		Script script=null;
		try {
			if(flag){
				script= (Script) getScriptCache().get(scriptName);
				if (script == null) {
					script = scriptEngine.createScript(scriptName + ".groovy", new Binding());
					getScriptCache().put(scriptName, script);
				}
			}else{
				script= scriptEngine.createScript(scriptName + ".groovy", new Binding());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return script;
	}

	/**
	 * 
	 * @Title: run
	 * @Description: 运行脚本,从run入口
	 * @param scriptName
	 *            脚本名
	 * @param argument
	 *            绑定变量
	 * @param flag
	 *            是否采取缓存策略
	 * @return String
	 */
	public static String run(String scriptName, String argument,boolean flag) {
		try {
			Binding binding = new Binding();
			binding.setVariable("arg", argument);
			Object result = run(scriptName, binding, flag);
			return result == null ? "" : result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title: run
	 * @Description: 运行脚本,从run入口
	 * @param scriptName
	 *            脚本名
	 * @param binding
	 *            绑定变量
	 * @param flag
	 *            是否采取缓存策略
	 * @return Object
	 */
	public static Object run(String scriptName, Binding binding,boolean flag) {
		try {
			Script script = getScriptObject(scriptName,flag);
			script.setBinding(binding);
			return script.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws InterruptedException {
//		Integer[] objs = new Integer[] { 1, 2 };
//		invokeMethod("hello", "additive", false, objs);
//		Binding binding = new Binding();
//		binding.setVariable("value", "milk");
//		run("hello", binding, false);
		// while (true) {
		// reloadScript();// 手动重载脚本
		// PlayerDataModifyByLua p = new PlayerDataModifyByLua();
		// invokeMethod("datas", "fillPlayerData",true, p);
		// System.out.println(p);
		// Thread.sleep(1000);
		// }
		// Object obj = invokeMethod("datas", "pdatas", null);
		// System.out.println(obj);
		// run("datas", "");
		
//		BattleConfigInfo battleConfigInfo=(BattleConfigInfo)invokeMethod("datas", "getBattleConfigData",false, BattleConfigInfo.class);
//		System.out.println(battleConfigInfo);
		// AINpcPassConfig
		// npcPassConfig=(AINpcPassConfig)invokeMethod("battleInitConfig",
		// "npcPassDetail_1_1",false);
		// Map tmpMap=npcPassConfig.getInitAIInfos();
		// Set<Map.Entry> params = tmpMap.entrySet();
		// for (Map.Entry entry : params) {
		// System.out.println(entry.getKey() + " : " + entry.getValue());
		// Map tmpMap1=(Map)entry.getValue();
		// Set<Map.Entry> params1 = tmpMap1.entrySet();
		// for (Map.Entry entry1 : params1) {
		// System.out.println(entry1.getKey() + " : " + entry1.getValue());
		// }
		// }
		// System.out.println("");
		//
		// Map playerMap=(Map)invokeMethod("battleInitConfig",
		// "mapSeats_1",false);
		// Set<Map.Entry> params2 = tmpMap.entrySet();
		// for (Map.Entry entry : params2) {
		// System.out.println(entry.getKey() + " : " + entry.getValue());
		// }
	}
}
