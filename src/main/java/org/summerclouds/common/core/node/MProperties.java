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
package org.summerclouds.common.core.node;

import java.io.BufferedWriter;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.tool.MString;
import org.summerclouds.common.core.tool.MSystem;
import org.summerclouds.common.core.util.SetCast;

public class MProperties extends AbstractProperties implements Externalizable {

    private static final long serialVersionUID = 1L;
    private static final Log log = Log.getLog(MProperties.class);

    protected Properties properties = null;
    private static final char[] hexDigit = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public MProperties() {
        this(new Properties());
    }

    // use IProperties.to() instead
    //    public MProperties(String... values) {
    //        this(new Properties());
    //        if (values != null) {
    //            for (int i = 0; i < values.length; i += 2) {
    //                if (i + 1 < values.length)
    //                    IProperties.appendToMap(this, values[i] + "=" + values[i + 1], '=', ':');
    //            }
    //        }
    //    }

    public MProperties(Dictionary<?, ?> config) {
        this.properties = new Properties();
        for (Enumeration<?> enu = config.keys(); enu.hasMoreElements(); ) {
            Object next = enu.nextElement();
            this.properties.put(String.valueOf(next), config.get(next));
        }
    }

    public MProperties(Map<?, ?> in) {
        this.properties = new Properties();
        if (in != null)
            for (Map.Entry<?, ?> e : in.entrySet())
                if (e.getKey() != null && e.getValue() != null)
                    this.properties.put(String.valueOf(e.getKey()), e.getValue());
    }

    public MProperties(IReadProperties in) {
        this.properties = new Properties();
        if (in != null)
            for (Map.Entry<?, ?> e : in.entrySet())
                if (e.getKey() != null && e.getValue() != null)
                    this.properties.put(String.valueOf(e.getKey()), e.getValue());
    }

    // need this constructor to avoid ambiguous references for IProperties
    public MProperties(IProperties in) {
        this.properties = new Properties();
        if (in != null)
            for (Map.Entry<?, ?> e : in.entrySet())
                if (e.getKey() != null && e.getValue() != null)
                    this.properties.put(String.valueOf(e.getKey()), e.getValue());
    }

