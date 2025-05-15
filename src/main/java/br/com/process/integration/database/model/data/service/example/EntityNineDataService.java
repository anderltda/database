package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntityNineData;
import br.com.process.integration.database.model.data.mapper.example.EntityNineDataMapper;

@Service
@Transactional
public class EntityNineDataService extends AbstractDataService<EntityNineData> {
	
	@Autowired
	private PagedResourcesAssembler<EntityNineData> pagedResourcesAssembler;

	public EntityNineDataService(EntityNineDataMapper mapper) {
		super(QueryMyBatisController.class, EntityNineData.class);
		this.mapper = mapper;
	}

	@Override
	public EntityNineData toModel(EntityNineData data) {
		EntityNineData model = new EntityNineData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntityNineData data) {
		this.data = data;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
