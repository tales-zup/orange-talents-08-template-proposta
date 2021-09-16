package com.zup.propostaservice.feign.contas;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "contas", url = "${feign.client.contas.url}")
public interface ContasApi {

    @GetMapping("/cartoes")
    ConsultaCartoesResponse consultarCartoes(@RequestParam("idProposta") Long idProposta);

}
