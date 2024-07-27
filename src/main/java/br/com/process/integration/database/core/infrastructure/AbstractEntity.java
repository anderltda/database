package br.com.process.integration.database.core.infrastructure;

import org.springframework.hateoas.RepresentationModel;

import br.com.process.integration.database.core.domain.Entity;

public abstract class AbstractEntity<ID, E extends Entity<?>> extends RepresentationModel<AbstractEntity<ID, Entity<?>>> implements Entity<ID> {
	
}