    public MProperties(Properties properties) {
        this.properties = properties;
        if (properties == null) this.properties = new Properties();
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public boolean isProperty(String key) {
        return properties.containsKey(key);
    }

    @Override
    public void removeProperty(String key) {
        properties.remove(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        if (value == null) properties.remove(key);
        else properties.put(key, value);
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public Set<String> keys() {
        return new SetCast<Object, String>(properties.keySet());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(properties);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        properties = (Properties) in.readObject();
    }

    @Override
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    @Override
    public Collection<Object> values() {
        return properties.values();
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        HashMap<String, Object> wrapper = new HashMap<>();
        for (java.util.Map.Entry<Object, Object> entry : properties.entrySet())
            wrapper.put(String.valueOf(entry.getKey()), entry.getValue());
        return wrapper.entrySet();
    }

    public static MProperties load(String fileName) {
        Properties p = new Properties();
        try {
            File f = new File(fileName);
            if (f.exists() && f.isFile()) {
                FileInputStream is = new FileInputStream(f);
                p.load(is);
            }
        } catch (Exception t) {
            log.d(fileName, t);
        }
        MProperties out = new MProperties(p);
        return out;
    }

    public static MProperties loadOrEmpty(File f) {
        MProperties out = load(f);
        if (out == null) out = new MProperties();
        return out;
    }

    public static MProperties load(File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            MProperties out = load(fis);
            fis.close();
            return out;
        } catch (IOException e) {
            return null;
        }
        //		Properties p = new Properties();
        //		try {
        //			if (f.exists() && f.isFile()) {
        //				FileInputStream is = new FileInputStream(f);
        //				p.load(is);
        //			}
        //		} catch (Exception t) {
        //			MLogUtil.log().d(f, t);
        //		}
        //		MProperties out = new MProperties(p);
        //		return out;
    }

    public static MProperties load(InputStream inStream) throws IOException {
        MProperties out = new MProperties();
        out.load0(new LineReader(inStream));
        return out;
    }

    public static MProperties loadFromString(String content) {
        StringReader reader = new StringReader(content);
        return load(reader);
    }

    private void load0(LineReader lr) throws IOException {
        char[] convtBuf = new char[1024];
        int limit;
        int keyLen;
        int valueStart;
        char c;
        boolean hasSep;
        boolean precedingBackslash;

        while ((limit = lr.readLine()) >= 0) {
            c = 0;
            keyLen = 0;
            valueStart = limit;
            hasSep = false;

            // System.out.println("line=<" + new String(lineBuf, 0, limit) + ">");
            precedingBackslash = false;
            while (keyLen < limit) {
                c = lr.lineBuf[keyLen];
                // need check if escaped.
                //                if ((c == '=' ||  c == ':') && !precedingBackslash) {
                if (c == '=' && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    hasSep = true;
                    break;
                } else if ((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    break;
                }
                if (c == '\\') {
                    precedingBackslash = !precedingBackslash;
                } else {
                    precedingBackslash = false;
                }
                keyLen++;
            }
            while (valueStart < limit) {
                c = lr.lineBuf[valueStart];
                if (c != ' ' && c != '\t' && c != '\f') {
                    //                    if (!hasSep && (c == '=' ||  c == ':')) {
                    if (!hasSep && c == '=') {
                        hasSep = true;
                    } else {
                        break;
                    }
                }
                valueStart++;
            }
            String key = loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
            String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);
            put(key, value);
        }
    }

    private static class LineReader {
        public LineReader(InputStream inStream) {
            this.inStream = inStream;
            inByteBuf = new byte[8192];
        }

        //        public LineReader(Reader reader) {
        //            this.reader = reader;
        //            inCharBuf = new char[8192];
        //        }

        byte[] inByteBuf;
        char[] inCharBuf;
        char[] lineBuf = new char[1024];
        int inLimit = 0;
        int inOff = 0;
        InputStream inStream;
        Reader reader;

        int readLine() throws IOException {
            int len = 0;
            char c = 0;

            boolean skipWhiteSpace = true;
            boolean isCommentLine = false;
            boolean isNewLine = true;
            boolean appendedLineBegin = false;
            boolean precedingBackslash = false;
            boolean skipLF = false;

            while (true) {
                if (inOff >= inLimit) {
                    inLimit =
                            (inStream == null) ? reader.read(inCharBuf) : inStream.read(inByteBuf);
                    inOff = 0;
                    if (inLimit <= 0) {
                        if (len == 0 || isCommentLine) {
                            return -1;
                        }
                        if (precedingBackslash) {
                            len--;
                        }
                        return len;
                    }
                }
                if (inStream != null) {
                    // The line below is equivalent to calling a
                    // ISO8859-1 decoder.
                    c = (char) (0xff & inByteBuf[inOff++]);
                } else {
                    c = inCharBuf[inOff++];
                }
                if (skipLF) {
                    skipLF = false;
                    if (c == '\n') {
                        continue;
                    }
                }
                if (skipWhiteSpace) {
                    if (c == ' ' || c == '\t' || c == '\f') {
                        continue;
                    }
                    if (!appendedLineBegin && (c == '\r' || c == '\n')) {
                        continue;
                    }
                    skipWhiteSpace = false;
                    appendedLineBegin = false;
                }
                if (isNewLine) {
                    isNewLine = false;
                    if (c == '#' || c == '!') {
                        isCommentLine = true;
                        continue;
                    }
                }

                if (c != '\n' && c != '\r') {
                    lineBuf[len++] = c;
                    if (len == lineBuf.length) {
                        int newLength = lineBuf.length * 2;
                        if (newLength < 0) {
                            newLength = Integer.MAX_VALUE;
                        }
                        char[] buf = new char[newLength];
                        System.arraycopy(lineBuf, 0, buf, 0, lineBuf.length);
                        lineBuf = buf;
                    }
                    // flip the preceding backslash flag
                    if (c == '\\') {
                        precedingBackslash = !precedingBackslash;
                    } else {
                        precedingBackslash = false;
                    }
                } else {
                    // reached EOL
                    if (isCommentLine || len == 0) {
                        isCommentLine = false;
                        isNewLine = true;
                        skipWhiteSpace = true;
                        len = 0;
                        continue;
                    }
                    if (inOff >= inLimit) {
                        inLimit =
                                (inStream == null)
                                        ? reader.read(inCharBuf)
                                        : inStream.read(inByteBuf);
                        inOff = 0;
                        if (inLimit <= 0) {
                            if (precedingBackslash) {
                                len--;
                            }
                            return len;
                        }
                    }
                    if (precedingBackslash) {
                        len -= 1;
                        // skip the leading whitespace characters in following line
                        skipWhiteSpace = true;
                        appendedLineBegin = true;
                        precedingBackslash = false;
                        if (c == '\r') {
                            skipLF = true;
                        }
                    } else {
                        return len;
                    }
                }
            }
        }
    }

    private String loadConvert(char[] in, int off, int len, char[] convtBuf) {
        if (convtBuf.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            convtBuf = new char[newLen];
        }
        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;

        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = aChar;
            }
        }
        return new String(out, 0, outLen);
    }

