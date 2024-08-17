package br.com.process.integration.database.domain.entity.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.domain.entity.test.EntityOne;

@Component
public class EntityOneAssembler extends RepresentationModelAssemblerSupport<EntityOne, EntityOne> {

	public EntityOneAssembler() {
		super(QueryJpaController.class, EntityOne.class);
	}

	@Override
	public EntityOne toModel(EntityOne entity) {
		EntityOne model = new EntityOne();
		BeanUtils.copyProperties(entity, model);
		return model;
	}


}
