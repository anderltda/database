package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntitySevenData;
import br.com.process.integration.database.model.data.mapper.example.EntitySevenDataMapper;

@Service
@Transactional
public class EntitySevenDataService extends AbstractDataService<EntitySevenData> {

	@Autowired
	private PagedResourcesAssembler<EntitySevenData> pagedResourcesAssembler;

	public EntitySevenDataService(EntitySevenDataMapper mapper) {
		super(QueryMyBatisController.class, EntitySevenData.class);
		this.mapper = mapper;
	}

	@Override
	public EntitySevenData toModel(EntitySevenData data) {
		EntitySevenData model = new EntitySevenData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntitySevenData data) {
		this.data = data;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
