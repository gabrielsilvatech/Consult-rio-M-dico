package med.voll.api.service.consulta.cancelamento;

import med.voll.api.infra.exception.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorConsultaJaCancelada implements ValidadorCancelamentoDeConsulta{

    @Autowired
    private ConsultaRepository repository;

    public ValidadorConsultaJaCancelada(ConsultaRepository repository) {
        this.repository = repository;
    }

    public void validar(DadosCancelamentoConsulta dados) {
        var consulta = repository.getReferenceById(dados.idConsulta());

        if(consulta.getMotivoCancelamento() != null) {
            throw new ValidacaoException("Consulta Já está cancelada");
        }

    }
}

