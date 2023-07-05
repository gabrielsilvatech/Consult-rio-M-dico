package med.voll.api.service.consulta.cancelamento;

import med.voll.api.infra.exception.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorCancelarConsultaAntecedenteVinteQuatroHoras implements ValidadorCancelamentoDeConsulta{

    @Autowired
    private ConsultaRepository repository;

    public ValidadorCancelarConsultaAntecedenteVinteQuatroHoras(ConsultaRepository repository) {
        this.repository = repository;
    }

    public void validar(DadosCancelamentoConsulta dados) {
        var consulta = repository.getReferenceById(dados.idConsulta());
        var agora = LocalDateTime.now();

        var diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();
        if(diferencaEmHoras < 24) {
            throw new ValidacaoException("Consulta só pode ser cancelada com mais de 24 horas de antecedência");
        }
    }
}
