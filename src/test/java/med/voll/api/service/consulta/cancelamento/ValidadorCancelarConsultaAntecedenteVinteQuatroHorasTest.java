package med.voll.api.service.consulta.cancelamento;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.MotivoCancelamento;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.infra.exception.ValidacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ValidadorCancelarConsultaAntecedenteVinteQuatroHorasTest {

    @Mock
    private ConsultaRepository consultaRepository;
    @Autowired
    private ValidadorCancelarConsultaAntecedenteVinteQuatroHoras validadorCancelarConsultaAntecedenteVinteQuatroHoras;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.validadorCancelarConsultaAntecedenteVinteQuatroHoras = new ValidadorCancelarConsultaAntecedenteVinteQuatroHoras(consultaRepository);
    }

    @Test
    @DisplayName("Deve ser válido, pois o horário de antecedência para o cancelamento da consulta é superior a 24 horas")
    public void validarCenario1() {
        var medico = new Medico();
        var paciente = new Paciente();
        var data = LocalDateTime.of(2023,7,4,07,30);
        var consulta = new Consulta(null, medico,paciente, data, null);

        when(consultaRepository.getReferenceById(consulta.getId())).thenReturn(consulta);

        var dados = new DadosCancelamentoConsulta(consulta.getId(), MotivoCancelamento.OUTROS);
        validadorCancelarConsultaAntecedenteVinteQuatroHoras.validar(dados);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o horário de antecedência para o cancelamento da consulta é inferior a 24 horas")
    public void validarCenario2() {
        var medico = new Medico();
        var paciente = new Paciente();
        var data = LocalDateTime.now().plusHours(20);
        var consulta = new Consulta(null, medico,paciente, data, null);

        when(consultaRepository.getReferenceById(consulta.getId())).thenReturn(consulta);

        var dados = new DadosCancelamentoConsulta(consulta.getId(), MotivoCancelamento.OUTROS);
        assertThrows(ValidacaoException.class, () -> validadorCancelarConsultaAntecedenteVinteQuatroHoras.validar(dados));
    }

}