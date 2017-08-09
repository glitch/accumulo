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

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * Basic tests for ParameterValidator
 */
public class ParameterValidatorTest {

  @Test
  public void testAlphaNumRegex() {
    Pattern p = Pattern.compile(ParameterValidator.ALPHA_NUM_REGEX);
    Assert.assertTrue(p.matcher("asdlkfj234kj324").matches());
    Assert.assertFalse(p.matcher("234-324").matches());
    Assert.assertFalse(p.matcher("").matches());
    
    p = Pattern.compile(ParameterValidator.ALPHA_NUM_REGEX_BLANK_OK);
    Assert.assertTrue(p.matcher("asdlkfj234kj324").matches());
    Assert.assertTrue(p.matcher("").matches());
    Assert.assertFalse(p.matcher("234-324").matches());
  }
  
  @Test
  public void testServerRegex() throws Exception {
    Pattern p = Pattern.compile(ParameterValidator.SERVER_REGEX);
    Assert.assertTrue(p.matcher("ab3cd.12d34.3xyz.net").matches());
    Assert.assertTrue(p.matcher("abcd.123.server-foo.com").matches());
    
    Assert.assertFalse(p.matcher("abcd.1234.*.xyz").matches());

    // TODO would you ever have a port number in tserverAddress?
    // abc-xyz.server:1234 or 192.168.1.1:9999
  }
  
}
