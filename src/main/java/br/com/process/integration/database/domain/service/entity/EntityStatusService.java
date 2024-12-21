package br.com.process.integration.database.domain.service.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.domain.model.entity.EntityStatus;

@Service
@Transactional
public class EntityStatusService extends AbstractJpaService<EntityStatus, EntityStatus, Long> {

	@Autowired
	private PagedResourcesAssembler<EntityStatus> pagedResourcesAssembler;

	protected EntityStatusService() {
		super(QueryJpaController.class, EntityStatus.class);
	}

	@Override
	public EntityStatus toModel(EntityStatus entity) {
		EntityStatus model = new EntityStatus();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setEntity(EntityStatus entity) {
		this.entity = entity;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
