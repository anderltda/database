package br.com.process.integration.database.model.entity.repository.test;

import br.com.process.integration.database.model.entity.dto.test.EntitySeven;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EntitySevenRepository extends JpaRepository<EntitySeven, Object>, JpaSpecificationExecutor<EntitySeven> {
}
