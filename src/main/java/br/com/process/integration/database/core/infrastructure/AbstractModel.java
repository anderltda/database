package br.com.process.integration.database.core.infrastructure;

import org.springframework.hateoas.RepresentationModel;

import br.com.process.integration.database.core.domain.Model;

public abstract class AbstractModel<M extends Model<?>> extends RepresentationModel<AbstractModel<Model<?>>> implements Model<M> {
	
}
