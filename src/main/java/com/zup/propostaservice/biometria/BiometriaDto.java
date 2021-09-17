package com.zup.propostaservice.biometria;

import java.time.LocalDate;

public class BiometriaDto {

    private Long id;
    private String fingerprint;
    private String idCartao;
    private LocalDate dataCadastro;

    public BiometriaDto(Biometria biometria) {
        this.id = biometria.getId();
        this.fingerprint = biometria.getFingerprint();
        this.idCartao = biometria.getCartao().getId();
        this.dataCadastro = biometria.getDataCadastro();
    }

    public Long getId() {
        return id;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getIdCartao() {
        return idCartao;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }
}
