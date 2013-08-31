/*
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
 * 
 */
package venigma.provider;

import venigma.server.VCipherSpi;

/**
 *
 * @author evan.summers
 */

public class VProvider extends java.security.Provider {
    public static final ProviderContext providerContext  = new ProviderContext();
    
    public VProvider() {
        super("VProvider", 1.0, "Provides KeyStore.JCEKS");
        put("KeyStore.JCEKS", KeyStoreSpi.class.getName());
        put("Cipher.AES", VCipherSpi.class.getName());
    }    
}