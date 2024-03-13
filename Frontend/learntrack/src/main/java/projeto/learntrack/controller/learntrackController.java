// Controlador para criação do CRUD:
package projeto.learntrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projeto.learntrack.model.learntrack;
import projeto.learntrack.server.learntrackRepository;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/learntrack") // rota: http://localhost:8080/learntrack
public class learntrackController {
    
    @Autowired
    private learntrackRepository repositorio;

    
    @GetMapping()
    public List<learntrack> listar(){
        return repositorio.findAll();
    }
    
    @PostMapping
    public learntrack adicionar(@RequestBody learntrack learntrack){
        return repositorio.save(learntrack);
    }
    
    @PutMapping("/{id}")
    public learntrack atualizar(@PathVariable Long id, @RequestBody learntrack learntrack) {
        learntrack.setId(id);
        return repositorio.save(learntrack);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repositorio.deleteById(id);
    }


}