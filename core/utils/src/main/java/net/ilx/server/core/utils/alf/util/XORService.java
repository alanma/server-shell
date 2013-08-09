package net.ilx.server.core.utils.alf.util;

import org.apache.commons.codec.binary.Base64;

/**
* @author Andreja Josipovic
*
 */
public class XORService {

    /**
     * Encodes a string
     *
     * @param data
     *            Data to encode
     * @param key
     *            Key to encode with
     *
     * @return base64 encoded data
     */
    public static String xorEncode(String data, String key) {

                byte byteData[] = data.getBytes();
                byte byteKey[] = key.getBytes();

                int keyPointer = 0;
                for (int i = 0; i < byteData.length; i++) {
                    byteData[i] ^= byteKey[keyPointer];
                    keyPointer += byteData[i];
                    keyPointer %= byteKey.length;
                }

                return Base64.encodeBase64URLSafeString(byteData);

    }

    /**
     * Decodes a string
     *
     * @param data
     *            Base64 encoded data to decode
     * @param key
     *            Key to decode with
     */
    public static String xorDecode(String data, String key) {

                byte byteData[] = Base64.decodeBase64(data);
                byte byteKey[] = key.getBytes();

                // This was a little interesting to code, because by the time we
                // increase the keyPointer, what we have to increase
                // it by is already destroyed by the line above it. Therefore, we have
                // to set keyPointerAdd before we decrypt the
                // byte that holds what's added to the pointer.
                int keyPointer = 0;
                byte keyPointerAdd = 0;

                for (int i = 0; i < byteData.length; i++) {
                    keyPointerAdd = byteData[i];
                    byteData[i] ^= byteKey[keyPointer];
                    keyPointer += keyPointerAdd;
                    keyPointer %= byteKey.length;
                }

                return new String(byteData);
    }

}
