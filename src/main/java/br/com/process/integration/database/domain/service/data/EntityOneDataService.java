package br.com.process.integration.database.domain.service.data;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.domain.model.data.EntityOneData;
import br.com.process.integration.database.domain.store.mapper.EntityOneDataMapper;

@Service
@Transactional
public class EntityOneDataService extends AbstractDataService<EntityOneData> {

	@Autowired
	private PagedResourcesAssembler<EntityOneData> pagedResourcesAssembler;

	public EntityOneDataService(EntityOneDataMapper entityOneMapper) {
		super(QueryMyBatisController.class, EntityOneData.class);
		this.mapper = entityOneMapper;
	}
	
	@Override
	public EntityOneData toModel(EntityOneData data) {
		EntityOneData model = new EntityOneData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntityOneData entityOneData) {
		this.data = entityOneData;
	}
	
	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
	
}
