package br.com.process.integration.database.model.entity.repository.example;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.model.entity.dto.example.EntityTwo;

@Repository
public interface EntityTwoRepository extends JpaRepository<EntityTwo, UUID>, JpaSpecificationExecutor<EntityTwo> {
	
}
