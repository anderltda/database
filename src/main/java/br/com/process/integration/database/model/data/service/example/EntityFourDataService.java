package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntityFourData;
import br.com.process.integration.database.model.data.mapper.example.EntityFourDataMapper;

@Service
@Transactional
public class EntityFourDataService extends AbstractDataService<EntityFourData> {

	@Autowired
	private PagedResourcesAssembler<EntityFourData> pagedResourcesAssembler;

	public EntityFourDataService(EntityFourDataMapper mapper) {
		super(QueryMyBatisController.class, EntityFourData.class);
		this.mapper = mapper;
	}

	@Override
	public EntityFourData toModel(EntityFourData data) {
		EntityFourData model = new EntityFourData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntityFourData data) {
		this.data = data;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
