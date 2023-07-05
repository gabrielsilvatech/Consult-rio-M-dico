package med.voll.api.service.consulta.agendamento;

import med.voll.api.domain.consulta.ConsultaRepository;
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
class ValidadorMedicoComOutraConsultaNoMesmoHorarioTest {

    @Mock
    private ConsultaRepository repository;
    @Mock
    private MedicoRepository medicoRepository;
    @Autowired
    private ValidadorMedicoComOutraConsultaNoMesmoHorario validadorMedicoComOutraConsultaNoMesmoHorario;
    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.validadorMedicoComOutraConsultaNoMesmoHorario = new ValidadorMedicoComOutraConsultaNoMesmoHorario(repository);
    }

    @Test
    @DisplayName("Deve ser válido, pois médico não possui consulta marcada na mesma dara e horário")
    void validarCenario1() {
        var medico = cadastrarMedico("Medico","medico.test@voll.med", "11988880000","012345",Especialidade.CARDIOLOGIA);
        var dados = new DadosAgendamentoConsulta(medico.getId(), 1l, LocalDateTime.now().plusHours(1), Especialidade.CARDIOLOGIA);

        when(repository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medico.getId(), dados.data())).thenReturn(false);

        validadorMedicoComOutraConsultaNoMesmoHorario.validar(dados);

    }

    @Test
    @DisplayName("Deve lançar uma exception, pois médico possui uma consulta marcada na mesma data e horário")
    void validarCenario2() {
        var medico = cadastrarMedico("Medico","medico.test@voll.med", "11988880000","012345",Especialidade.CARDIOLOGIA);
        var dados = new DadosAgendamentoConsulta(medico.getId(), 1l, LocalDateTime.now().plusHours(1), Especialidade.CARDIOLOGIA);

        when(repository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medico.getId(), dados.data())).thenReturn(true);

        assertThrows(ValidacaoException.class, () -> validadorMedicoComOutraConsultaNoMesmoHorario.validar(dados));

    }

    private Medico cadastrarMedico(String nome, String email, String telefone, String crm, Especialidade especialidade) {
        var medico = new Medico(dadosMedico(nome, email, telefone, crm, especialidade));
        medicoRepository.save(medico);
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