package br.com.process.integration.database.model.entity.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityEight;

@Service
@Transactional
public class EntityEightService extends AbstractJpaService<EntityEight, EntityEight, Long> {

	@Autowired
	private PagedResourcesAssembler<EntityEight> pagedResourcesAssembler;

	protected EntityEightService() {
		super(QueryJpaController.class, EntityEight.class);
	}

	@Override
	public EntityEight toModel(EntityEight entity) {
		EntityEight model = new EntityEight();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setEntity(EntityEight entity) {
		this.entity = entity;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
