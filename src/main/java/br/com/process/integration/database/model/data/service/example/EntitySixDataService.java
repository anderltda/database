package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntitySixData;
import br.com.process.integration.database.model.data.mapper.example.EntitySixDataMapper;

@Service
@Transactional
public class EntitySixDataService extends AbstractDataService<EntitySixData> {

	@Autowired
	private PagedResourcesAssembler<EntitySixData> pagedResourcesAssembler;

	public EntitySixDataService(EntitySixDataMapper mapper) {
		super(QueryMyBatisController.class, EntitySixData.class);
		this.mapper = mapper;
	}

	@Override
	public EntitySixData toModel(EntitySixData data) {
		EntitySixData model = new EntitySixData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntitySixData data) {
		this.data = data;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
