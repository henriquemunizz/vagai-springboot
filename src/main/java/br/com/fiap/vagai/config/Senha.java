package br.com.fiap.vagai.config;

import java.security.*;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Senha {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String gerar(String senha) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        var base = Base64.getEncoder();
        return base.encodeToString(salt) + ":" + base.encodeToString(calcular(senha, salt));
    }

    public static boolean conferir(String senha, String armazenada) {
        String[] partes = armazenada.split(":");
        var base = Base64.getDecoder();
        return MessageDigest.isEqual(base.decode(partes[1]), calcular(senha, base.decode(partes[0])));
    }

    private static byte[] calcular(String senha, byte[] salt) {
        PBEKeySpec chave = new PBEKeySpec(senha.toCharArray(), salt, 600000, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(chave).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 indisponível", e);
        } finally {
            chave.clearPassword();
        }
    }
}
