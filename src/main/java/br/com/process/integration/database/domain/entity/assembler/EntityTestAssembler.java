package br.com.process.integration.database.domain.entity.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.domain.entity.test.EntityTest;

@Component
public class EntityTestAssembler extends RepresentationModelAssemblerSupport<EntityTest, EntityTest> {

	public EntityTestAssembler() {
		super(QueryJpaController.class, EntityTest.class);
	}

	@Override
	public EntityTest toModel(EntityTest entity) {
		EntityTest model = new EntityTest();
		BeanUtils.copyProperties(entity, model);
		return model;
	}


}
