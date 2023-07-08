package med.voll.api.controller;

import med.voll.api.domain.consulta.*;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.service.consulta.AgendaService;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosCancelamentoConsulta> dadosCancelamentoConsultaJson;

    @MockBean
    private AgendaService agendaService;

    @Test
    @DisplayName("Deveria devolver código http 400, quando as informações estão inválidas")
    @WithMockUser
    void agendarCenario1() throws Exception {
            var response = mvc.perform(post("/consultas"))
                    .andReturn().getResponse();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 200, quando as informações estão válidas")
    @WithMockUser
    void agendarCenario2() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;
        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 1l, 1l, data);

        when(agendaService.agendar(any())).thenReturn(dadosDetalhamento);

        var response = mvc
                .perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAgendamentoConsultaJson
                                .write(new DadosAgendamentoConsulta(1l, 1l, data, especialidade)).getJson()
                        )
                ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(dadosDetalhamento).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    @Test
    @DisplayName("Deveria devolver código http 400, quando as informações estão inválidas")
    @WithMockUser
    void cancelarCenario1() throws Exception {

        var response = mvc.perform(delete("/consultas"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("Deveria devolver código http 204, quando as informações está válida e a consulta não está cancelada")
    @WithMockUser
    void cancelarCenario2() throws Exception {
        var motivo = MotivoCancelamento.MEDICO_CANCELOU;

        var response = mvc
                .perform(delete("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCancelamentoConsultaJson
                                .write(new DadosCancelamentoConsulta(1l, motivo)).getJson()
                        )
                ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

}