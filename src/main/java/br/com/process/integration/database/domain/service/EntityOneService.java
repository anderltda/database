package br.com.process.integration.database.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.domain.entity.EntityOne;
import br.com.process.integration.database.domain.entity.assembler.EntityOneAssembler;

@Service
@Transactional
public class EntityOneService extends AbstractJpaService<EntityOne, Long> {

	@Autowired
	private EntityOneAssembler entity1ModelAssembler;
	
	@Autowired
	private PagedResourcesAssembler<EntityOne> pagedResourcesAssembler;
	
	
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
		pagedModel = pagedResourcesAssembler.toModel(pages, entity1ModelAssembler);
	}

}
