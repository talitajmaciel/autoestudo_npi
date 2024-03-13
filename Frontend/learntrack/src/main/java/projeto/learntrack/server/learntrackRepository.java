// Se conecta com o banco
package projeto.learntrack.server;

import org.springframework.data.jpa.repository.JpaRepository;

import projeto.learntrack.model.learntrack;

public interface learntrackRepository extends JpaRepository<learntrack, Long> {
    
}