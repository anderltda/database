package br.com.process.integration.database.domain.entity.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.domain.entity.EntityTest1;

@Component
public class EntityTest1Assembler extends RepresentationModelAssemblerSupport<EntityTest1, EntityTest1> {

	public EntityTest1Assembler() {
		super(QueryJpaController.class, EntityTest1.class);
	}

	@Override
	public EntityTest1 toModel(EntityTest1 entity) {
		EntityTest1 model = new EntityTest1();
		BeanUtils.copyProperties(entity, model);
		return model;
	}


}
