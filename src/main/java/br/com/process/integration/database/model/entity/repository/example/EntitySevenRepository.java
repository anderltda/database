package br.com.process.integration.database.model.entity.repository.example;

import br.com.process.integration.database.model.entity.dto.example.EntitySeven;
import br.com.process.integration.database.model.entity.dto.example.EntitySevenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EntitySevenRepository extends JpaRepository<EntitySeven, EntitySevenId>, JpaSpecificationExecutor<EntitySeven> {
}
