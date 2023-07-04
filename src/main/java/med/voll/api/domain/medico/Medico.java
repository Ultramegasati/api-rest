package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.endereco.Endereco;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter                             //
@NoArgsConstructor                  //
@AllArgsConstructor                 //
@EqualsAndHashCode(of = "id")        //utilizando anotações lombok
public class Medico {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    private String crm;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;

    private Boolean ativo;


    public Medico(DadosCadastroMedico dados) {
        this.ativo = true;
        this.nome = dados.nome();
        this.email = dados.email();
        this.crm = dados.crm();
        this.telefone = dados.telefone();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }

    public void atualizarIformacoes(DadosAtualizacaoMedicos atualizar) {
        if (atualizar.nome() != null){
            this.nome = atualizar.nome();
        }

        if (atualizar.telefone() != null){
            this.telefone = atualizar.telefone();
        }

        if (atualizar.endereco() != null){
            this.endereco.atualizarInforacoes(atualizar.endereco());
        }
    }

    public void excluir() {

        this.ativo = false;
    }
}
