package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    private PacienteRepository repository;
    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody  @Valid DadosCadastroPaciente dadosCadastroPaciente){
        //System.out.println("Dados: " + dadosCadastroPaciente);

        repository.save(new Paciente(dadosCadastroPaciente));

    }

    @GetMapping
    public Page<DadosListagemPaciente> listar(Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody   @Valid DadosAtualizacaoPacientes atualizar){
        var paciente = repository.getReferenceById(atualizar.id());
        paciente.atualizarInformacoes(atualizar);

    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){

        var paciente = repository.getReferenceById(id);
        paciente.excluir();

    }



//    @DeleteMapping("/{id}")
//    @Transactional
//    public void excluir(@PathVariable Long id){
//
//        repository.deleteById(id);
//
//    }
}
