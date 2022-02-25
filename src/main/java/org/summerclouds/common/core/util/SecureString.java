/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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
package org.summerclouds.common.core.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.summerclouds.common.core.tool.MFile;
import org.summerclouds.common.core.tool.MMath;
import org.summerclouds.common.core.tool.MRandom;
import org.summerclouds.common.core.tool.MString;

public class SecureString implements Externalizable {

    private static final long serialVersionUID = 1L;
    protected byte[] data;
    protected int length;

    public SecureString() {}

    public SecureString(String data) {
        this.length = data == null ? 0 : data.length();
        if (data == null) return;
        this.data = obfuscate(MString.toBytes(data));
    }

    public String value() {
        if (data == null) return null;
        return MString.toString(unobfuscate(data));
    }

    public int length() {
        return length;
    }

    public boolean isNull() {
        return data == null;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(length);
        if (data == null) out.writeInt(-1);
        else {
            out.writeInt(data.length);
            out.write(data);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        length = in.readInt();
        int len = in.readInt();
        if (len < 0) {
            data = null;
        } else {
            data = new byte[len];
            MFile.readBinary(in, data, 0, len);
        }
    }

    @Override
    public String toString() {
        return "[***]";
    }
    
    public static byte[] unobfuscate(byte[] in) {
        if (in == null) return null;
        if (in.length < 2) return in;
        byte[] out = new byte[in.length - 1];
        byte salt = in[0];
        for (int i = 1; i < in.length; i++) out[i - 1] = MMath.subRotate(in[i], salt);
        return out;
    }

    public static byte[] obfuscate(byte[] in) {
        if (in == null) return null;
        if (in.length < 1) return in;
        byte[] out = new byte[in.length + 1];
        byte salt = MRandom.getByte();
        out[0] = salt;
        for (int i = 0; i < in.length; i++) out[i + 1] = MMath.addRotate(in[i], salt);
        return out;
    }

}
