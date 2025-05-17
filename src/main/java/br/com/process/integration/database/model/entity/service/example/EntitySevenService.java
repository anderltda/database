package br.com.process.integration.database.model.entity.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntitySeven;
import br.com.process.integration.database.model.entity.dto.example.EntitySevenId;

@Service
@Transactional
public class EntitySevenService extends AbstractJpaService<EntitySeven, EntitySeven, EntitySevenId> {

	@Autowired
	private PagedResourcesAssembler<EntitySeven> pagedResourcesAssembler;

	protected EntitySevenService() {
		super(QueryJpaController.class, EntitySeven.class);
	}

	@Override
	public EntitySeven toModel(EntitySeven entity) {
		EntitySeven model = new EntitySeven();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public void setId(EntitySevenId id) {
		this.id = id;
	}

	@Override
	public void setEntity(EntitySeven entity) {
		this.entity = entity;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
