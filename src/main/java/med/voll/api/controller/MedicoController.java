package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {


    @Autowired
    private MedicoRepository repository;

    @PostMapping    //se chegar requisição para url ("/medicos"), do tipo POST chame o metodo cadastrar
    //public void cadastrar(@RequestBody String json){  //pegar o corpo da requisição @RequestBody
    //POST no protocolo HTT devole código 201, devolve no corpo os dados do novo recurso/registro criado
    //e devolve também um cabeçalho do protocolo HTTP(Location)

    @Transactional     //para alterar dados

    public ResponseEntity cadastrar(@RequestBody  @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder){
        //System.out.println(dados);

        var medico = new Medico(dados);

        repository.save(medico);


        //Spring tem a classe UriComponentsBuilder que faz o encapsulamento o endereço da API


        //passado parâmetro{id} dinâmico
        //toUri() criar o obeto tipo uri
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();




        //criar uri que representa o endereço que Spring vai criar atomaticamente

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody   @Valid DadosAtualizacaoMedicos atualizar){

        var medico = repository.getReferenceById(atualizar.id());//objeto medico da consulta do banco de dados
        medico.atualizarIformacoes(atualizar);

        //devolver a atualização realizada
        //medico é entidade JPA, não é recomendado devolver e receber no JPAcontroller

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));


    }


    @DeleteMapping("/{id}")  // parâmetro dinâmico
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){     //@PathVariable indicando que é um parâmetro dinâmico

        var medico = repository.getReferenceById(id);
        medico.excluir();

        //na exclusão boa pratica retornar 204, foi realizado com sucesso e sem conteúdo
        return ResponseEntity.noContent().build();   //build() para construir o objeto ResponseEntity

    }

    //Exclusão por id

//    @DeleteMapping("/{id}")  // parâmetro dinâmico
//    @Transactional
//    public void excluir(@PathVariable Long id){     //@PathVariable indicando que é um parâmetro dinâmico
//
//        repository.deleteById(id);
//
//    }



    //detalhar o médico
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
}
