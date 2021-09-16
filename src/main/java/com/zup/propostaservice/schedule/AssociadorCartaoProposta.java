package com.zup.propostaservice.schedule;

import com.zup.propostaservice.feign.contas.ConsultaCartoesResponse;
import com.zup.propostaservice.feign.contas.ContasApi;
import com.zup.propostaservice.proposta.Proposta;
import com.zup.propostaservice.proposta.PropostaRepository;
import com.zup.propostaservice.proposta.StatusProposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class AssociadorCartaoProposta {

    @Autowired
    private ContasApi contasApi;

    @Autowired
    private PropostaRepository propostaRepository;

    private final long SEGUNDO = 1000;
    private final long MINUTO = SEGUNDO * 60;
    private final long MEIO_MINUTO = MINUTO / 2;

    @Scheduled(fixedDelay = MEIO_MINUTO)
    public void associarCartoesComProposta() {

        List<Proposta> propostas = propostaRepository.findByStatusPropostaAndNumeroCartao(StatusProposta.ELEGIVEL, null);

        for(Proposta proposta : propostas) {
            ConsultaCartoesResponse consultaCartoesResponse = contasApi.consultarCartoes(proposta.getId());
            proposta.setNumeroCartao(consultaCartoesResponse.getId());
            propostaRepository.save(proposta);
        }

    }

}
