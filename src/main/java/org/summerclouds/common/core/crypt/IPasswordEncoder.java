package org.summerclouds.common.core.crypt;

public interface IPasswordEncoder {

    String encode(String plain, String secret);

    String decode(String encoded, String secret);

    boolean validate(String plain, String encoded, String secret);
    
}
