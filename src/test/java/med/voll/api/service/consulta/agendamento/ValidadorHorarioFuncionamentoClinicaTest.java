package med.voll.api.service.consulta.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.infra.exception.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidadorHorarioFuncionamentoClinicaTest {

    @Autowired
    private ValidadorHorarioFuncionamentoClinica validadorHorarioFuncionamentoClinica;

    @Test
    @DisplayName("Deve ser válido caso a data seja de segunda a sábado e o horário seja entre 07:00 e 18:00")
    void validaCenario1() {
        var data = LocalDateTime.of(2023,06,23,17,00);
        var dadosConsulta = new DadosAgendamentoConsulta(1l,1l, data, Especialidade.CARDIOLOGIA);

        validadorHorarioFuncionamentoClinica.validar(dadosConsulta);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois a data é um domingo")
    void validarCenario2() {
        var data = LocalDateTime.of(2023,06,25,17,00); // dia 25 de junho de 2023 é um domingo
        var dadosConsulta = new DadosAgendamentoConsulta(1l,1l, data, Especialidade.CARDIOLOGIA);

        assertThrows(ValidacaoException.class, () -> validadorHorarioFuncionamentoClinica.validar(dadosConsulta));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o horário é igual as 18:00")
    void validarCenario3() {
        var data = LocalDateTime.of(2023,06,23,18,00);
        var dadosConsulta = new DadosAgendamentoConsulta(1l,1l, data, Especialidade.CARDIOLOGIA);

        assertThrows(ValidacaoException.class, () -> validadorHorarioFuncionamentoClinica.validar(dadosConsulta));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o horário é inferior ás 07:00")
    void validarCenario4() {
        var data = LocalDateTime.of(2023,06,23,06,59);
        var dadosConsulta = new DadosAgendamentoConsulta(1l,1l, data, Especialidade.CARDIOLOGIA);

        assertThrows(ValidacaoException.class, () -> validadorHorarioFuncionamentoClinica.validar(dadosConsulta));
    }

    @Test
    @DisplayName("Deve ser válido, pois o horário é igual ás 07:00")
    void validarCenario5() {
        var data = LocalDateTime.of(2023,06,23,07,00);
        var dadosConsulta = new DadosAgendamentoConsulta(1l,1l, data, Especialidade.CARDIOLOGIA);

        validadorHorarioFuncionamentoClinica.validar(dadosConsulta);
    }
}