package med.voll.api.service.consulta.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
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
class ValidadorMedicoAtivoTest {

    @Mock
    private MedicoRepository repository;
    @Autowired
    private ValidadorMedicoAtivo validadorMedicoAtivo;
    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.validadorMedicoAtivo = new ValidadorMedicoAtivo(repository);
    }

    @Test
    @DisplayName("Deve ser válido, pois o médico está ativo")
    void validarCenario1() {
        var medico = cadastrarMedico("Medico","medico.test@voll.med", "11988880000","012345",Especialidade.CARDIOLOGIA);
        when(repository.findAtivoById(medico.getId())).thenReturn(true);

        var dados = new DadosAgendamentoConsulta(medico.getId(), 1l, LocalDateTime.now().plusHours(1), Especialidade.CARDIOLOGIA);
        validadorMedicoAtivo.validar(dados);
    }

    @Test
    @DisplayName("Deve lançar ValidacaoException, pois o médico está inativo")
    void validarCenario2() {
        var medico = cadastrarMedico("Medico","medico.test@voll.med", "11988880000","012345",Especialidade.CARDIOLOGIA);
        when(repository.findAtivoById(medico.getId())).thenReturn(false);

        var dados = new DadosAgendamentoConsulta(medico.getId(), 1l, LocalDateTime.now().plusHours(1), Especialidade.CARDIOLOGIA);
        assertThrows(ValidacaoException.class, () -> validadorMedicoAtivo.validar(dados));
    }

    private Medico cadastrarMedico(String nome, String email, String telefone, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosMedico(nome, email, telefone, crm, especialidade));
        repository.save(medico);
        return medico;
    }

    private DadosCadastroMedico dadosMedico(String nome, String email, String telefone, String crm, Especialidade especialidade) {
        return new DadosCadastroMedico(
                nome,
                email,
                telefone,
                crm,
                especialidade,
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