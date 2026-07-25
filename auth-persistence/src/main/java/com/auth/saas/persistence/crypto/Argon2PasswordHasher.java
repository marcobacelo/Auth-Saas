package com.auth.saas.persistence.crypto;

import com.auth.saas.domain.crypto.PasswordHasher;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Component
public class Argon2PasswordHasher implements PasswordHasher {

    private static final String PREFIX = "$argon2id$";
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;

    private final int memoryKb;
    private final int iterations;
    private final int parallelism;
    private final SecureRandom secureRandom = new SecureRandom();

    public Argon2PasswordHasher(
            @Value("${auth.crypto.argon2.memory-kb:65536}") int memoryKb,
            @Value("${auth.crypto.argon2.iterations:3}") int iterations,
            @Value("${auth.crypto.argon2.parallelism:1}") int parallelism) {
        this.memoryKb = memoryKb;
        this.iterations = iterations;
        this.parallelism = parallelism;
    }

    @Override
    public String hash(char[] rawPassword) {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        byte[] hash = generate(rawPassword, salt, memoryKb, iterations, parallelism);
        try {
            return PREFIX + "v=19$m=" + memoryKb + ",t=" + iterations + ",p=" + parallelism
                    + "$" + Base64.getEncoder().withoutPadding().encodeToString(salt)
                    + "$" + Base64.getEncoder().withoutPadding().encodeToString(hash);
        } finally {
            Arrays.fill(hash, (byte) 0);
            Arrays.fill(rawPassword, '\0');
        }
    }

    @Override
    public boolean matches(char[] rawPassword, String encodedPassword) {
        if (encodedPassword == null || !encodedPassword.startsWith(PREFIX)) {
            Arrays.fill(rawPassword, '\0');
            return false;
        }
        try {
            ParsedHash parsed = ParsedHash.parse(encodedPassword);
            byte[] actual = generate(rawPassword, parsed.salt(), parsed.memoryKb(), parsed.iterations(), parsed.parallelism());
            boolean ok = constantTimeEquals(actual, parsed.hash());
            Arrays.fill(actual, (byte) 0);
            return ok;
        } catch (RuntimeException ex) {
            return false;
        } finally {
            Arrays.fill(rawPassword, '\0');
        }
    }

    @Override
    public boolean needsRehash(String encodedPassword) {
        if (encodedPassword == null || !encodedPassword.startsWith(PREFIX)) {
            return true;
        }
        ParsedHash parsed = ParsedHash.parse(encodedPassword);
        return parsed.memoryKb() != memoryKb
                || parsed.iterations() != iterations
                || parsed.parallelism() != parallelism;
    }

    private byte[] generate(char[] rawPassword, byte[] salt, int memoryKb, int iterations, int parallelism) {
        byte[] passwordBytes = new String(rawPassword).getBytes(StandardCharsets.UTF_8);
        try {
            Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                    .withSalt(salt)
                    .withMemoryAsKB(memoryKb)
                    .withIterations(iterations)
                    .withParallelism(parallelism)
                    .build();
            Argon2BytesGenerator generator = new Argon2BytesGenerator();
            generator.init(params);
            byte[] out = new byte[HASH_LENGTH];
            generator.generateBytes(passwordBytes, out);
            return out;
        } finally {
            Arrays.fill(passwordBytes, (byte) 0);
        }
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    private record ParsedHash(int memoryKb, int iterations, int parallelism, byte[] salt, byte[] hash) {

        static ParsedHash parse(String encoded) {
            // $argon2id$v=19$m=65536,t=3,p=1$salt$hash
            String[] parts = encoded.split("\\$");
            if (parts.length != 6) {
                throw new IllegalArgumentException("invalid argon2 hash encoding");
            }
            String[] params = parts[3].split(",");
            int memory = Integer.parseInt(params[0].substring(2));
            int iterations = Integer.parseInt(params[1].substring(2));
            int parallelism = Integer.parseInt(params[2].substring(2));
            byte[] salt = Base64.getDecoder().decode(parts[4]);
            byte[] hash = Base64.getDecoder().decode(parts[5]);
            return new ParsedHash(memory, iterations, parallelism, salt, hash);
        }
    }
}
