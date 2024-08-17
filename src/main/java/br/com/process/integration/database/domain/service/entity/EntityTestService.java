package br.com.process.integration.database.domain.service.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.domain.model.entity.EntityTest;

@Service
@Transactional
public class EntityTestService extends AbstractJpaService<EntityTest, EntityTest, Long> {

	@Autowired
	private PagedResourcesAssembler<EntityTest> pagedResourcesAssembler;

	protected EntityTestService() {
		super(QueryNativeController.class, EntityTest.class);
	}
	
	@Override
	public EntityTest toModel(EntityTest entity) {
		EntityTest model = new EntityTest();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setEntity(EntityTest entity1) {
		this.entity = entity1;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
