package br.com.gerencianet.gnsdk.lib;

import android.util.Base64;

import org.spongycastle.crypto.AsymmetricBlockCipher;
import org.spongycastle.crypto.engines.RSAEngine;
import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.util.PublicKeyFactory;

public class RSA {

    public static String encrypt(String data, String strKey) throws Exception {
        strKey = strKey.replace("-----BEGIN PUBLIC KEY-----\n", "");
        strKey = strKey.replace("-----END PUBLIC KEY-----", "");

        AsymmetricKeyParameter publicKey = PublicKeyFactory.createKey(Base64.decode(strKey, Base64.NO_WRAP));
        AsymmetricBlockCipher e = new RSAEngine();

        e = new org.spongycastle.crypto.encodings.PKCS1Encoding(e);
        e.init(true, publicKey);

        byte[] messageBytes = data.getBytes();
        byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);
        return new String(Base64.encodeToString(hexEncodedCipher, Base64.NO_WRAP));
    }

}
