package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.*;
import med.voll.api.service.medico.MedicoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
class MedicoControllerTest {


    @MockBean
    private MedicoService service;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJacksonTester;
    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJacksonTester;
    @Autowired
    private JacksonTester<DadosAtualizacaoMedico> dadosAtualizacaoMedicoJacksonTester;
    @Autowired
    private JacksonTester<DadosListagemMedico> dadosListagemMedicoJacksonTester;

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 201, quando as informações estão válidas")
    void cadastrarCenario1() throws Exception{
        var medico = new Medico(dadosCadastro());
        var response = mvc.perform(post("/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroMedicoJacksonTester
                        .write(dadosCadastro()).getJson()
                )
        ).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var dadosDetalhamento =  new DadosDetalhamentoMedico(
                medico.getId(),
                medico.getNome(),
                medico.getEmail(),
                medico.getCrm(),
                medico.getTelefone(),
                medico.getEspecialidade(),
                medico.getEndereco()
        );

        var jsonEsperado = dadosDetalhamentoMedicoJacksonTester.write(dadosDetalhamento).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 400, quando as informações estão inválidas")
    void cadastrarCenario2() throws Exception{

        var response = mvc.perform(post("/medicos")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 200, quando as informações estão válidas")
    void listarCenario1() throws Exception {
        var response = mvc.perform(get("/medicos")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 200, quando as informações estão válidas")
    void atualizarCenario1() throws Exception{
        var response = mvc.perform(put("/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAtualizacaoMedicoJacksonTester.write(new DadosAtualizacaoMedico(1l,"Nome teste", null,null)).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 400, quando o ID for null")
    void atualizarCenario2() throws Exception{
        var response = mvc.perform(put("/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosAtualizacaoMedicoJacksonTester.write(new DadosAtualizacaoMedico(null,"Nome Teste", null,null)).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 400, quando não for passado informações")
    void atualizarCenario3() throws Exception{
        var response = mvc.perform(put("/medicos")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 204, quando as informações são válidas")
    void excluirCenario1() throws Exception {
        var response = mvc.perform(delete("/medicos/1")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 200, quando as informações são válidas")
    void ativarCenario1() throws Exception {
        var medico = new Medico(dadosCadastro());
        var response = mvc.perform(put("/medicos/1")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver código http 200, quando as informações são válidas")
    void detalharCenario1() throws Exception {
        var medico = new Medico(dadosCadastro());
        var response = mvc.perform(get("/medicos/1")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private DadosEndereco dadosEndereco(){
        return new DadosEndereco(
                "Rua teste",
                "Bairro teste",
                "08888222",
                "Cidade Teste",
                "CT",
                "Complemento",
                "00"
        );
    }

    private DadosCadastroMedico dadosCadastro() {
        return new DadosCadastroMedico(
                "Medico Teste",
                "medico.teste@voll.med",
                "11999990000",
                "000000",
                Especialidade.CARDIOLOGIA,
                dadosEndereco()
        );
    }

}