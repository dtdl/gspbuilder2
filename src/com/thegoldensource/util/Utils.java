package com.thegoldensource.util;

import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

/**
 * @author mkopp
 * @since Jul 27, 2004 3:30:53 PM
 */
public class Utils
{
    private static final Logger logger = Logger.getLogger(Utils.class);

    public static String GetValueBetweenSeparators(String msg, int position, char separator)
    {
        int start = 0;
        int end = msg.indexOf(separator);
        int j;
        for (j = 0; j < position && end != -1; ++j)
        {
            start = end + 1;
            end = msg.indexOf(separator, start);
        }
        final String value;
        if (j != position)
            value = null;
        else if (end == -1)
        {
            if (msg.length() == 0)
                value = null;
            else
                value = msg.substring(start);
        }
        else
            value = msg.substring(start, end);
        return value;
    }

    /**
     * Convert a byte array to a hex-string representation for debugging output.
     * 
     * @param b The byte array to convert
     * 
     * @return The string-representation of the byte array.
     */
    static public String ToHex(final byte[] b)
    {
        if (b == null)
        {
            return null;
        }
        final String hits = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder();

        for (final byte element : b)
        {
            final int j = element & 0xFF;

            final char first = hits.charAt(j / 16);
            final char second = hits.charAt(j % 16);

            sb.append(first);
            sb.append(second);
        }
        return sb.toString();
    }

    /**
     * Converts from a hex-string to string. *
     * 
     * @param base The hex string to convert.
     * 
     * @return A ASCII string.
     * 
     * @throws IllegalArgumentException If the input-string is not correctly formatted.
     */
    public static String hexToString(final String base)
    {
        if (base == null)
        {
            return null;
        }
        else if ((base.length() % 2) != 0)
        {
            throw new IllegalArgumentException("Hex String length must be a multiple of 2.");
        }
        else
        {
            try
            {
                final StringBuilder buffer = new StringBuilder(20);
                final int baseLen = base.length();
                for (int x = 0; (x + 2) <= baseLen; x += 2)
                {
                    buffer.append((char) Integer.parseInt(base.substring(x, x + 2), 16));
                }
                return buffer.toString();
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("The provided base string contains invalid characters " + base);
            }
            catch (ClassCastException e)
            {
                throw new IllegalArgumentException("The provided base string contains invalid characters " + base);
            }
        }
    }

    /**
     * Converts a string to a hex string. *
     * 
     * @param base The string to convert.
     * 
     * @return A HEX string.
     */
    public static String stringToHex(final String base)
    {
        if (base == null)
        {
            return null;
        }
        else
        {
            final StringBuilder buffer = new StringBuilder(20);
            final int baseLen = base.length();
            for (int x = 0; x < baseLen; x++)
            {
                buffer.append(Integer.toHexString(base.charAt(x)));
            }
            return buffer.toString();
        }
    }

    /**
     * Converts from a hex-string to a byte array. The input string is normally generated using the ToHex() method. *
     * 
     * @param hex A string that contains a representation of the byte-array.
     * @return A byte-array with the contents of the String.
     * @throws IllegalArgumentException If the input-string is not correctly formatted.
     */
    static public byte[] FromHex(final String hex)
    {
        if (hex == null)
        {
            return null;
        }
        else if ((hex.length() % 2) != 0)
        {
            throw new IllegalArgumentException("Hex String length must be a multiple of 2.");
        }

        final int length = hex.length() / 2;
        final byte[] result = new byte[length];
        final String hits = "0123456789ABCDEF";
        final String h = hex.toUpperCase();

        for (int i = 0; i < length; i++)
        {
            char c = h.charAt(2 * i);
            int index = hits.indexOf(c);
            if (index == -1)
            {
                throw new IllegalArgumentException("Hex String can't contain '" + c + "'");
            }

            int j = 16 * index;
            c = h.charAt((2 * i) + 1);
            index = hits.indexOf(c);
            if (index == -1)
            {
                throw new IllegalArgumentException("Hex String can't contain '" + c + "'");
            }

            j += index;
            result[i] = (byte) (j & 0xFF);
        }
        return result;
    }

    public static String EncryptPassword(String password)
    {
        if (password == null)
            return "";
        try
        {
//            if (password.length() % 8 != 0)
//            {
//                password = StringUtils.rightPad(password, password.length() + 8 - (password.length() % 8), " ");
//            }
//          start
            int dis = password.length() % 8;
        	if (dis != 0)
            {
        		String pad = "";
        		for (int i = 0; i < 8 - dis; i++ ) {
        			pad += " "; 
        		}
                password = password + pad;
            }
//            end
            final byte[] keyByteArr = Utils.FromHex("2E1E66F8F3541281");
            final KeySpec spec = new DESKeySpec(keyByteArr);
            final SecretKeyFactory desFactory = SecretKeyFactory.getInstance("DES");
            final javax.crypto.SecretKey DES_key = desFactory.generateSecret(spec);
            final javax.crypto.Cipher encrypter = javax.crypto.Cipher.getInstance("DES");
            encrypter.init(javax.crypto.Cipher.ENCRYPT_MODE, DES_key);
            final byte[] encrypt_bytes = encrypter.doFinal(password.getBytes());
            final String encrypted = "Encrypted::" + Utils.ToHex(encrypt_bytes);
            return encrypted;
        }
        catch (final Exception e)
        {
            logger.error("An exception occurred during encryption of a password!", e);
            return password;
        }

    }

    public static String DecryptPassword(String encryptpassword)
    {
        if (encryptpassword == null)
            return "";
        try
        {
            byte[] decrypt_bytes;
            final byte[] keyByteArr = Utils.FromHex("2E1E66F8F3541281");
            final KeySpec spec = new DESKeySpec(keyByteArr);
            SecretKeyFactory desFactory = SecretKeyFactory.getInstance("DES");
            javax.crypto.SecretKey DES_key = desFactory.generateSecret(spec);
            javax.crypto.Cipher decrypter = javax.crypto.Cipher.getInstance("DES");
            decrypter.init(javax.crypto.Cipher.DECRYPT_MODE, DES_key);
            if (encryptpassword.contains("Encrypted::"))
                decrypt_bytes = decrypter.doFinal(Utils.FromHex(encryptpassword.substring(11))); // select after
                                                                                                 // "Encrypted::"
            else
                decrypt_bytes = decrypter.doFinal(Utils.FromHex(encryptpassword));
            final String decryptPassword = new String(decrypt_bytes);
            return decryptPassword;
        }
        catch (final Exception e)
        {
            logger.error("An exception occurred during decryption of a password!", e);
            return encryptpassword;
        }

    }

	public static void main(String[] args) {
//		String pass = "Gsource@789       ";
//		System.out.println(pass.length());
//		System.out.println("Gsource@789: " + Utils.EncryptPassword(pass));
//		
//		String dpass = Utils.DecryptPassword(Utils.EncryptPassword(pass));
//		System.out.println(dpass.length());
//		System.out.println("dpass      : " + Utils.EncryptPassword(dpass));
//		
	}
    
}

