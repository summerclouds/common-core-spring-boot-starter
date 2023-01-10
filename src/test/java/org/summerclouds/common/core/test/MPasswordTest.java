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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.summerclouds.common.core.tool.MPassword;
import org.summerclouds.common.junit.TestCase;

public class MPasswordTest extends TestCase {

    private static String[] TESTIFY = {
        "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_.:,;+*#<>", "test"
    };

    @Test
    public void testPassword() {
        for (String pw : TESTIFY) {
            System.out.println("> " + pw);
            String enc = MPassword.encode(pw);
            System.out.println("< " + enc);
            String dec = MPassword.decode(enc);
            assertTrue(pw.equals(dec));
        }
    }

    @Test
    public void testMD5() {
        String method = MPassword.MD5;
        for (String pw : TESTIFY) {
            System.out.println("> " + pw);
            String enc = MPassword.encode(method, pw);
            System.out.println("< " + enc);
            assertTrue(MPassword.validate(pw, enc));
        }
    }

    @Test
    public void testRot13() {
        String method = MPassword.ROT13;
        for (String pw : TESTIFY) {
            System.out.println("> " + pw);
            String enc = MPassword.encode(method, pw);
            System.out.println("< " + enc);
            String dec = MPassword.decode(enc);
            System.out.println("# " + dec);
            assertTrue(pw.equals(dec));
        }
    }

    @Test
    public void testRot13And10() {
        String method = MPassword.ROT13AND5;
        for (String pw : TESTIFY) {
            System.out.println("> " + pw);
            String enc = MPassword.encode(method, pw);
            System.out.println("< " + enc);
            String dec = MPassword.decode(enc);
            System.out.println("# " + dec);
            assertTrue(pw.equals(dec));
        }
    }
}
