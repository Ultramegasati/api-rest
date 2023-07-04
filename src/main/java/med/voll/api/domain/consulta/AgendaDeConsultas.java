package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.cancela.DadosCancelamentoConsulta;
import med.voll.api.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private PacienteRepository pacienteRepository;



    //chamando todos os validadores
    //nome do método dos validadores são iguais
    //foi criado uma interface
    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;


    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;



    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dadosAgendamentoConsulta){

//existsById ja faz consuta no baco de dados p determinado id
        if (!pacienteRepository.existsById(dadosAgendamentoConsulta.idPaciente())){
            throw new ValidacaoException("Id do paciente não existe!");
        }

        //esta vindo idmedico e não é null, && verifico no bd
        if (dadosAgendamentoConsulta.idMedico() != null && !medicoRepository.existsById(dadosAgendamentoConsulta.idMedico())){
            throw new ValidacaoException("Id do médico não existe!");
        }


        //percorrer todos os validadores
        validadores.forEach(v -> v.validar(dadosAgendamentoConsulta));





//getReferenceById() é utilizado para recuperar uma referência a um objeto no banco de dados,
// sem que seja necessário buscar todas as informações desse objeto

//findById() é utilizado para buscar todas as informações do objeto no banco de dados.
        var paciente = pacienteRepository.getReferenceById(dadosAgendamentoConsulta.idPaciente());
        var medico = escolherMedico(dadosAgendamentoConsulta);
        if (medico == null){
            throw new ValidacaoException("Não tem médico disponivél nesta data.");
        }
        var consulta = new Consulta(null, medico, paciente, dadosAgendamentoConsulta.data(), null);




        consultaRepository.save(consulta);


        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {

       if (dados.idMedico() != null){
           return medicoRepository.getReferenceById(dados.idMedico());
       }


       if (dados.especialidade() == null){
           throw new ValidacaoException("Especialidade é obrigatória quando médico não foi escolhido!!");
       }

       return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
    }


    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }

}
