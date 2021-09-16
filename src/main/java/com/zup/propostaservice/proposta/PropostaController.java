package com.zup.propostaservice.proposta;

import com.zup.propostaservice.feign.analisefinanceira.AnaliseFinanceiraApi;
import com.zup.propostaservice.feign.analisefinanceira.AnaliseFinanceiraRequest;
import com.zup.propostaservice.feign.analisefinanceira.AnaliseFinanceiraResponse;
import com.zup.propostaservice.feign.contas.ConsultaCartoesResponse;
import com.zup.propostaservice.feign.contas.ContasApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private AnaliseFinanceiraApi analiseFinanceiraApi;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarProposta(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder) {
        Proposta proposta = request.toModel();
        proposta = propostaRepository.save(proposta);

        AnaliseFinanceiraResponse analiseFinanceiraResponse = analiseFinanceiraApi.consultaDadosCliente(
                new AnaliseFinanceiraRequest(proposta.getDocumento(), proposta.getNome(), proposta.getId()));
        proposta.setStatusProposta(StatusProposta.converter(analiseFinanceiraResponse.getResultadoSolicitacao()));
        proposta = propostaRepository.save(proposta);

        URI uri = uriBuilder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        PropostaDto dto = new PropostaDto(proposta);
        return ResponseEntity.created(uri).body(dto);
    }

}
