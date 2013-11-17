package com.aknow.saboom.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;


public class KeyUtil {

    private static final String METHOD = "RSA";
    private static final String RANDOMIZE = "SHA1PRNG";


    public static ArrayList<Object> makeKeySet() throws NoSuchAlgorithmException {

        KeyPairGenerator generator = null;
        SecureRandom random = null;

        // generate key-pair
        generator = KeyPairGenerator.getInstance(METHOD);
        random = SecureRandom.getInstance(RANDOMIZE);

        generator.initialize(1024, random);
        KeyPair keyPair = generator.generateKeyPair();

        // get private-key and public-key
        byte[] privateKeyBinaryData = keyPair.getPrivate().getEncoded();
        PublicKey publicKey = keyPair.getPublic();

        //set to returnList
        ArrayList<Object> returnList = new ArrayList<Object>();
        returnList.add(0, privateKeyBinaryData);
        returnList.add(1, publicKey);

        return returnList;
    }
}
