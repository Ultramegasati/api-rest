package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/medicos")
public class MedicoController {


    @Autowired
    private MedicoRepository repository;

    @PostMapping    //se chegar requisição para url ("/medicos"), do tipo POST chame o metodo cadastrar
    //public void cadastrar(@RequestBody String json){  //pegar o corpo da requisição @RequestBody

    @Transactional     //para alterar dados
    public void cadastrar(@RequestBody  @Valid DadosCadastroMedico dados){
        //System.out.println(dados);

        repository.save(new Medico(dados));
    }

    @GetMapping
    public Page<DadosListagemMedico> listar(Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody   @Valid DadosAtualizacaoMedicos atualizar){

        var medico = repository.getReferenceById(atualizar.id());//objeto medico da consulta do banco de dados
        medico.atualizarIformacoes(atualizar);

    }


    @DeleteMapping("/{id}")  // parâmetro dinâmico
    @Transactional
    public void excluir(@PathVariable Long id){     //@PathVariable indicando que é um parâmetro dinâmico

        var medico = repository.getReferenceById(id);
        medico.excluir();

    }

    //Exclusão por id

//    @DeleteMapping("/{id}")  // parâmetro dinâmico
//    @Transactional
//    public void excluir(@PathVariable Long id){     //@PathVariable indicando que é um parâmetro dinâmico
//
//        repository.deleteById(id);
//
//    }


}
