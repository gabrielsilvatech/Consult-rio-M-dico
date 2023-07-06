package med.voll.api.service.paciente;

import med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public void serviceAtualizarPaciente(DadosAtualizacaoPaciente dados) {
        if(!pacienteRepository.existsById(dados.id())) {
            throw new ValidacaoException("Paciente não existe");
        }

        if(dados.nome() == null && dados.endereco() == null && dados.telefone() == null) {
            throw new ValidacaoException("Necessário preencher ao menos um dado para atualizar");
        }

    }

    public void serviceExcluirPaciente(Paciente paciente) {
        if(!pacienteRepository.existsById(paciente.getId())) {
            throw new ValidacaoException("Paciente não existe");
        }

        if(!pacienteRepository.findAtivoById(paciente.getId())) {
            throw new ValidacaoException("Paciente já está Inativo");
        }
        pacienteRepository.getReferenceById(paciente.getId()).excluir();
    }

    public void serviceAtivarPaciente(Paciente paciente) {
        if(!pacienteRepository.existsById(paciente.getId())) {
            throw new ValidacaoException("Paciente não existe");
        }

        if(pacienteRepository.findAtivoById(paciente.getId())) {
            throw new ValidacaoException("Paciente já está Ativo");
        }
        pacienteRepository.getReferenceById(paciente.getId()).ativar();
    }

    public void serviceDetalharPaciente(Paciente paciente) {
        if(!pacienteRepository.existsById(paciente.getId())) {
            throw new ValidacaoException("Paciente não existe");
        }

        if (!pacienteRepository.findAtivoById(paciente.getId())) {
            throw new ValidacaoException("Paciente está Inativo");
        }
    }

}
