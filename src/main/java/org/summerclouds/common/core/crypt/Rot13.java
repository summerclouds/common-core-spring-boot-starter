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
package org.summerclouds.common.core.crypt;

public class Rot13 {

    /**
     * Encode characters +13 and Numbers +5
     *
     * @param in
     * @return
     */
    public static String encode13And5(String in) {
        return decode13And5(in);
    }

    /**
     * Decode characters +13 and Numbers +5
     *
     * @param in
     * @return
     */
    public static String decode13And5(String in) {

        StringBuilder out = new StringBuilder();

        for (int i = 0; i < in.length(); i++) {
            int chr = in.charAt(i);

            // convert char if required
            if ((chr >= 'A') && (chr <= 'Z')) {
                chr += 13;
                if (chr > 'Z') chr -= 26;
            } else if ((chr >= 'a') && (chr <= 'z')) {
                chr += 13;
                if (chr > 'z') chr -= 26;
            } else if ((chr >= '0') && (chr <= '9')) {
                chr += 5;
                if (chr > '9') chr -= 10;
            }

            // and return it to sender
            out.append((char) chr);
        }

        return out.toString();
    }

    /**
     * Encode characters +13
     *
     * @param in
     * @return
     */
    public static String encode13(String in) {
        return decode13(in);
    }

    /**
     * Decode characters +13
     *
     * @param in
     * @return
     */
    public static String decode13(String in) {

        StringBuilder out = new StringBuilder();

        for (int i = 0; i < in.length(); i++) {
            int chr = in.charAt(i);

            // convert char if required
            if ((chr >= 'A') && (chr <= 'Z')) {
                chr += 13;
                if (chr > 'Z') chr -= 26;
            } else if ((chr >= 'a') && (chr <= 'z')) {
                chr += 13;
                if (chr > 'z') chr -= 26;
            }

            // and return it to sender
            out.append((char) chr);
        }

        return out.toString();
    }
}
