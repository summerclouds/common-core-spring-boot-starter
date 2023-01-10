/**
 * Copyright (C) 2022 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.summerclouds.common.core.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.summerclouds.common.core.condition.SConditionalOnProperty;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.junit.TestCase;

@SpringBootTest(
        classes = {SpringSummerCloudsCoreAutoConfiguration.class},
        properties = {"aaa=bbb"})
public class ConditionsTest extends TestCase {

    @Test
    public void testExpressions() {
        {
            boolean b = MSpring.checkConditions(FalseCondition.class);
            assertFalse(b);
        }
        {
            boolean b = MSpring.checkConditions(NoCondition.class);
            assertTrue(b);
        }
        {
            boolean b = MSpring.checkConditions(TrueCondition.class);
            assertTrue(b);
        }
    }

    public static class NoCondition {}

    @SConditionalOnProperty(value = "aaa", havingValue = "xxx")
    public static class FalseCondition {}

    @SConditionalOnProperty(value = "aaa", havingValue = "bbb")
    public static class TrueCondition {}
}
