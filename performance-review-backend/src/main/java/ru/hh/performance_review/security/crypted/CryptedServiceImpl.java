package ru.hh.performance_review.security.crypted;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@RequiredArgsConstructor
public class CryptedServiceImpl implements CryptedService {

    private final static String ALGORITHM = "AES";
    //ни в коем случае не изменять, иначе не раскодировать пароли из БД
    private static final String KEY = "E1C89D86DC5053368AC1FD0BEDCDA1DA";

    @SneakyThrows
    @Override
    public String encryptedPasswordProcess(String password) {
        byte[] keyBytes = hexStringToByteArray(KEY);
        SecretKeySpec sks = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(password.getBytes());

        return byteArrayToHexString(encrypted);
    }

    @SneakyThrows
    @Override
    public String decryptedPasswordProcess(String password) {
        byte[] keyBytes = hexStringToByteArray(KEY);
        SecretKeySpec sks = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(password));

        return new String(decrypted);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte value : b) {
            int v = value & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

}
