package br.com.process.integration.database.model.entity.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityFour;

@Service
@Transactional
public class EntityFourService extends AbstractJpaService<EntityFour, EntityFour, String> {

	@Autowired
	private PagedResourcesAssembler<EntityFour> pagedResourcesAssembler;

	protected EntityFourService() {
		super(QueryJpaController.class, EntityFour.class);
	}

	@Override
	public EntityFour toModel(EntityFour entity) {
		EntityFour model = new EntityFour();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setEntity(EntityFour entity) {
		this.entity = entity;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
