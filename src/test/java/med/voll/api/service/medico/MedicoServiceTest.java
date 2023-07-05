package med.voll.api.service.medico;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.*;
import med.voll.api.infra.exception.ValidacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;
    @Autowired
    private MedicoService medicoService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.medicoService = new MedicoService(medicoRepository);
    }

    @Test
    @DisplayName("Deve ser válido, pois foi passado ao menos um parâmetro")
    void serviceAtualizarMedicoCenario1() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(true);

        var dadosAtualizacao= new DadosAtualizacaoMedico(medico.getId(), "Medico Test",null,null);
        medicoService.serviceAtualizarMedico(dadosAtualizacao);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois não foi passado nenhum parâmetro")
    void serviceAtualizarMedicoCenario2() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(true);

        var dadosAtualizacao= new DadosAtualizacaoMedico(medico.getId(), null,null,null);
        assertThrows(ValidacaoException.class, () -> medicoService.serviceAtualizarMedico(dadosAtualizacao));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois ID informado não existe")
    void serviceAtualizarMedicoCenario3() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(false);

        var dadosAtualizacao= new DadosAtualizacaoMedico(medico.getId(), "Medico Test",null,null);
        assertThrows(ValidacaoException.class, () -> medicoService.serviceAtualizarMedico(dadosAtualizacao));
    }

    @Test
    @DisplayName("Deve ser válido, pois o médico existe e está ativo")
    void serviceExcluirMedicoCenario1() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(true);
        when(medicoRepository.findAtivoById(medico.getId())).thenReturn(true);
        when(medicoRepository.getReferenceById(medico.getId())).thenReturn(medico);

        medicoService.serviceExcluirMedico(medico);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o médico existe, porém está inátivo")
    void serviceExcluirMedicoCenario2() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(true);
        when(medicoRepository.findAtivoById(medico.getId())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> medicoService.serviceExcluirMedico(medico));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o médico não existe")
    void serviceExcluirMedicoCenario3() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> medicoService.serviceExcluirMedico(medico));
    }

    @Test
    @DisplayName("Deve ser válido, se o id existir e o médico estiver excluido lógicamente")
    void serviceAtivarMedicoCenario1() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(true);
        when(medicoRepository.findAtivoById(medico.getId())).thenReturn(false);
        when(medicoRepository.getReferenceById(medico.getId())).thenReturn(medico);

        medicoService.serviceAtivarMedico(medico);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o id existe, porem o médico está ativo")
    void serviceAtivarMedicoCenario2() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(true);
        when(medicoRepository.findAtivoById(medico.getId())).thenReturn(true);

        assertThrows(ValidacaoException.class, () -> medicoService.serviceAtivarMedico(medico));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o id não existe")
    void serviceAtivarMedicoCenario3() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> medicoService.serviceAtivarMedico(medico));
    }

    @Test
    @DisplayName("Deve ser válido, pois o id existe e o médico está ativo")
    void serviceDetalharMedicoCenario1() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(true);
        when(medicoRepository.findAtivoById(medico.getId())).thenReturn(true);

        medicoService.serviceDetalharMedico(medico);
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o id existe, porém o médico está inátivo")
    void serviceDetalharMedicoCenario2() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(true);
        when(medicoRepository.findAtivoById(medico.getId())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> medicoService.serviceDetalharMedico(medico));
    }

    @Test
    @DisplayName("Deve lançar uma exception, pois o id não existe")
    void serviceDetalharMedicoCenario3() {
        var medico = new Medico();
        when(medicoRepository.existsById(medico.getId())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> medicoService.serviceDetalharMedico(medico));
    }

}