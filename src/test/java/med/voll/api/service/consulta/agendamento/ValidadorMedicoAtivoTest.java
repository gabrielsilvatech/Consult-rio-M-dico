package med.voll.api.service.consulta.agendamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.infra.exception.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidadorMedicoAtivoTest {

    @MockBean
    private MedicoRepository repository;
    @Autowired
    private ValidadorMedicoAtivo validadorMedicoAtivo;

    @Test
    @DisplayName("Deve lançar ValidacaoException, pois o médico esta inativo")
    void validar() {
        var endereco = new DadosEndereco("rua", "bairro", "0000000", "cidade", "uf", "complemento", "0");
        var dadosCadastro = new DadosCadastroMedico(
                "Medico Teste",
                "medico.teste@vool.med",
                "11999990000",
                "000111",
                Especialidade.CARDIOLOGIA,
                endereco);
        var medico = new Medico(dadosCadastro);
        var dados = new DadosAgendamentoConsulta(medico.getId(), 1l, LocalDateTime.now().plusHours(1), Especialidade.CARDIOLOGIA);

        //validadorMedicoAtivo.validar(dados);
        assertThrows(ValidacaoException.class, () -> validadorMedicoAtivo.validar(dados));
    }

}