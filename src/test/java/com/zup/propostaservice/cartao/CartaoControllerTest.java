package com.zup.propostaservice.cartao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.propostaservice.avisodeviagem.AvisoDeViagemRequest;
import com.zup.propostaservice.biometria.BiometriaRequest;
import com.zup.propostaservice.carteira.Carteira;
import com.zup.propostaservice.carteira.CarteiraEnum;
import com.zup.propostaservice.carteira.CarteiraRepository;
import com.zup.propostaservice.carteira.CarteiraRequest;
import com.zup.propostaservice.feign.contas.ConsultaCartoesResponse;
import com.zup.propostaservice.feign.contas.ContasApi;
import com.zup.propostaservice.proposta.Proposta;
import com.zup.propostaservice.proposta.PropostaRepository;
import com.zup.propostaservice.proposta.PropostaRequest;
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
import java.time.LocalDate;
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

    @Autowired
    private CarteiraRepository carteiraRepository;

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

    // Falta fazer funcionar o autowired de AssociadorCartaoProposta
    // teste ainda não funciona
    @Test
    public void deveriaCadastrarAvisoDeViagem() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propostaRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //        associadorCartaoProposta.associarCartoesComProposta();
        ConsultaCartoesResponse response = contasApi.consultarCartoes(1L);

        AvisoDeViagemRequest avisoDeViagemRequest = new AvisoDeViagemRequest("Paris", LocalDate.now().plusDays(30));

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + response.getId() + "aviso-de-viagem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(avisoDeViagemRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCartao").value(response.getId()))
                .andExpect(jsonPath("$.destino").value("Paris"));

    }

    // Falta fazer funcionar o autowired de AssociadorCartaoProposta
    // teste ainda não funciona
    @Test
    public void deveriaCadastrarCarteira() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(propostaRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //        associadorCartaoProposta.associarCartoesComProposta();
        ConsultaCartoesResponse response = contasApi.consultarCartoes(1L);

        CarteiraRequest carteiraRequest = new CarteiraRequest("paypal@gmail.com", CarteiraEnum.PAYPAL);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + response.getId() + "cadastrar-carteira")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(carteiraRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/carteiras/{id}"))
                .andExpect(jsonPath("$.email").value("paypal@gmail.com"))
                .andExpect(jsonPath("$.carteira").value(CarteiraEnum.PAYPAL));
    }

    @Test
    public void naoDeveriaCadastrarCarteiraJaAssociadaAoCartao() throws Exception {
        Proposta proposta = new Proposta("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));
        proposta = propostaRepository.save(proposta);

        Cartao cartao = new Cartao("1234-5678-1234-5678", LocalDateTime.now(), "Tales Araujo", new BigDecimal(500), proposta);
        cartaoRepository.save(cartao);

        Carteira carteira = new Carteira(cartao, CarteiraEnum.PAYPAL, "paypal@gmail.com");
        carteiraRepository.save(carteira);

        CarteiraRequest carteiraRequest = new CarteiraRequest("paypal@gmail.com", CarteiraEnum.PAYPAL);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/cadastrar-carteira")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(carteiraRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Esse cartão já é associado a essa carteira."));
    }

}