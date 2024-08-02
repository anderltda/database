package br.com.process.integration.database.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.domain.entity.EntityTest1;

@Repository
public interface EntityTest1Repository extends JpaRepository<EntityTest1, Long>, JpaSpecificationExecutor<EntityTest1> {

}
