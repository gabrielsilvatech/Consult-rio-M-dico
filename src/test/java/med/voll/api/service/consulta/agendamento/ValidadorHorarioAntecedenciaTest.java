package med.voll.api.service.consulta.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.infra.exception.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidadorHorarioAntecedenciaTest {

    @Autowired
    private ValidadorHorarioAntecedencia validadorHorarioAntecedencia;

    @Test
    @DisplayName("Deve ser válido caso o horário de antecedência seja superior a 30 minutos")
    void validarCenario1() {
        var dados = new DadosAgendamentoConsulta(1l,1l,LocalDateTime.now().plusMinutes(35), Especialidade.CARDIOLOGIA);
        validadorHorarioAntecedencia.validar(dados);
    }

    @Test
    @DisplayName("Deve ser válido caso o horário de antecedência seja igual a 30 minutos")
    void validarCenario2() {
        var dados = new DadosAgendamentoConsulta(1l,1l,LocalDateTime.now().plusMinutes(30), Especialidade.CARDIOLOGIA);
        validadorHorarioAntecedencia.validar(dados);
    }

    @Test
    @DisplayName("Deve lançar uma ValidacaoException caso o horário de antecedência seja inferior a 30 minutos")
    void validarCenario3() {
        var dados = new DadosAgendamentoConsulta(1l,1l,LocalDateTime.now().plusMinutes(25), Especialidade.CARDIOLOGIA);
        assertThrows(ValidacaoException.class, () -> validadorHorarioAntecedencia.validar(dados));
    }
}