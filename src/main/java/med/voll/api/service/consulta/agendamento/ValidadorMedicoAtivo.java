package med.voll.api.service.consulta.agendamento;

import med.voll.api.infra.exception.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private MedicoRepository repository;

    public ValidadorMedicoAtivo(MedicoRepository repository) {
        this.repository = repository;
    }

    public void validar (DadosAgendamentoConsulta dados) {

        if(!repository.findAtivoById(dados.idMedico())) {
            throw new ValidacaoException("Consulta tem que ser agendada com um médico ativo");
        }

    }
}
