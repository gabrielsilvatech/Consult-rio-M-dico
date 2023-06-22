package med.voll.api.domain.consulta;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ConsultaRepositoryTest {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deveria devolver false, se o médico estiver uma consulta na data, porém o motivo de cancelamento não seja null")
    void existsByMedicoIdAndDataAndMotivoCancelamentoIsNullCenario1() {
        //given ou arrange
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var medico = cadastrarMedico("Medico Test", "medico.test@voll.med", "123456", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente Test", "paciente.test@voll.paciente", "00000000000");
        var motivoCancelamento = MotivoCancelamento.MEDICO_CANCELOU;
        cadastrarConsulta(medico, paciente, proximaSegundaAs10, motivoCancelamento);

        //when ou act
        var consulta = consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medico.getId(), proximaSegundaAs10);

        //then ou assert
        assertThat(consulta).isFalse();
    }

    @Test
    @DisplayName("Deveria devolver false, se o médico não estiver consulta na data")
    void existsByMedicoIdAndDataAndMotivoCancelamentoIsNullCenario2() {
        //given ou arrange
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var medico = cadastrarMedico("Medico Test", "medico.test@voll.med", "123456", Especialidade.CARDIOLOGIA);

        //when ou act
        var consulta = consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medico.getId(), proximaSegundaAs10);

        //then ou assert
        assertThat(consulta).isFalse();
    }

    @Test
    @DisplayName("Deveria devolver true, se o médico estiver consulta na data e o motivo de cancelamento seja null")
    void existsByMedicoIdAndDataAndMotivoCancelamentoIsNullCenario3() {
        //given ou arrange
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var medico = cadastrarMedico("Medico Test", "medico.test@voll.med", "123456", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente Test", "paciente.test@voll.paciente", "00000000000");
        cadastrarConsulta(medico, paciente, proximaSegundaAs10, null);

        //when ou act
        var consulta = consultaRepository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medico.getId(), proximaSegundaAs10);

        //then ou assert
        assertThat(consulta).isTrue();
    }

    @Test
    @DisplayName("Deveria devolver false, se o paciente estiver uma consulta na data, porém o motivo de cancelamento não seja null")
    void existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNullCenario1() {
        //given ou arrange
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var proximaSegundaAs7 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(7,0);
        var proximaSegundaAs18 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(18,0);
        var medico = cadastrarMedico("Medico Test", "medico.test@voll.med", "123456", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente Test", "paciente.test@voll.paciente", "00000000000");
        var motivoCancelamento = MotivoCancelamento.MEDICO_CANCELOU;
        cadastrarConsulta(medico, paciente, proximaSegundaAs10, motivoCancelamento);

        //when ou act
        var consulta = consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(paciente.getId(), proximaSegundaAs7, proximaSegundaAs18);

        //then ou assert
        assertThat(consulta).isFalse();
    }

    @Test
    @DisplayName("Deveria devolver false, se o paciente não estiver consulta na data")
    void existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNullCenario2() {
        //given ou arrange
        var proximaSegundaAs7 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(7,0);
        var proximaSegundaAs18 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(18,0);
        var paciente = cadastrarPaciente("Paciente Test", "paciente.test@voll.paciente", "00000000000");

        //when ou act
        var consulta = consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(paciente.getId(), proximaSegundaAs7, proximaSegundaAs18);

        //then ou assert
        assertThat(consulta).isFalse();
    }

    @Test
    @DisplayName("Deveria devolver true, se o paciente estiver consulta na data e o motivo de cancelamento seja null")
    void existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNullCenario3() {
        //given ou arrange
        var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var proximaSegundaAs7 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(7,0);
        var proximaSegundaAs18 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(18,0);
        var medico = cadastrarMedico("Medico Test", "medico.test@voll.med", "123456", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente Test", "paciente.test@voll.paciente", "00000000000");
        cadastrarConsulta(medico, paciente, proximaSegundaAs10, null);
        //when ou act
        var consulta = consultaRepository.existsByPacienteIdAndDataBetweenAndMotivoCancelamentoIsNull(paciente.getId(), proximaSegundaAs7, proximaSegundaAs18);

        //then ou assert
        assertThat(consulta).isTrue();
    }

    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data, MotivoCancelamento motivoCancelamento) {
        em.persist(new Consulta(null, medico, paciente, data, motivoCancelamento));
    }

    private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
        em.persist(medico);
        return medico;
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf) {
        var paciente = new Paciente(dadosPaciente(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }

    private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new DadosCadastroMedico(
                nome,
                email,
                "61999999999",
                crm,
                especialidade,
                dadosEndereco()
        );
    }

    private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(
                nome,
                email,
                "61999999999",
                cpf,
                dadosEndereco()
        );
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }
}