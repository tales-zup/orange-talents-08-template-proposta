package com.zup.propostaservice.feign.contas;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "contas", url = "${feign.client.contas.url}")
public interface ContasApi {

    @GetMapping("/cartoes")
    ConsultaCartoesResponse consultarCartoes(@RequestParam("idProposta") Long idProposta);

    @PostMapping("/cartoes/{id}/bloqueios")
    void bloquearCartao(@PathVariable("id") String id, @RequestBody BloquearCartaoRequest request);

    @PostMapping("/cartoes/{id}/avisos")
    AvisoViagemApiContaResponse avisarViagem(@PathVariable("id") String id, AvisoViagemApiContaRequest request);

    @PostMapping("/cartoes/{id}/carteiras")
    CadastroCarteiraApiContaResponse cadastrarCarteira(@PathVariable("id") String id, CadastroCarteiraApiContaRequest request);

}
