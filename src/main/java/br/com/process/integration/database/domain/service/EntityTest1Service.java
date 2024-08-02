package br.com.process.integration.database.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.domain.entity.EntityTest1;
import br.com.process.integration.database.domain.entity.assembler.EntityTest1Assembler;

@Service
@Transactional
public class EntityTest1Service extends AbstractJpaService<EntityTest1, Long> {

	@Autowired
	private EntityTest1Assembler entity1ModelAssembler;
	
	@Autowired
	private PagedResourcesAssembler<EntityTest1> pagedResourcesAssembler;
	
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setEntity(EntityTest1 entity1) {
		this.entity = entity1;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, entity1ModelAssembler);
	}

}
