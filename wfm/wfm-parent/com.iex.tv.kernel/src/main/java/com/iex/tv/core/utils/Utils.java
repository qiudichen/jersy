package com.iex.tv.core.utils;

import java.util.Collection;
import java.util.Map;

public class Utils {
	public static boolean isEmpty(Collection<?> cols) {
		return (cols == null || cols.isEmpty());
	}

	public static boolean isEmpty(Map<?, ?> cols) {
		return (cols == null || cols.isEmpty());
	}
	
	public static boolean isEmpty(String...values) {
		return (values == null || values.length == 0);
	}
	
	public static boolean isBlank(String name) {
		return (name == null || name.isEmpty());
	}
}
