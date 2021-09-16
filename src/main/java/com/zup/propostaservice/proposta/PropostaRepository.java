package com.zup.propostaservice.proposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    List<Proposta> findByStatusPropostaAndNumeroCartao(StatusProposta statusProposta, String numeroCartao);

}
