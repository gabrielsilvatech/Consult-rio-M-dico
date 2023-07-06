package med.voll.api.service.paciente;

import med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest

class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Autowired
    private PacienteService pacienteService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.pacienteService = new PacienteService(pacienteRepository);
    }

    @Test
    @DisplayName("Deve ser Válido, pois o ID é válido e foi passado ao menos uma informação")
    void serviceAtualizarPacienteCenario1() {
        var dados = new DadosAtualizacaoPaciente(1l,"Paciente Teste", null,null);
        when(pacienteRepository.existsById(dados.id())).thenReturn(true);
        pacienteService.serviceAtualizarPaciente(dados);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o ID é válido, porém não foi passado nenhuma informção")
    void serviceAtualizarPacienteCenario2() {
        var dados = new DadosAtualizacaoPaciente(1l,null, null,null);
        when(pacienteRepository.existsById(dados.id())).thenReturn(true);
        assertThrows(ValidacaoException.class, () -> pacienteService.serviceAtualizarPaciente(dados));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o ID é invalido")
    void serviceAtualizarPacienteCenario3() {
        var dados = new DadosAtualizacaoPaciente(1l,"Paciente Teste", null,null);
        when(pacienteRepository.existsById(dados.id())).thenReturn(false);
        assertThrows(ValidacaoException.class, () -> pacienteService.serviceAtualizarPaciente(dados));
    }

    @Test
    @DisplayName("Deve ser válido, pois o ID existe, e o paciente está ativo")
    void serviceExcluirPacienteCenario1() {
        var paciente = new Paciente();
        when(pacienteRepository.existsById(paciente.getId())).thenReturn(true);
        when(pacienteRepository.findAtivoById(paciente.getId())).thenReturn(true);
        when(pacienteRepository.getReferenceById(paciente.getId())).thenReturn(paciente);
        pacienteService.serviceExcluirPaciente(paciente);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o ID existe, e o paciente já está inativo")
    void serviceExcluirPacienteCenario2() {
        var paciente = new Paciente();
        when(pacienteRepository.existsById(paciente.getId())).thenReturn(true);
        when(pacienteRepository.findAtivoById(paciente.getId())).thenReturn(false);
        assertThrows(ValidacaoException.class, () -> pacienteService.serviceExcluirPaciente(paciente));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o ID não existe")
    void serviceExcluirPacienteCenario3() {
        var paciente = new Paciente();
        when(pacienteRepository.existsById(paciente.getId())).thenReturn(false);
        assertThrows(ValidacaoException.class, () -> pacienteService.serviceExcluirPaciente(paciente));
    }

    @Test
    @DisplayName("Deve ser válido, pois o ID existe e o paciente está inativo")
    void serviceAtivarPacienteCenario1() {
        var paciente = new Paciente();
        when(pacienteRepository.existsById(paciente.getId())).thenReturn(true);
        when(pacienteRepository.findAtivoById(paciente.getId())).thenReturn(false);
        when(pacienteRepository.getReferenceById(paciente.getId())).thenReturn(paciente);
        pacienteService.serviceAtivarPaciente(paciente);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o ID existe e o paciente já está ativo")
    void serviceAtivarPacienteCenario2() {
        var paciente = new Paciente();
        when(pacienteRepository.existsById(paciente.getId())).thenReturn(true);
        when(pacienteRepository.findAtivoById(paciente.getId())).thenReturn(true);
        assertThrows(ValidacaoException.class, () -> pacienteService.serviceAtivarPaciente(paciente));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o ID não existe")
    void serviceAtivarPacienteCenario3() {
        var paciente = new Paciente();
        when(pacienteRepository.existsById(paciente.getId())).thenReturn(false);
        assertThrows(ValidacaoException.class, () -> pacienteService.serviceAtivarPaciente(paciente));
    }

    @Test
    @DisplayName("Deve ser válido, pois o id existe e está ativo")
    void serviceDetalharPacienteCenario1() {
        var paciente = new Paciente();
        when(pacienteRepository.existsById(paciente.getId())).thenReturn(true);
        when(pacienteRepository.findAtivoById(paciente.getId())).thenReturn(true);
        pacienteService.serviceDetalharPaciente(paciente);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o id existe, porém está inativo")
    void serviceDetalharPacienteCenario2() {
        var paciente = new Paciente();
        when(pacienteRepository.existsById(paciente.getId())).thenReturn(true);
        when(pacienteRepository.findAtivoById(paciente.getId())).thenReturn(false);
        assertThrows(ValidacaoException.class, () -> pacienteService.serviceDetalharPaciente(paciente));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o id não existe")
    void serviceDetalharPacienteCenario3() {
        var paciente = new Paciente();
        when(pacienteRepository.existsById(paciente.getId())).thenReturn(false);
        assertThrows(ValidacaoException.class, () -> pacienteService.serviceDetalharPaciente(paciente));
    }
}