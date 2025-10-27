/*
 * Copyright 2025 xclite.com and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.xclite.api.utils;

import com.xclite.api.exception.LiteApiException;

import java.security.MessageDigest;

/**
 * MD5加密类
 *
 * @author zhixiulee
 */
public class MD5Utils {
	/**
	 * 十六进制字符数组
	 */
	private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * MD5加密
	 */
	public static String encrypt(String value) {
		return encrypt(value.getBytes());
	}

	/**
	 * MD5加密
	 */
	public static String encrypt(byte[] value) {
		try {
			byte[] bytes = MessageDigest.getInstance("MD5").digest(value);
			char[] chars = new char[32];
			for (int i = 0; i < chars.length; i = i + 2) {
				byte b = bytes[i / 2];
				chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
				chars[i + 1] = HEX_CHARS[b & 0xf];
			}
			return new String(chars);
		} catch (Exception e) {
			throw new LiteApiException("md5 encrypt error", e);
		}
	}
}
