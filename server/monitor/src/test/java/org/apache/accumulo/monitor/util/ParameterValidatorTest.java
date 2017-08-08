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

/**
 * Basic tests for ParameterValidator
 */
public class ParameterValidatorTest {

  @Test
  public void sanitizeParameterTest() throws Exception {
    String foo = "abcd.1234";
    Assert.assertEquals(ParameterValidator.sanitizeParameter(foo), foo);

    foo = "abcd.123.server-foo.com";
    Assert.assertEquals(ParameterValidator.sanitizeParameter(foo), foo);

    // TODO would you ever have a port number in tserverAddress?
    // abc-xyz.server:1234 or 192.168.1.1:9999

    // test what happens with csv values
    // abc,def,xyz
    foo = "abc,def,xyz";
    Assert.assertEquals(ParameterValidator.sanitizeParameter(foo), "abc%2Cdef%2Cxyz");
  }

  @Test
  public void sanitizeParameterWithDefaultValueTest() throws Exception {
    Assert.assertEquals(ParameterValidator.sanitizeParameter("", "default"), "default");
    Assert.assertEquals(ParameterValidator.sanitizeParameter(null, "default"), "default");
  }

}
