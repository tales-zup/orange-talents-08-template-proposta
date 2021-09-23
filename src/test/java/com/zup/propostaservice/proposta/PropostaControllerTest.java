package com.zup.propostaservice.proposta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PropostaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveriaCadastrarPropostaComSucesso() throws Exception {
        PropostaRequest body = new PropostaRequest("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void naoDeveriaCadastrarPropostaComCpfOuCnpjInvalido() throws Exception {
        PropostaRequest body = new PropostaRequest("12345678912", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void naoDeveriaCadastrarPropostaComMesmoDocumento() throws Exception {
        PropostaRequest body = new PropostaRequest("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));
        PropostaRequest body2 = new PropostaRequest("02005036005", "tales.araujo@zup.com.br",
                "Tales Araujo", "Rua Abc 123", new BigDecimal(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(body2))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

}