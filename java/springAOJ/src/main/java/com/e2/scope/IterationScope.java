package com.e2.scope;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class IterationScope implements Scope {
	private final ConcurrentMap<String, Object> cache = new ConcurrentHashMap<String, Object>();
	private final ConcurrentMap<String, List<Runnable>> destructionCallbacks = new ConcurrentHashMap<String, List<Runnable>>();
	private static final String ITERATION_START_TIME_PATTERN = "yyyyMMdd-HHmmss.SSS";
	private final AtomicReference<String> iterationStartTime = new AtomicReference<String>();

	public IterationScope() {
		updateIterationStartTime();
	}

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Object result = null;
		int counter = 0;
		while (result == null) {
			if (!cache.containsKey(name)) {
				cache.putIfAbsent(name, objectFactory.getObject());
			}
			result = cache.get(name);
			if (++counter > 10) {
				throw new IllegalStateException(
						String.format(
								"Can't retrieve scoped object with the name '%s'",
								name));
			}
		}
		return result;
	}

	@Override
	public Object remove(String name) {
		Object result = cache.remove(name);
		notifyDestructionCallbacks(name);
		return result;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		if (!destructionCallbacks.containsKey(name)) {
			destructionCallbacks.putIfAbsent(name,
					new CopyOnWriteArrayList<Runnable>());
		}
		List<Runnable> callbacks = destructionCallbacks.get(name);
		callbacks.add(callback);
	}

	@Override
	public String getConversationId() {
		return iterationStartTime.get();
	}

	@Override
	public Object resolveContextualObject(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private void updateIterationStartTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				ITERATION_START_TIME_PATTERN);
		iterationStartTime.set(dateFormat.format(new Date()));
	}

	private void notifyDestructionCallbacks(String name) {
		List<Runnable> callbacks = destructionCallbacks.get(name);
		if (callbacks == null) {
			return;
		}
		for (Runnable callback : callbacks) {
			callback.run();
		}
	}

	public void nextIteration() {
		Map<String, Object> cacheSnapshot = new HashMap<String, Object>(cache);
		cache.clear();
		updateIterationStartTime();
		for (String name : cacheSnapshot.keySet()) {
			notifyDestructionCallbacks(name);
		}
	}
}
