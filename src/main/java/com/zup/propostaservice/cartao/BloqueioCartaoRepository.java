package com.zup.propostaservice.cartao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloqueioCartaoRepository extends JpaRepository<BloqueioCartao, Long> {

    List<BloqueioCartao> findByAtivoAndCartao_Id(boolean ativo, String idCartao);

}
