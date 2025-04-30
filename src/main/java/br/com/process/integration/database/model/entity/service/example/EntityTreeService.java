package br.com.process.integration.database.model.entity.service.example;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityTree;

@Service
@Transactional
public class EntityTreeService extends AbstractJpaService<EntityTree, EntityTree, UUID> {

	@Autowired
	private PagedResourcesAssembler<EntityTree> pagedResourcesAssembler;

	protected EntityTreeService() {
		super(QueryJpaController.class, EntityTree.class);
	}

	@Override
	public EntityTree toModel(EntityTree entity) {
		EntityTree model = new EntityTree();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public void setEntity(EntityTree entity) {
		this.entity = entity;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
