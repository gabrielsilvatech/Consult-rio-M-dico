package med.voll.api.service.consulta.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.exception.ValidacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ValidadorPacienteAtivoTest {

    @Mock
    private PacienteRepository repository;
    @Autowired
    private ValidadorPacienteAtivo validadorPacienteAtivo;
    @BeforeEach
    private void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.validadorPacienteAtivo = new ValidadorPacienteAtivo(repository);
    }

    @Test
    @DisplayName("Deve ser válido, pois o paciente está ativo")
    void validarCenario1() {
        var paciente = cadastrarPaciente("Paciente", "paciente.test@voll", "11988880000", "12345678996");
        var data = LocalDateTime.now().plusHours(5);

        when(repository.findAtivoById(paciente.getId())).thenReturn(true);

        var dados = new DadosAgendamentoConsulta(1l, paciente.getId(), data, null);
        validadorPacienteAtivo.validar(dados);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o paciente está inativo")
    void validarCenario2() {
        var paciente = cadastrarPaciente("Paciente", "paciente.test@voll", "11988880000", "12345678996");
        var data = LocalDateTime.now().plusHours(5);

        when(repository.findAtivoById(paciente.getId())).thenReturn(false);

        var dados = new DadosAgendamentoConsulta(1l, paciente.getId(), data, null);
        assertThrows(ValidacaoException.class, () -> validadorPacienteAtivo.validar(dados));
    }

    private Paciente cadastrarPaciente(String nome, String email, String telefone, String cpf) {
        var paciente = new Paciente(dadosPaciente(nome, email, telefone, cpf));
        repository.save(paciente);
        return paciente;
    }

    private DadosCadastroPaciente dadosPaciente(String nome, String email, String telefone, String cpf) {
        return new DadosCadastroPaciente(
                nome,
                email,
                telefone,
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