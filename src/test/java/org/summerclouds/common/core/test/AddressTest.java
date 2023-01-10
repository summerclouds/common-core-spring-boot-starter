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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.summerclouds.common.core.internal.SpringSummerCloudsCoreAutoConfiguration;
import org.summerclouds.common.core.util.Address;
import org.summerclouds.common.core.util.Address.SALUTATION;
import org.summerclouds.common.junit.TestCase;

@SpringBootTest
@ContextConfiguration(classes = SpringSummerCloudsCoreAutoConfiguration.class)
public class AddressTest extends TestCase {

    @Test
    public void testSalutationDe() {
        {
            SALUTATION sal = Address.toSalutation("Herr");
            System.out.println(sal);
            assertEquals(SALUTATION.MR, sal);
            String txt = Address.toSalutationString(sal, Locale.GERMAN);
            System.out.println(txt);
            assertEquals("Herr", txt);
        }
        {
            SALUTATION sal = Address.toSalutation("frau");
            System.out.println(sal);
            assertEquals(SALUTATION.MRS, sal);
            String txt = Address.toSalutationString(sal, Locale.GERMAN);
            System.out.println(txt);
            assertEquals("Frau", txt);
        }
    }
}
