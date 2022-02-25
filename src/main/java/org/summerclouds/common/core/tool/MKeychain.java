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

import java.io.IOException;
import java.util.UUID;

import org.summerclouds.common.core.cfg.BeanRef;
import org.summerclouds.common.core.error.MException;
import org.summerclouds.common.core.util.IKeychain;
import org.summerclouds.common.core.util.SecureString;

public class MKeychain {

	private MKeychain() {}
	
	private static final BeanRef<IKeychain> instance = new BeanRef<>(IKeychain.class);

    static final String TYPE_RSA_PRIVATE_KEY = "rsa.cipher.private.key";
    static final String TYPE_RSA_PUBLIC_KEY = "rsa.cipher.public.key";
    static final String TYPE_AES_PRIVATE_KEY = "aes.cipher.private.key";
    static final String TYPE_AES_PUBLIC_KEY = "aes.cipher.public.key";
    static final String TYPE_DSA_PRIVATE_KEY = "dsa.sign.private.key";

    static final String TYPE_DSA_PUBLIC_KEY = "dsa.sign.public.key";
    static final String TYPE_ECC_PRIVATE_KEY = "ecc.sign.private.key";
    static final String TYPE_ECC_PUBLIC_KEY = "ecc.sign.public.key";

    static final String TYPE_TEXT = "text";
    static final String TYPE_CIPHER = "cipher";
    static final String TYPE_SIGNATURE = "signature";

    static final String SOURCE_DEFAULT = "default";

    static final String SUFFIX_CIPHER_PRIVATE_KEY = ".cipher.private.key";
    static final String SUFFIX_CIPHER_PUBLIC_KEY = ".cipher.public.key";
    static final String SUFFIX_SIGN_PRIVATE_KEY = ".sign.private.key";
    static final String SUFFIX_SIGN_PUBLIC_KEY = ".sign.public.key";

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
    
    public static interface KeyEntry {

        /**
         * Returns the unique id of the entry.
         *
         * @return The unique id
         */
        UUID getId();

        /**
         * Returns the type of the entry as string. A list of default types is defined in MVault.
         *
         * @return The type of the entry, never null.
         */
        String getType();

        /**
         * Return a readable description describe the key and/or the usage.
         *
         * @return description
         */
        String getDescription();

        /**
         * Return the value of the entry as text.
         *
         * @return The entry as text.
         */
        SecureString getValue();

        /**
         * Return a technical name of the entry.
         *
         * @return The name
         */
        String getName();
    }    
    
    public static interface KeychainSource {

        /**
         * Return a entry by id or null if not found.
         *
         * @param id
         * @return The id or null
         */
        KeyEntry getEntry(UUID id);

        /**
         * Return a not editable list of current stored entry ids.
         *
         * @return a list of ids.
         */
        Iterable<UUID> getEntryIds();

        /**
         * Return a unique name of the source.
         *
         * @return the name
         */
        String getName();

        /**
         * Return a editable instance or null if not supported
         *
         * @return editable vault source
         */
        MutableVaultSource getEditable();

        /**
         * Return a entry by name or null if not found. Return the first entry found.
         *
         * @param name
         * @return The id or null
         */
        KeyEntry getEntry(String name);
    }
    
    public static interface MutableVaultSource extends KeychainSource {

        void addEntry(KeyEntry entry) throws MException;

        void removeEntry(UUID id) throws MException;

        void doLoad() throws IOException;

        void doSave() throws IOException;

        /**
         * Return true if load and save is needed to persist changed data.
         *
         * @return true if storage is in memory
         */
        boolean isMemoryBased();

        void updateEntry(KeyEntry entry) throws MException;
    }

}
