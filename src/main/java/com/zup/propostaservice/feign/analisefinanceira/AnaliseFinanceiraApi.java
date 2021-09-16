package com.zup.propostaservice.feign.analisefinanceira;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "analise-financeira", url = "${feign.client.analise-financeira.url}")
public interface AnaliseFinanceiraApi {

    @PostMapping("/solicitacao")
    AnaliseFinanceiraResponse consultaDadosCliente(@RequestBody AnaliseFinanceiraRequest request);

}
