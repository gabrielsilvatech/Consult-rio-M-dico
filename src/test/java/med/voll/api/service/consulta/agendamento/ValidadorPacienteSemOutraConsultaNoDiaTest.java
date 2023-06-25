package med.voll.api.service.consulta.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidadorPacienteSemOutraConsultaNoDiaTest {

    @MockBean
    private PacienteRepository repository;
    @Autowired
    private ValidadorPacienteSemOutraConsultaNoDia validadorPacienteSemOutraConsultaNoDia;


    @Test
    @DisplayName("Deve ser válido, pois paciente não possui consulta")
    void validarCenario1() {
        var endereco = new DadosEndereco("logadouro", "bairro", "01111222", "cidade", "uf", "complemento", "00");
        var dadosCadrasto = new DadosCadastroPaciente("paciente", "pacient.test@voll.pct","11988884444", "12345678998",endereco);
        var paciente = new Paciente(dadosCadrasto);
        var data = LocalDateTime.now().plusHours(5);
        var dados = new DadosAgendamentoConsulta(1l, paciente.getId(), data, Especialidade.CARDIOLOGIA);

        validadorPacienteSemOutraConsultaNoDia.validar(dados);
    }
}