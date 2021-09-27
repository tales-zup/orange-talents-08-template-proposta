package com.zup.propostaservice.crypto;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CryptoService {

    @Value("${chave-criptografia-base}")
    private String chaveCriptografia;

    private BasicTextEncryptor encryptor;

    public CryptoService() {
        this.encryptor = new BasicTextEncryptor();
    }

    @PostConstruct
    public void setarChave() {
        encryptor.setPasswordCharArray(chaveCriptografia.toCharArray());
    }

    public String criptografar(String texto) {
        return encryptor.encrypt(texto);
    }

    public String descriptografar(String texto) {
        return encryptor.decrypt(texto);
    }

}
