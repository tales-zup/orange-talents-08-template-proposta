package com.zup.propostaservice.biometria;

import com.zup.propostaservice.cartao.Cartao;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import java.util.Base64;

public class BiometriaRequest {

    @NotBlank
    private String fingerprint;

    public Biometria toModel(Cartao cartao) {
        return new Biometria(fingerprint, cartao);
    }

    public void validacaoBiometria(String fingerprint) {
        try {
            byte[] fingerprintByte = Base64.getDecoder().decode(fingerprint);
            String teste = Base64.getEncoder().encodeToString(fingerprintByte);
            Assert.isTrue(teste.equals(this.fingerprint), "Base64 Inválida");
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fingerprint cadastrado é inválido!");
        }
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
