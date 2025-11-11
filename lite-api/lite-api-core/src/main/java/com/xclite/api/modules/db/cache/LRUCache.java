/*
 * MIT License
 *
 * Copyright (c) 2020 小东
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package com.xclite.api.modules.db.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LRU缓存实现
 *
 * @author mxd
 * 修改者: zhixiulee
 */
public class LRUCache extends LinkedHashMap<String, LRUCache.ExpireNode<Object>> {

	private final String separator = ":";

	private final int capacity;

	private final long expire;

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public LRUCache(int capacity, long expire) {
		super((int) Math.ceil(capacity / 0.75) + 1, 0.75f, true);
		// 容量
		this.capacity = capacity;
		// 固定过期时间
		this.expire = expire;
	}

	public void put(String name, String key, Object value) {
		// 封装成过期时间节点
		put(name, key, value, this.expire);
	}

	public void put(String name, String key, Object value, long ttl) {
		long expireTime = ttl > 0 ? (System.currentTimeMillis() + ttl) : (this.expire > -1 ? System.currentTimeMillis() + this.expire : Long.MAX_VALUE);
		lock.writeLock().lock();
		try {
			// 封装成过期时间节点
			put(name + separator + key, new ExpireNode<>(expireTime, value));
		} finally {
			lock.writeLock().unlock();
		}
	}

	public Object get(String name, String key) {
		key = name + separator + key;
		lock.readLock().lock();
		ExpireNode<Object> expireNode;
		try {
			expireNode = super.get(key);
		} finally {
			lock.readLock().unlock();
		}
		if (expireNode == null) {
			return null;
		}
		// 惰性删除过期的
//        if (this.expire > -1L && expireNode.expire < System.currentTimeMillis()) {
		if (expireNode.expire < System.currentTimeMillis()) {
			try {
				lock.writeLock().lock();
				super.remove(key);
			} finally {
				lock.writeLock().unlock();
			}
			return null;
		}
		return expireNode.value;
	}

	public void delete(String name) {
		try {
			lock.writeLock().lock();
			Iterator<Map.Entry<String, ExpireNode<Object>>> iterator = super.entrySet().iterator();
			String prefix = name + separator;
			// 清除所有key前缀为name + separator的缓存
			while (iterator.hasNext()) {
				Map.Entry<String, ExpireNode<Object>> entry = iterator.next();
				if (entry.getKey().startsWith(prefix)) {
					iterator.remove();
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}


	@Override
	protected boolean removeEldestEntry(Map.Entry<String, ExpireNode<Object>> eldest) {
		if (this.expire > -1L && size() > capacity) {
			clean();
		}
		// lru淘汰
		return size() > this.capacity;
	}

	/**
	 * 清理已过期的数据
	 */
	private void clean() {
		try {
			lock.writeLock().lock();
			Iterator<Map.Entry<String, ExpireNode<Object>>> iterator = super.entrySet().iterator();
			long now = System.currentTimeMillis();
			while (iterator.hasNext()) {
				Map.Entry<String, ExpireNode<Object>> next = iterator.next();
				// 判断是否过期
				if (next.getValue().expire < now) {
					iterator.remove();
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}


	/**
	 * 过期时间节点
	 */
	static class ExpireNode<V> {
		long expire;
		V value;

		ExpireNode(long expire, V value) {
			this.expire = expire;
			this.value = value;
		}
	}
}
