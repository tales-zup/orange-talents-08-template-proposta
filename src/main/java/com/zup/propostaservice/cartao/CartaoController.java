package com.zup.propostaservice.cartao;

import com.zup.propostaservice.biometria.Biometria;
import com.zup.propostaservice.biometria.BiometriaDto;
import com.zup.propostaservice.biometria.BiometriaRepository;
import com.zup.propostaservice.biometria.BiometriaRequest;
import com.zup.propostaservice.proposta.PropostaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private BiometriaRepository biometriaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @PostMapping("/{id}/cadastrar-biometria")
    public ResponseEntity cadastrarBiometriaNoCartao(
            @PathVariable("id") String idCartao,
            @RequestBody @Valid BiometriaRequest request,
            UriComponentsBuilder uriBuilder) {

        Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(
                () -> new EntityNotFoundException("Esse cartão não existe."));

        Biometria biometria = request.toModel(cartao);
        request.validacaoBiometria(biometria.getFingerprint());
        biometriaRepository.save(biometria);

        URI uri = uriBuilder.path("/biometrias/{id}").buildAndExpand(biometria.getId()).toUri();
        BiometriaDto dto = new BiometriaDto(biometria);
        return ResponseEntity.created(uri).body(dto);
    }

}
