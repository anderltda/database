package br.com.process.integration.database.model.entity.repository.test;

import br.com.process.integration.database.model.entity.dto.test.EntitySix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EntitySixRepository extends JpaRepository<EntitySix, Object>, JpaSpecificationExecutor<EntitySix> {
}
