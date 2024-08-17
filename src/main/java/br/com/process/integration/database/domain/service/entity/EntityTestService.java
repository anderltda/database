package br.com.process.integration.database.domain.service.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.domain.entity.assembler.EntityTestAssembler;
import br.com.process.integration.database.domain.entity.test.EntityTest;

@Service
@Transactional
public class EntityTestService extends AbstractJpaService<EntityTest, Long> {

	@Autowired
	private EntityTestAssembler entityTestAssembler;
	
	@Autowired
	private PagedResourcesAssembler<EntityTest> pagedResourcesAssembler;
	
	
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
		pagedModel = pagedResourcesAssembler.toModel(pages, entityTestAssembler);
	}

}
