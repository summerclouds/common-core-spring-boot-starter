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
package org.summerclouds.common.core.tool;

import java.util.UUID;

import org.summerclouds.common.core.cfg.BeanRef;
import org.summerclouds.common.core.crypt.IKeychain;
import org.summerclouds.common.core.crypt.KeyEntry;
import org.summerclouds.common.core.crypt.KeychainSource;

public class MKeychain {

	private MKeychain() {}
	
	private static final BeanRef<IKeychain> instance = new BeanRef<>(IKeychain.class);

    public static final String TYPE_RSA_PRIVATE_KEY = "rsa.cipher.private.key";
    public static final String TYPE_RSA_PUBLIC_KEY = "rsa.cipher.public.key";
    public static final String TYPE_AES_PRIVATE_KEY = "aes.cipher.private.key";
    public static final String TYPE_AES_PUBLIC_KEY = "aes.cipher.public.key";
    public static final String TYPE_DSA_PRIVATE_KEY = "dsa.sign.private.key";

    public static final String TYPE_DSA_PUBLIC_KEY = "dsa.sign.public.key";
    public static final String TYPE_ECC_PRIVATE_KEY = "ecc.sign.private.key";
    public static final String TYPE_ECC_PUBLIC_KEY = "ecc.sign.public.key";

    public static final String TYPE_TEXT = "text";
    public static final String TYPE_CIPHER = "cipher";
    public static final String TYPE_SIGNATURE = "signature";

    public static final String SOURCE_DEFAULT = "default";

    public static final String SUFFIX_CIPHER_PRIVATE_KEY = ".cipher.private.key";
    public static final String SUFFIX_CIPHER_PUBLIC_KEY = ".cipher.public.key";
    public static final String SUFFIX_SIGN_PRIVATE_KEY = ".sign.private.key";
    public static final String SUFFIX_SIGN_PUBLIC_KEY = ".sign.public.key";

    public static IKeychain get() {
    	return instance.bean();
    }
    
    /**
     * Register a new source for VaultEntries
     *
     * @param source
     */
    public static void registerSource(KeychainSource source) {
    	get().registerSource(source);
    }
    
    /**
     * Unregister a registered source
     *
     * @param sourceName
     */
    public static void unregisterSource(String sourceName) {
    	get().unregisterSource(sourceName);
    }

    /**
     * Return a list of registered sources
     *
     * @return a list of names.
     */
    public static String[] getSourceNames() {
    	return get().getSourceNames();
    }

    /**
     * Return a single source or null if not found.
     *
     * @param name
     * @return the source or null.
     */
    public static KeychainSource getSource(String name) {
    	return get().getSource(name);
    }

    /**
     * Return a entry by id or null if not found.
     *
     * @param id
     * @return The entry or null.
     */
    public static KeyEntry getEntry(UUID id) {
    	return get().getEntry(id);
    }

    /**
     * Return a entry by name or null if not found. The method will return the first entry found.
     *
     * @param name
     * @return The entry or null.
     */
    public static KeyEntry getEntry(String name) {
    	return get().getEntry(name);
    }

}
