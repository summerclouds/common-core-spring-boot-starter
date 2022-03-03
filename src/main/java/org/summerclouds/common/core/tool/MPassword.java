package org.summerclouds.common.core.tool;

import org.summerclouds.common.core.cfg.BeanRef;
import org.summerclouds.common.core.crypt.IPassword;

public class MPassword {

	public static final String ROT13 = "R1";
	public static final String ROT13AND10 = "R2";
	public static final String DUMMY = "DUMMY";
	public static final String HASH_MD5 = "HASH_MD5";

	// legacy
    private static final String PREFIX_DUMMY = "`X";
    private static final String PREFIX_ROT13 = "`B:";
    private static final String PREFIX_SPECIAL1 = "`A";
    
    public static final String PREFIX = "`";
	public static final String SEPARATOR = "$";

	private MPassword() {}
	
	private static BeanRef<IPassword> instance = new BeanRef<>(IPassword.class);
	
	public static IPassword get() {
		return instance.bean();
	}

	public static String[] supportedEncodings() {
		return get().supportedEncodings();
	}
	
	public static String encode(String method, String plain, String secret) {
		return PREFIX + ROT13AND10 + SEPARATOR + get().encode(method, plain, secret);
	}

	public static String decode(String method, String encoded, String secret) {
		return get().decode(method, encoded, secret);
	}

    public static String encode(String plain) {
        return encode(ROT13AND10, plain, null);
    }
    
    public static boolean isEncoded(String plain) {
        if (plain == null) return false;
        return plain.startsWith(PREFIX);
    }

    public static String decode(String encoded) {
    	return decode(encoded, null);
    }
    
    public static String decode(String encoded, String secret) {
        if (encoded == null) return null;
        if (!isEncoded(encoded)) return encoded;
        // legacy
        if (encoded.startsWith(PREFIX_ROT13)) return get().decode(ROT13AND10, encoded, null);
        if (encoded.startsWith(PREFIX_DUMMY)) return get().decode(DUMMY, encoded, null);
        if (encoded.startsWith(PREFIX_SPECIAL1)) {
            StringBuilder out = new StringBuilder();
            for (int i = 2; i < encoded.length(); i++) {
                char c = encoded.charAt(i);
                switch (c) {
                    case '0':
                        c = '9';
                        break;
                    case '1':
                        c = '0';
                        break;
                    case '2':
                        c = '1';
                        break;
                    case '3':
                        c = '2';
                        break;
                    case '4':
                        c = '3';
                        break;
                    case '5':
                        c = '4';
                        break;
                    case '6':
                        c = '5';
                        break;
                    case '7':
                        c = '6';
                        break;
                    case '8':
                        c = '7';
                        break;
                    case '9':
                        c = '8';
                        break;
                }
                out.append(c);
            }
            return out.toString();
        }
        // END legacy
        int pos = encoded.indexOf('$',1);
        if (pos == -1) return encoded;
        String method = encoded.substring(1, pos);
        encoded = encoded.substring(pos+1);
        return get().decode(method, encoded, secret);
    }


	
	
}
