package br.com.process.integration.database.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.domain.model.entity.EntityTwo;

@Repository
public interface EntityTwoRepository extends JpaRepository<EntityTwo, String>, JpaSpecificationExecutor<EntityTwo> {
	
}
