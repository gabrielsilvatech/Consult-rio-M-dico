package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.paciente.*;
import med.voll.api.service.paciente.PacienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class PacienteControllerTest {

    @MockBean
    private PacienteService service;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DadosCadastroPaciente> dadosCadastroPacienteJacksonTester;
    @Autowired
    private JacksonTester<DadosDetalhamentoPaciente> dadosDetalhamentoPacienteJacksonTester;
    @Autowired
    private JacksonTester<DadosAtualizacaoPaciente> dadosAtualizacaoPacienteJacksonTester;

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 400, quando as informações estão inválidas")
    void cadastrarCenario1() throws Exception{
        var response = mvc.perform(post("/pacientes")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 201, quando as informações estão válidas")
    void cadastrarCenario2() throws Exception{
        var paciente = new Paciente(dadosCadastro());
        var response = mvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroPacienteJacksonTester
                                .write(dadosCadastro()).getJson()
                        )
                ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var dadosDetalhamento = new DadosDetalhamentoPaciente(
          paciente.getId(),
          paciente.getNome(),
          paciente.getEmail(),
          paciente.getTelefone(),
          paciente.getCpf(),
          paciente.getEndereco()
        );

        var jsonEsperado = dadosDetalhamentoPacienteJacksonTester.write(dadosDetalhamento).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 200, quando as informações estão válidas")
    void listarCenario1() throws Exception {
        var response = mvc.perform(get("/pacientes")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 400, quando o id for null")
    void atualizarCenario1() throws Exception {
        var dadosAtualizacao = new DadosAtualizacaoPaciente(null,"Teste Atualizacao", null,null);
        var response = mvc.perform(put("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAtualizacaoPacienteJacksonTester.write(dadosAtualizacao).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 400, quando não passar informações")
    void atualizarCenario2() throws Exception{
        var response = mvc.perform(put("/pacientes")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 201, quando as informações estão válidas")
    void atualizarCenario3() throws Exception{
        var dadosAtualizacao = new DadosAtualizacaoPaciente(1l,"Teste Atualizacao", null,null);
        var response = mvc.perform(put("/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAtualizacaoPacienteJacksonTester.write(dadosAtualizacao).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 204, quando as informações estão válidas")
    void excluirCenario1() throws Exception {
        var response = mvc.perform(delete("/pacientes/1")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 200, quando as informações estão válidas")
    void ativarCenario1() throws Exception {
        var response = mvc.perform(put("/pacientes/1")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 200, quando as informações estão válidas")
    void detalharCenario1() throws Exception {
        var response = mvc.perform(get("/pacientes/1")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua teste",
                "bairro teste",
                "00000000",
                "cidade teste",
                "ct",
                "complemento teste",
                "00");
    }

    private DadosCadastroPaciente dadosCadastro() {
        return new DadosCadastroPaciente(
                "Paciente Teste",
                "paciente.teste@voll.paciente",
                "14785236936",
                "12345678998",
                dadosEndereco());
    }

}