package com.zup.propostaservice.feign.contas;

import com.zup.propostaservice.cartao.Cartao;
import com.zup.propostaservice.proposta.Proposta;
import com.zup.propostaservice.proposta.PropostaRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ConsultaCartoesResponse {

    private String id;
    private LocalDateTime emitidoEm;
    private String titular;
    private List<BloqueiosCartaoResponse> bloqueios;
    private List<AvisoCartaoResponse> avisos;
    private List<CarteiraCartaoResponse> carteiras;
    private List<ParcelasCartaoResponse> parcelas;
    private BigDecimal limite;
    private RenegociacaoCartaoResponse renegociacao;
    private Long idProposta;

    public String getId() {
        return id;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public String getTitular() {
        return titular;
    }

    public List<BloqueiosCartaoResponse> getBloqueios() {
        return bloqueios;
    }

    public List<AvisoCartaoResponse> getAvisos() {
        return avisos;
    }

    public List<CarteiraCartaoResponse> getCarteiras() {
        return carteiras;
    }

    public List<ParcelasCartaoResponse> getParcelas() {
        return parcelas;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public RenegociacaoCartaoResponse getRenegociacao() {
        return renegociacao;
    }

    public Long getIdProposta() {
        return idProposta;
    }

    public Cartao toModel(PropostaRepository propostaRepository) {
        Proposta proposta = propostaRepository.findById(idProposta).orElseThrow(
                () -> new EntityNotFoundException("Essa proposta não existe."));
        return new Cartao(id, emitidoEm, titular, limite, proposta);
    }
}
