package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntityFiveData;
import br.com.process.integration.database.model.data.mapper.example.EntityFiveDataMapper;

@Service
@Transactional
public class EntityFiveDataService extends AbstractDataService<EntityFiveData> {

	@Autowired
	private PagedResourcesAssembler<EntityFiveData> pagedResourcesAssembler;

	public EntityFiveDataService(EntityFiveDataMapper mapper) {
		super(QueryMyBatisController.class, EntityFiveData.class);
		this.mapper = mapper;
	}

	@Override
	public EntityFiveData toModel(EntityFiveData data) {
		EntityFiveData model = new EntityFiveData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntityFiveData data) {
		this.data = data;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
