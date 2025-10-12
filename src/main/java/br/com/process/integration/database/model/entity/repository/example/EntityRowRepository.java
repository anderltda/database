package br.com.process.integration.database.model.entity.repository.example;

import br.com.process.integration.database.model.entity.dto.example.EntityRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRowRepository extends JpaRepository<EntityRow, Long>, JpaSpecificationExecutor<EntityRow> {
}
