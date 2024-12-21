package br.com.process.integration.database.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.domain.model.entity.EntityStatus;

@Repository
public interface EntityStatusRepositoury extends JpaRepository<EntityStatus, Long>, JpaSpecificationExecutor<EntityStatus> {

}
