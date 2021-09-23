package com.zup.propostaservice.cartao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.propostaservice.biometria.BiometriaRequest;
import com.zup.propostaservice.feign.contas.ConsultaCartoesResponse;
import com.zup.propostaservice.feign.contas.ContasApi;
import com.zup.propostaservice.proposta.Proposta;
import com.zup.propostaservice.proposta.PropostaRepository;
import com.zup.propostaservice.proposta.PropostaRequest;
import com.zup.propostaservice.schedule.AssociadorCartaoProposta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CartaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContasApi contasApi;

//    @Autowired
//    private AssociadorCartaoProposta associadorCartaoProposta;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BloqueioCartaoRepository bloqueioCartaoRepository;

    // Falta fazer funcionar o autowired de AssociadorCartaoProposta
    // teste ainda não funciona
    @Test
    public void deveriaCadastrarBiometriaComSucesso() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propostaRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

//        associadorCartaoProposta.associarCartoesComProposta();

        ConsultaCartoesResponse response = contasApi.consultarCartoes(1L);
        BiometriaRequest biometriaRequest = new BiometriaRequest();
        biometriaRequest.setFingerprint("dGV4dG9hbG93d3d3");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + response.getId() + "/cadastrar-biometria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(biometriaRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fingerprint").value("dGV4dG9hbG93d3d3"))
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/biometrias/{id}"));
    }

    // Falta fazer funcionar o autowired de AssociadorCartaoProposta
    // teste ainda não funciona
    @Test
    public void deveriaCadastrarBloqueioCartao() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propostaRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

//        associadorCartaoProposta.associarCartoesComProposta();
        ConsultaCartoesResponse response = contasApi.consultarCartoes(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + response.getId() + "/bloquear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCartao").value(response.getId()))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    public void naoDeveriaBloquearCartaoQueJaEstaBloqueado() throws Exception {
        Proposta proposta = new Proposta("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));
        proposta = propostaRepository.save(proposta);

        Cartao cartao = new Cartao("1234-5678-1234-5678", LocalDateTime.now(), "Tales Araujo", new BigDecimal(500), proposta);
        cartaoRepository.save(cartao);

        BloqueioCartao bloqueioCartao = new BloqueioCartao(cartao, true, "127.0.0.1", "Spring Teste");
        bloqueioCartaoRepository.save(bloqueioCartao);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/bloquear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Esse cartão já está bloqueado."));
    }

    // Falta fazer funcionar o autowired de AssociadorCartaoProposta
    // teste ainda não funciona
    @Test
    public void deveriaBloquearCartaoQueTemApenasUmBloqueioMasNaoEstaAtivo() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propostaRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //        associadorCartaoProposta.associarCartoesComProposta();
        ConsultaCartoesResponse response = contasApi.consultarCartoes(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + response.getId() + "/bloquear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCartao").value(response.getId()))
                .andExpect(jsonPath("$.ativo"). value(true));

        BloqueioCartao bloqueioCartao = bloqueioCartaoRepository.findById(1L).orElseThrow(
                () -> new EntityNotFoundException("Erro no teste"));
        bloqueioCartao.liberarBloqueio();
        bloqueioCartaoRepository.save(bloqueioCartao);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + response.getId() + "/bloquear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCartao").value(response.getId()))
                .andExpect(jsonPath("$.ativo"). value(true));
    }

}