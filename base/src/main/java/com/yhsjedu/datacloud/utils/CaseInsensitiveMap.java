package com.yhsjedu.datacloud.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 当Key值类型为String类型时,键值不分大小写的Map类<br>
 * 
 * @param <K>
 * @param <V>
 */
public class CaseInsensitiveMap<K, V> extends HashMap<K, V> {
	
	private static final long serialVersionUID = 1L;
	private Map<String, String> keyMap = new HashMap<String, String>();

	/**
	 * 构造方法
	 */
	public CaseInsensitiveMap() {
	}

	/**
	 * 构造方法
	 */
	public CaseInsensitiveMap(int length) {
		super(length);
	}

	/**
	 * 构造方法
	 * 
	 * @param map
	 */
	public CaseInsensitiveMap(Map<K, V> map) {
		super(map.size());
		for (Entry<K, V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 根据给定的Key查找Map中存在的Key值
	 * 
	 * @param key
	 * @return
	 */
	private Object findRealKey(Object key) {
		if (key != null && key instanceof String) {
			String realKey = keyMap.get(key.toString().toLowerCase());
			if (realKey != null)
				return realKey;
		}
		return key;
	}

	/**
	 * 获取Key值所映射的Value值<br>
	 * 当Key为String类型时不分大小写
	 * 
	 * @see java.util.HashMap#get(Object)
	 */
	@Override
	public V get(Object key) {
		return super.get(findRealKey(key));
	}

	/**
	 * 设置Key键所映射的Value值<br>
	 * 当Key为String类型且存在相等(不分大小)的Key值时,使用原来的Key值
	 * 
	 * @see java.util.HashMap#put(Object, Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V put(K key, V value) {
		if (key != null && key instanceof String) {
			String oldKey = keyMap.put(key.toString().toLowerCase(), key
					.toString());
			if (oldKey != null) {
				super.remove(oldKey);
			}
		}
		return super.put((K) findRealKey(key), value);
	}

	/**
	 * 判断是否包含指定的Key值<br>
	 * 当Key为String类型时不分大小写
	 * 
	 * @see java.util.HashMap#containsKey(Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(findRealKey(key));
	}

	/**
	 * 删除Key值映射的数据<br>
	 * 当Key为String类型时不分大小写
	 * 
	 * @see java.util.HashMap#remove(Object)
	 */
	@Override
	public V remove(Object key) {
		if (key != null && key instanceof String) {
			keyMap.remove(key.toString().toLowerCase());
		}
		return super.remove(this.findRealKey(key));
	}

}
