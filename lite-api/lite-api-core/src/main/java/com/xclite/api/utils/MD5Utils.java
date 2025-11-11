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

package com.xclite.api.utils;

import com.xclite.api.exception.LiteApiException;

import java.security.MessageDigest;

/**
 * MD5加密类
 *
 * @author mxd
 * 修改者: zhixiulee
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
