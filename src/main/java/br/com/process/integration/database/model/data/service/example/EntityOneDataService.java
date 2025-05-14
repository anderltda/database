package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntityOneData;
import br.com.process.integration.database.model.data.mapper.example.EntityOneDataMapper;

@Service
@Transactional
public class EntityOneDataService extends AbstractDataService<EntityOneData> {

	@Autowired
	private PagedResourcesAssembler<EntityOneData> pagedResourcesAssembler;

	public EntityOneDataService(EntityOneDataMapper mapper) {
		super(QueryMyBatisController.class, EntityOneData.class);
		this.mapper = mapper;
	}

	@Override
	public EntityOneData toModel(EntityOneData data) {
		EntityOneData model = new EntityOneData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntityOneData data) {
		this.data = data;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
