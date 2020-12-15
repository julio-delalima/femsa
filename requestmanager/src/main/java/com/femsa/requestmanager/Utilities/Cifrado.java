package com.femsa.requestmanager.Utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cifrado {

    /***
     * Función para genera un hash en formato SHA-256.
     * Lo encontré aquí https://stackoverflow.com/questions/10129311/does-every-android-phone-support-sha-256 :3
     * @param cadena el string de entrada que se quiere cifrar.
     * @return el hash generado a partir d elos parámetros de entrada.
     * */
    public static String SHA256(String cadena)
        {
            MessageDigest digest;
            String hash;
            try
                {
                    digest = MessageDigest.getInstance("SHA-256");
                    digest.update(cadena.getBytes());
                    hash = bytesToHexString(digest.digest());
                    return hash;
                }
            catch (NoSuchAlgorithmException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return null;
                }
        }

    private static String bytesToHexString(byte[] bytes)
        {
            // http://stackoverflow.com/questions/332079
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes)
                {
                    String hex = Integer.toHexString(0xFF & aByte);
                    if (hex.length() == 1)
                        {
                            sb.append('0');
                        }
                    sb.append(hex);
                }
            return sb.toString();
        }
}
