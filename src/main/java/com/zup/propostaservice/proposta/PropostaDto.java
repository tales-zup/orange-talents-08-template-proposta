package com.zup.propostaservice.proposta;

import java.math.BigDecimal;

public class PropostaDto {

    private Long id;
    private String documento;
    private String email;
    private String nome;
    private String endereco;
    private BigDecimal salario;

    public PropostaDto() {
    }

    public PropostaDto(Proposta proposta) {
        this.id = proposta.getId();
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.endereco = proposta.getEndereco();
        this.salario = proposta.getSalario();
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}
