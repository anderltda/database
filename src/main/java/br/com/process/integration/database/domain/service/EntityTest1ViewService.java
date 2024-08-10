package br.com.process.integration.database.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.domain.view.EntityTest1View;
import br.com.process.integration.database.domain.view.assembler.EntityTest1ViewAssembler;

@Service
@Transactional
public class EntityTest1ViewService extends AbstractJdbcService<EntityTest1View> {
	
	@Autowired
	private EntityTest1ViewAssembler entityTest1ViewAssembler;
	
	@Autowired
	private PagedResourcesAssembler<EntityTest1View> pagedResourcesAssembler;
	

	@Override
	public void setView(EntityTest1View entityTest1View) {
		this.view = entityTest1View;
	}
	
	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, entityTest1ViewAssembler);
		
	}
}
