package br.com.process.integration.database.model.entity.repository.example;

import br.com.process.integration.database.model.entity.dto.example.EntityTree;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityTreeRepository extends JpaRepository<EntityTree, UUID>, JpaSpecificationExecutor<EntityTree> {
}
