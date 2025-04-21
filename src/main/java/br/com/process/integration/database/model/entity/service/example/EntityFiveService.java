package br.com.process.integration.database.model.entity.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityFive;

@Service
@Transactional
public class EntityFiveService extends AbstractJpaService<EntityFive, EntityFive, String> {

	@Autowired
	private PagedResourcesAssembler<EntityFive> pagedResourcesAssembler;

	protected EntityFiveService() {
		super(QueryJpaController.class, EntityFive.class);
	}

	@Override
	public EntityFive toModel(EntityFive entity) {
		EntityFive model = new EntityFive();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void setEntity(EntityFive entity) {
		this.entity = entity;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
