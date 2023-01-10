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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;
import org.summerclouds.common.core.crypt.MD5;
import org.summerclouds.common.core.crypt.Rot13;
import org.summerclouds.common.core.tool.MLorem;
import org.summerclouds.common.junit.TestCase;

public class CryptTest extends TestCase {

    @Test
    public void testRot13() {
        String lorem = MLorem.createWithSize(1000);
        String encoded = Rot13.encode13(lorem);
        String decoded = Rot13.decode13(encoded);
        assertEquals(lorem, decoded);
    }

    @Test
    public void testRot13And10() {
        String lorem = MLorem.createWithSize(1000) + Math.random() + Math.random();
        String encoded = Rot13.encode13And5(lorem);
        String decoded = Rot13.decode13And5(encoded);
        assertEquals(lorem, decoded);
    }

    @Test
    public void testMD5() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        {
            String lorem = MLorem.createWithSize(1000);
            String hash = MD5.hash(lorem);

            byte[] bytesOfMessage = lorem.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            String javaHash = DatatypeConverter.printHexBinary(theMD5digest).toLowerCase();

            assertEquals(javaHash, hash);
        }
        {
            String lorem =
                    "11111111112222222222333333333344444444445555555555666666666677777777778888888888999999999900000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899999999990000000000";
            String hash = MD5.hash(lorem);

            byte[] bytesOfMessage = lorem.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            String javaHash = DatatypeConverter.printHexBinary(theMD5digest).toLowerCase();

            assertEquals(javaHash, hash);
        }
    }
}
