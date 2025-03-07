package com.zup.propostaservice.cartao;

import com.zup.propostaservice.avisodeviagem.AvisoDeViagem;
import com.zup.propostaservice.avisodeviagem.AvisoDeViagemDto;
import com.zup.propostaservice.avisodeviagem.AvisoDeViagemRepository;
import com.zup.propostaservice.avisodeviagem.AvisoDeViagemRequest;
import com.zup.propostaservice.biometria.Biometria;
import com.zup.propostaservice.biometria.BiometriaDto;
import com.zup.propostaservice.biometria.BiometriaRepository;
import com.zup.propostaservice.biometria.BiometriaRequest;
import com.zup.propostaservice.carteira.Carteira;
import com.zup.propostaservice.carteira.CarteiraDto;
import com.zup.propostaservice.carteira.CarteiraRepository;
import com.zup.propostaservice.carteira.CarteiraRequest;
import com.zup.propostaservice.feign.contas.AvisoViagemApiContaRequest;
import com.zup.propostaservice.feign.contas.BloquearCartaoRequest;
import com.zup.propostaservice.feign.contas.CadastroCarteiraApiContaRequest;
import com.zup.propostaservice.feign.contas.ContasApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private BiometriaRepository biometriaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BloqueioCartaoRepository bloqueioCartaoRepository;

    @Autowired
    private AvisoDeViagemRepository avisoDeViagemRepository;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private ContasApi contasApi;

    @PostMapping("/{id}/cadastrar-biometria")
    public ResponseEntity<BiometriaDto> cadastrarBiometriaNoCartao(
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

    @PostMapping("/{id}/bloquear")
    @Transactional
    public BloqueioCartaoDto cadastrarBloqueioCartao(@PathVariable("id") String idCartao, HttpServletRequest request) {

        Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(
                () -> new EntityNotFoundException("Esse cartão não existe."));

        if(!bloqueioCartaoRepository.findByAtivoAndCartao_Id(true, idCartao).isEmpty()) {
            throw new IllegalArgumentException("Esse cartão já está bloqueado.");
        }

        contasApi.bloquearCartao(idCartao, new BloquearCartaoRequest("proposta-service"));
        cartao.setStatusCartao(StatusCartao.BLOQUEADO);
        cartaoRepository.save(cartao);

        BloqueioCartao bloqueioCartao = new BloqueioCartao(cartao, true, request.getRemoteAddr(), request.getHeader("User-Agent"));
        bloqueioCartaoRepository.save(bloqueioCartao);
        return new BloqueioCartaoDto(bloqueioCartao);
    }

    @PostMapping("/{id}/aviso-de-viagem")
    public AvisoDeViagemDto cadastrarAvisoDeViagem(
            @PathVariable("id") String idCartao,
            @RequestBody @Valid AvisoDeViagemRequest avisoDeViagemRequest,
            HttpServletRequest request) {

        Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(
                () -> new EntityNotFoundException("Esse cartão não existe."));

        AvisoDeViagem avisoDeViagem = avisoDeViagemRequest
                .toModel(cartao, request.getRemoteAddr(), request.getHeader("User-Agent"));

        contasApi.avisarViagem(idCartao,
                new AvisoViagemApiContaRequest(avisoDeViagem.getDestino(), avisoDeViagem.getDataTermino()));

        avisoDeViagem = avisoDeViagemRepository.save(avisoDeViagem);
        return new AvisoDeViagemDto(avisoDeViagem);

    }

    @PostMapping("{id}/cadastrar-carteira")
    public ResponseEntity<CarteiraDto> cadastrarCarteira(
            @PathVariable("id") String idCartao,
            @RequestBody @Valid CarteiraRequest request,
            UriComponentsBuilder uriBuilder) {

        Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(
                () -> new EntityNotFoundException("Esse cartão não existe."));

        if(carteiraRepository.existsByCarteiraAndCartao_Id(request.getCarteira(), idCartao)){
            throw new IllegalArgumentException("Esse cartão já é associado a essa carteira.");
        }

        contasApi.cadastrarCarteira(idCartao,
                new CadastroCarteiraApiContaRequest(request.getEmail(), request.getCarteira()));

        Carteira carteira = request.toModel(cartao);
        carteira = carteiraRepository.save(carteira);

        URI uri = uriBuilder.path("/carteiras/{id}").buildAndExpand(carteira.getId()).toUri();
        CarteiraDto dto = new CarteiraDto(carteira);
        return ResponseEntity.created(uri).body(dto);

    }

}
