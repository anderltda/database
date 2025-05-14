package br.com.process.integration.database.model.entity.repository.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.model.entity.dto.example.EntitySix;

@Repository
public interface EntitySixRepository extends JpaRepository<EntitySix, Long>, JpaSpecificationExecutor<EntitySix> {
}
