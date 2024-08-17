package br.com.process.integration.database.domain.view.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.domain.view.EntityOneView;

@Component
public class EntityOneViewAssembler extends RepresentationModelAssemblerSupport<EntityOneView, EntityOneView> {

	public EntityOneViewAssembler() {
		super(QueryNativeController.class, EntityOneView.class);
	}

	@Override
	public EntityOneView toModel(EntityOneView entity) {
		EntityOneView model = new EntityOneView();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

}
