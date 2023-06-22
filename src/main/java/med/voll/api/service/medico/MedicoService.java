package med.voll.api.service.medico;

import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public void serviceAtualizarMedico(DadosAtualizacaoMedico dados) {
        var medico = medicoRepository.getReferenceById(dados.id());

        if(!medicoRepository.existsById(dados.id())){
            throw new ValidacaoException("Médico não existe");
        }

        if(dados.nome() == null && dados.endereco() == null && dados.telefone() == null) {
            throw new ValidacaoException("Necessário preencher ao menos um dado para atualizar");
        }

        medico.atualizarInformações(dados);
    }

    public void serviceExcluirMedico(Medico medico) {
        if(!medicoRepository.findAtivoById(medico.getId())) {
            throw  new ValidacaoException("Medico já está Inativo");
        }
        medicoRepository.getReferenceById(medico.getId()).excluir();
    }

    public void serviceAtivarMedico(Medico medico) {
        if (medicoRepository.findAtivoById(medico.getId())){
            throw new ValidacaoException("Médico já está ativo");
        }
        medicoRepository.getReferenceById(medico.getId()).ativar();
    }

    public void serviceDetalharMedico(Medico medico) {
        if(!medicoRepository.findAtivoById(medico.getId())) {
            throw new ValidacaoException("Medico está Inativo");
        }
    }

    public void serviceCadastrarMedico(Medico medico) {
        medicoRepository.save(medico);
    }
}
