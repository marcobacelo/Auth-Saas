package com.auth.saas.domain.crypto;

public interface PasswordHasher {

    String hash(char[] rawPassword);

    boolean matches(char[] rawPassword, String encodedPassword);

    boolean needsRehash(String encodedPassword);
}
