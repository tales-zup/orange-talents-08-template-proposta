package com.zup.propostaservice.cartao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zup.propostaservice.avisodeviagem.AvisoDeViagemRequest;
import com.zup.propostaservice.biometria.BiometriaRequest;
import com.zup.propostaservice.carteira.Carteira;
import com.zup.propostaservice.carteira.CarteiraEnum;
import com.zup.propostaservice.carteira.CarteiraRepository;
import com.zup.propostaservice.carteira.CarteiraRequest;
import com.zup.propostaservice.feign.analisefinanceira.AnaliseFinanceiraApi;
import com.zup.propostaservice.feign.analisefinanceira.AnaliseFinanceiraResponse;
import com.zup.propostaservice.feign.analisefinanceira.ResultadoSolicitacao;
import com.zup.propostaservice.feign.contas.*;
import com.zup.propostaservice.proposta.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CartaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper;

    @MockBean
    private AnaliseFinanceiraApi analiseFinanceiraApi;

    @MockBean
    private ContasApi contasApi;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BloqueioCartaoRepository bloqueioCartaoRepository;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @BeforeClass
    public static void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }
    
    private String associarPropostaAoCartao(Long idProposta) {
        Proposta proposta = propostaRepository.findById(idProposta).orElseThrow(
                () -> new EntityNotFoundException("Erro no teste."));

        ConsultaCartoesResponse consultaCartoesResponse = new ConsultaCartoesResponse(
                "1234-1234-5678-5678", LocalDateTime.now(), "Tales Araujo", new BigDecimal(10), idProposta);
        Cartao cartao = consultaCartoesResponse.toModel(propostaRepository);
        proposta.setNumeroCartao(consultaCartoesResponse.getId());
        cartao.setProposta(proposta);
        propostaRepository.save(proposta);
        cartaoRepository.save(cartao);
        return consultaCartoesResponse.getId();
    }

    private PropostaDto requisitarCriacaoDeProposta() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));

        Mockito.when(analiseFinanceiraApi.consultaDadosCliente(Mockito.any())).thenReturn(new AnaliseFinanceiraResponse(
                "02005036005", "Tales Araujo", "1", ResultadoSolicitacao.SEM_RESTRICAO));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(propostaRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        return mapper.readValue(response, PropostaDto.class);
    }

    private Cartao cadastrarPropostaECartao() {
        Proposta proposta = new Proposta("81850356017", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));
        proposta = propostaRepository.save(proposta);
        Cartao cartao = new Cartao("1234-5678-1234-5678", LocalDateTime.now(), "Tales Araujo", new BigDecimal(500), proposta);
        proposta.setNumeroCartao(cartao.getId());
        propostaRepository.save(proposta);
        cartaoRepository.save(cartao);
        return cartao;
    }

    @Test
    public void deveriaCadastrarBiometriaComSucesso() throws Exception {
//        PropostaDto propostaDto = requisitarCriacaoDeProposta();
//        String idCartao = associarPropostaAoCartao(propostaDto.getId());
        Cartao cartao = cadastrarPropostaECartao();

        BiometriaRequest biometriaRequest = new BiometriaRequest();
        biometriaRequest.setFingerprint("dGV4dG9hbG93d3d3");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/cadastrar-biometria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(biometriaRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fingerprint").value("dGV4dG9hbG93d3d3"));
    }

    @Test
    public void deveriaCadastrarBloqueioCartao() throws Exception {
//        PropostaDto propostaDto = requisitarCriacaoDeProposta();
//        String idCartao = associarPropostaAoCartao(propostaDto.getId());
        Cartao cartao = cadastrarPropostaECartao();

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/bloquear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCartao").value(cartao.getId()))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    public void naoDeveriaBloquearCartaoQueJaEstaBloqueado() throws Exception {
        Cartao cartao = cadastrarPropostaECartao();

        BloqueioCartao bloqueioCartao = new BloqueioCartao(cartao, true, "127.0.0.1", "Spring Teste");
        bloqueioCartaoRepository.save(bloqueioCartao);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/bloquear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Esse cartão já está bloqueado."));
    }

    @Test
    public void deveriaBloquearCartaoQueTemApenasUmBloqueioMasNaoEstaAtivo() throws Exception {
//        PropostaDto propostaDto = requisitarCriacaoDeProposta();
//        String idCartao = associarPropostaAoCartao(propostaDto.getId());
        Cartao cartao = cadastrarPropostaECartao();

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/bloquear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCartao").value(cartao.getId()))
                .andExpect(jsonPath("$.ativo"). value(true));

        BloqueioCartao bloqueioCartao = bloqueioCartaoRepository.findById(1L).orElseThrow(
                () -> new EntityNotFoundException("Erro no teste"));
        bloqueioCartao.liberarBloqueio();
        bloqueioCartaoRepository.save(bloqueioCartao);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/bloquear")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCartao").value(cartao.getId()))
                .andExpect(jsonPath("$.ativo"). value(true));
    }

    @Test
    public void deveriaCadastrarAvisoDeViagem() throws Exception {
//        PropostaDto propostaDto = requisitarCriacaoDeProposta();
//        String idCartao = associarPropostaAoCartao(propostaDto.getId());
        Cartao cartao = cadastrarPropostaECartao();

        AvisoDeViagemRequest avisoDeViagemRequest = new AvisoDeViagemRequest("Paris", LocalDate.now().plusDays(30));

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/aviso-de-viagem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(avisoDeViagemRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCartao").value(cartao.getId()))
                .andExpect(jsonPath("$.destino").value("Paris"));

    }

    @Test
    public void naoDeveriaCadastrarAvisoDeViagemComDataTerminoNoPassado() throws Exception {
//        PropostaDto propostaDto = requisitarCriacaoDeProposta();
//        String idCartao = associarPropostaAoCartao(propostaDto.getId());
        Cartao cartao = cadastrarPropostaECartao();

        AvisoDeViagemRequest avisoDeViagemRequest = new AvisoDeViagemRequest("Paris", LocalDate.now().minusDays(1));

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/aviso-de-viagem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(avisoDeViagemRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$[0].erro").value("must be a future date"));
    }

    @Test
    public void deveriaCadastrarCarteira() throws Exception {
//        PropostaDto propostaDto = requisitarCriacaoDeProposta();
//        String idCartao = associarPropostaAoCartao(propostaDto.getId());
        Cartao cartao = cadastrarPropostaECartao();

        CarteiraRequest carteiraRequest = new CarteiraRequest("paypal@gmail.com", CarteiraEnum.PAYPAL);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/cadastrar-carteira")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carteiraRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("paypal@gmail.com"))
                .andExpect(jsonPath("$.carteira").value(CarteiraEnum.PAYPAL.toString()));
    }

    @Test
    public void naoDeveriaCadastrarCarteiraJaAssociadaAoCartao() throws Exception {
        Cartao cartao = cadastrarPropostaECartao();

        Carteira carteira = new Carteira(cartao, CarteiraEnum.PAYPAL, "paypal@gmail.com");
        carteiraRepository.save(carteira);

        CarteiraRequest carteiraRequest = new CarteiraRequest("paypal@gmail.com", CarteiraEnum.PAYPAL);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getId() + "/cadastrar-carteira")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carteiraRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Esse cartão já é associado a essa carteira."));
    }

}