    //	public static MProperties load(InputStream is) {
    //		Properties p = new Properties();
    //		try {
    //			p.load(is);
    //		} catch (Exception t) {
    //			MLogUtil.log().d(t);
    //		}
    //		MProperties out = new MProperties(p);
    //		return out;
    //	}

    public static MProperties load(Reader is) {
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (Exception t) {
            log.d(t);
        }
        MProperties out = new MProperties(p);
        return out;
    }

    public void putAll(String prefix, Map<?, ?> map) {
        for (Entry<?, ?> entry : map.entrySet())
            put(String.valueOf(prefix + entry.getKey()), entry.getValue());
    }

    @Override
    public int size() {
        return properties.size();
    }

    public boolean save(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        boolean ret = save(fos);
        fos.close();
        return ret;
    }

    public boolean save(File file, boolean addDate) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        boolean ret = save(fos, addDate);
        fos.close();
        return ret;
    }

    public String saveToString() {
        StringWriter out = new StringWriter();
        try {
            store(new BufferedWriter(out), true, true);
        } catch (IOException e) {
            log().e(e);
        }
        return out.toString();
    }

    public String saveToString(boolean addDate) {
        StringWriter out = new StringWriter();
        try {
            store(new BufferedWriter(out), true, addDate);
        } catch (IOException e) {
            log().e(e);
        }
        return out.toString();
    }

    public boolean save(OutputStream out, boolean addDate) throws IOException {
        store(
                new BufferedWriter(new OutputStreamWriter(out, MString.CHARSET_UTF_8)),
                true,
                addDate);
        return true;
    }

    public boolean save(OutputStream out) throws IOException {
        store(new BufferedWriter(new OutputStreamWriter(out, MString.CHARSET_UTF_8)), true, true);
        return true;
    }

    private void store(BufferedWriter bw, boolean escUnicode, boolean addDate) throws IOException {
        if (addDate) {
            bw.write("#" + new Date().toString());
            bw.newLine();
        }
        synchronized (this) {
            for (String key : sortedKeys()) {
                String val = (String) getString(key, "");
                key = saveConvert(key, true, escUnicode);
                /* No need to escape embedded and trailing spaces for value, hence
                 * pass false to flag.
                 */
                val = saveConvert(val, false, escUnicode);
                bw.write(key + "=" + val);
                bw.newLine();
            }
        }
        bw.flush();
    }

    public Set<String> sortedKeys() {
        return new TreeSet<String>(new SetCast<Object, String>(properties.keySet()));
    }

    private String saveConvert(String value, boolean escapeSpace, boolean escapeUnicode) {
        int len = value.length();
        int bufLen = len * 2;
        if (bufLen < 0) bufLen = Integer.MAX_VALUE;
        StringBuilder outBuffer = new StringBuilder(bufLen);
        for (int x = 0; x < len; x++) {
            char aChar = value.charAt(x);
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
                case ' ':
                    if (x == 0 || escapeSpace) outBuffer.append('\\');
                    outBuffer.append(' ');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                case '=':
                case ':':
                case '#':
                case '!':
                    outBuffer.append('\\');
                    outBuffer.append(aChar);
                    break;
                default:
                    if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex(aChar & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    @Override
    public void clear() {
        properties.clear();
    }

    @Override
    public synchronized String toString() {
        int max = properties.size() - 1;
        if (max == -1) return "{}";

        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();

        sb.append('{');
        for (int i = 0; ; i++) {
            Map.Entry<Object, Object> e = it.next();
            Object key = e.getKey();
            Object value = e.getValue();
            String keyStr = key.toString();
            sb.append(keyStr);
            sb.append('=');
            sb.append(
                    MSystem.isPasswordName(keyStr)
                            ? "[***]"
                            : (value == this ? "(this Map)" : value.toString()));

            if (i == max) return sb.append('}').toString();
            sb.append(", ");
        }
    }
}
