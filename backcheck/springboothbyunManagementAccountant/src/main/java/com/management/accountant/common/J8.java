package com.management.accountant.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Java 8 兼容的集合快捷构造工具，语义对应 Java 9+ 的 Map.of / List.of / Set.of。
 * 差异：返回可变集合且允许 null 值（原 Map.of 遇 null 会抛异常，此处放宽）。
 */
public final class J8 {

	private J8() {
	}

	/** 键值对形式构造 Map，参数个数必须为偶数 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> mapOf(Object... kv) {
		Map<K, V> map = new HashMap<K, V>();
		if (kv != null) {
			for (int i = 0; i + 1 < kv.length; i += 2) {
				map.put((K) kv[i], (V) kv[i + 1]);
			}
		}
		return map;
	}

	@SafeVarargs
	public static <T> List<T> listOf(T... items) {
		List<T> list = new ArrayList<T>();
		if (items != null) {
			for (T item : items) {
				list.add(item);
			}
		}
		return list;
	}

	@SafeVarargs
	public static <T> Set<T> setOf(T... items) {
		Set<T> set = new HashSet<T>();
		if (items != null) {
			for (T item : items) {
				set.add(item);
			}
		}
		return set;
	}
}
