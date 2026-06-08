package br.com.espaco_verde.configuration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class DataCryptoConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES";

    private static final byte[] KEY;

    static {
        String keyBase64 = System.getenv("CRYPTO_KEY");
        if(keyBase64 == null || keyBase64.isBlank()){
            throw new IllegalStateException("ERRO: A variável de ambiente CRYPTO_KEY não esta configurada");
        }
        KEY = Base64.getDecoder().decode(keyBase64);
    }

    @Override
    public String convertToDatabaseColumn(String text){
        if(text == null) return null;
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(KEY, ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar a mensage ", e
            );
        }
    }

    @Override
    public String convertToEntityAttribute(String text) {
        if(text ==null) return null;

        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(KEY, ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
        } catch (Exception e){
            return text;
        }
    }

}
