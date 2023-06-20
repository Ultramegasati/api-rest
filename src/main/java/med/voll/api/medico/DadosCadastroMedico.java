package med.voll.api.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.endereco.DadosEndereco;


public record DadosCadastroMedico(

        //@NotNull         //para não receber nada nulo
        @NotBlank        //para não receber vazio e nulo, não precisa no @NotNull
        String nome,
        @NotBlank        //tbm para campos Strings
        @Email
        String email,
        @NotBlank
        String telefone,
        @NotBlank
        @Pattern(regexp = "\\d{4,6}")  //expressão regular, de 4 a 6 dígitos
        String crm,
        @NotNull
        Especialidade especialidade,
        @NotNull
        @Valid     //dentro dele um dos atributos é um DTO e nele vai ter anotacoes do bean, para validar este tbm
        DadosEndereco endereco) {




}
