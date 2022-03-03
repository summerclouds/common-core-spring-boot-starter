package org.summerclouds.common.core.crypt;

import org.summerclouds.common.core.activator.DefaultImplementation;

@DefaultImplementation(DefaultPassword.class)
public interface IPassword {

	String[] supportedEncodings();
	
    String encode(String method, String plain, String secret);

    String decode(String method, String encoded, String secret);

}
