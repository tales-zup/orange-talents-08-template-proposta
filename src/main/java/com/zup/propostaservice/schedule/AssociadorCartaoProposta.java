package com.zup.propostaservice.schedule;

import com.zup.propostaservice.cartao.Cartao;
import com.zup.propostaservice.cartao.CartaoRepository;
import com.zup.propostaservice.feign.contas.ConsultaCartoesResponse;
import com.zup.propostaservice.feign.contas.ContasApi;
import com.zup.propostaservice.proposta.Proposta;
import com.zup.propostaservice.proposta.PropostaRepository;
import com.zup.propostaservice.proposta.StatusProposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@ConditionalOnProperty(value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true)
public class AssociadorCartaoProposta {

    @Autowired
    private ContasApi contasApi;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    private final long SEGUNDO = 1000;
    private final long MINUTO = SEGUNDO * 60;
    private final long MEIO_MINUTO = MINUTO / 2;

    @Scheduled(fixedDelay = 10000)
    public void associarCartoesComProposta() {

        System.out.println("INICIANDO SCHEDULER ASSOCIADOR CARTOES PROPOSTA");
        List<Proposta> propostas = propostaRepository.findByStatusPropostaAndNumeroCartao(StatusProposta.ELEGIVEL, null);

        try {
            for(Proposta proposta : propostas) {
                ConsultaCartoesResponse consultaCartoesResponse = contasApi.consultarCartoes(proposta.getId());
                Cartao cartao = consultaCartoesResponse.toModel(propostaRepository);
                proposta.setNumeroCartao(consultaCartoesResponse.getId());
                cartao.setProposta(proposta);
                propostaRepository.save(proposta);
                cartaoRepository.save(cartao);
            }
            System.out.println("FINALIZANDO SCHEDULER ASSOCIADOR CARTOES PROPOSTA");
            System.out.println("FORAM PROCESSADAS " + propostas.size() + " PROPOSTAS");
        } catch (Exception e) {
            System.out.println("ERRO NO ASSOCIAMENTO DE PROPOSTAS COM CARTAO");
            System.out.println(e.getMessage());
        }

    }

}
