/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.accumulo.monitor.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

/**
 * Simple utility class to validate Accumulo Monitor Query and Path parameters
 */
public class ParameterValidator {

  /**
   * @param s
   *          String parameter to sanitize. Common usages are tableId, type, tserverAddress
   * @return URLEncoder encoded version of the passed in String parameter
   * @throws UnsupportedEncodingException
   *           if the string cannot be encoded to UTF-8
   */
  public static String sanitizeParameter(String s) throws UnsupportedEncodingException {
    return StringUtils.isEmpty(s) ? StringUtils.EMPTY : URLEncoder.encode(s, "UTF-8").trim();
  }

  public static String sanitizeParameter(String s, String defaultValue) throws UnsupportedEncodingException {
    try {
      String sanitized = sanitizeParameter(s);
      return StringUtils.EMPTY.equals(sanitized) ? defaultValue : sanitized;
    } catch (UnsupportedEncodingException e) {
      return defaultValue;
    }
  }
}
