package net.ilx.server.core.utils.jaxrs.client.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;

import net.ilx.server.core.utils.alf.util.StreamUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;

public class KeyHolder {
    private static final Logger LOG = Logger.getLogger(KeyHolder.class);

    private static final String JCEKS = "jceks";
    private Key key;

    public KeyHolder(final String keystore, final String serviceName) {
        String keystorePass = "P3r0Zd3r0";
        loadKeystore(keystore, keystorePass, serviceName);
    }

    private void loadKeystore(final String keystore,
                              final String keystorePass,
                              final String serviceName)
    {
        InputStream inputStream = null;
        try {
            KeyStore ks = KeyStore.getInstance(JCEKS);
            URL keystoreUrl = ResourceUtils.getURL(keystore);
            inputStream = keystoreUrl.openStream();
            ks.load(inputStream, keystorePass.toCharArray());
            key = ks.getKey(serviceName, keystorePass.toCharArray());
        }
        catch (Throwable t) {
            String msg = "Unable to load key from keystore";
            LOG.error(msg, t);
            throw new IllegalStateException(msg, t);
        }
        finally {
            StreamUtils.close(inputStream);
        }
    }

    /**
     * returns base64 encoded key used by the service
     *
     * @param serviceName
     * @return
     */
    public String getKey() {
        byte[] encoded = key.getEncoded();
        byte[] base64 = Base64.encodeBase64(encoded);
        String encodedKey;
        try {
            encodedKey = new String(base64, "ISO8859_1");
        } catch (UnsupportedEncodingException e) {
            String msg = "Unable to encode key";
            LOG.error(msg);
            throw new IllegalStateException(msg, e);
        }
        return encodedKey;
    }

}
