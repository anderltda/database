package br.com.process.integration.database.domain.service.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.domain.view.assembler.EntityOneViewAssembler;
import br.com.process.integration.database.domain.view.test.EntityOneView;

@Service
@Transactional
public class EntityOneViewService extends AbstractJdbcService<EntityOneView> {
	
	@Autowired
	private EntityOneViewAssembler entityOneViewAssembler;
	
	@Autowired
	private PagedResourcesAssembler<EntityOneView> pagedResourcesAssembler;
	

	@Override
	public void setView(EntityOneView entityOneView) {
		this.view = entityOneView;
	}
	
	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, entityOneViewAssembler);
		
	}
}
