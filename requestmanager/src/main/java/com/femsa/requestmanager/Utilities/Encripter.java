package com.femsa.requestmanager.Utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encripter {

    public static final String getMD5Hash(final String plainText) {
        final String MD5 = "MD5";
        try
            {
                // Create MD5 Hash
                MessageDigest digest = MessageDigest.getInstance(MD5);
                digest.update(plainText.getBytes());
                byte[] messageDigest = digest.digest();

                // Create Hex String
                StringBuilder hexString = new StringBuilder();
                for (byte aMessageDigest : messageDigest)
                    {
                        String h = Integer.toHexString(0xFF & aMessageDigest);
                        while (h.length() < 2)
                            h = "0" + h;
                        hexString.append(h);
                    }
                return hexString.toString();

            }
        catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
        return "";
    }
}
