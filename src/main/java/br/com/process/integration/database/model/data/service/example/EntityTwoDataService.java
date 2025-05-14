package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntityTwoData;
import br.com.process.integration.database.model.data.mapper.example.EntityTwoDataMapper;

@Service
@Transactional
public class EntityTwoDataService extends AbstractDataService<EntityTwoData> {

	@Autowired
	private PagedResourcesAssembler<EntityTwoData> pagedResourcesAssembler;

	public EntityTwoDataService(EntityTwoDataMapper mapper) {
		super(QueryMyBatisController.class, EntityTwoData.class);
		this.mapper = mapper;
	}

	@Override
	public EntityTwoData toModel(EntityTwoData data) {
		EntityTwoData model = new EntityTwoData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntityTwoData data) {
		this.data = data;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
