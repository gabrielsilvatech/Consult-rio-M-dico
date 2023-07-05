package med.voll.api.service.consulta.cancelamento;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.MotivoCancelamento;
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
class ValidadorConsultaJaCanceladaTest {

    @Autowired
    private ValidadorConsultaJaCancelada validadorConsultaJaCancelada;
    @Mock
    private ConsultaRepository consultaRepository;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.validadorConsultaJaCancelada = new ValidadorConsultaJaCancelada(consultaRepository);
    }

    @Test
    @DisplayName("Deve ser válido, pois o motivo de cancelamento é null")
    void validarCenario1() {
        var medico = new Medico();
        var paciente = new Paciente();
        var consulta = new Consulta(1l,medico,paciente, LocalDateTime.now(),null);

        when(consultaRepository.getReferenceById(consulta.getId())).thenReturn(consulta);

        var dados = new DadosCancelamentoConsulta(consulta.getId(), MotivoCancelamento.OUTROS);
        validadorConsultaJaCancelada.validar(dados);

    }

    @Test
    @DisplayName("Deve lançar uma exception, pois a consulta já está cancelada")
    void validarCenario2() {
        var medico = new Medico();
        var paciente = new Paciente();
        var consulta = new Consulta(1l,medico,paciente, LocalDateTime.now(),MotivoCancelamento.MEDICO_CANCELOU);

        when(consultaRepository.getReferenceById(consulta.getId())).thenReturn(consulta);

        var dados = new DadosCancelamentoConsulta(consulta.getId(), MotivoCancelamento.OUTROS);
        assertThrows(ValidacaoException.class, () -> validadorConsultaJaCancelada.validar(dados));

    }
}