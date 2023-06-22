package org.jfrog.security.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * This class is used to avoid import a another library
 * Also can give me some convenience
 */
public class BCProviderFactory {
    private static final BouncyCastleProvider BC_PROVIDER = new BouncyCastleProvider();

    static {
        BC_PROVIDER.replace("KeyStore.BKS-V1", "BKS-V1 is forbidden because of CVE-2018-5382");
        Security.addProvider(BC_PROVIDER);
    }

    public static BouncyCastleProvider getProvider() {
        return BC_PROVIDER;
    }
}
