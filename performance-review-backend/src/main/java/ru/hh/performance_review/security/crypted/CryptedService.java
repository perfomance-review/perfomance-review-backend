package ru.hh.performance_review.security.crypted;

public interface CryptedService {

    String encryptedPasswordProcess(String password);

    String decryptedPasswordProcess(String password);
}
