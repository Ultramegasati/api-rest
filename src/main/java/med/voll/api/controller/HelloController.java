package med.voll.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController   //anotação utilizada para api rest
@RequestMapping("/hello")    //qual endereço a url que o controller vai responder


public class HelloController {


    //classe HelloControler; chegando requisição para "/hello", do tipo Get ;  chame o metodo olaMundo
    @GetMapping    //de qual metodo é para chamar;

    public String olaMundo(){
        return "Hello World Spring!!!!!";
    }



}
