package br.com.process.integration.database.domain.view.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.domain.view.EntityTest1View;

@Component
public class EntityTest1ViewAssembler extends RepresentationModelAssemblerSupport<EntityTest1View, EntityTest1View> {

	public EntityTest1ViewAssembler() {
		super(QueryNativeController.class, EntityTest1View.class);
	}

	@Override
	public EntityTest1View toModel(EntityTest1View entity) {
		EntityTest1View model = new EntityTest1View();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

}
