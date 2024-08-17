package br.com.process.integration.database.domain.service.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.domain.model.entity.EntityOne;

@Service
@Transactional
public class EntityOneService extends AbstractJpaService<EntityOne, EntityOne, Long> {

	@Autowired
	private PagedResourcesAssembler<EntityOne> pagedResourcesAssembler;

	protected EntityOneService() {
		super(QueryNativeController.class, EntityOne.class);
	}

	@Override
	public EntityOne toModel(EntityOne entity) {
		EntityOne model = new EntityOne();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setEntity(EntityOne entity1) {
		this.entity = entity1;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
