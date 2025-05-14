package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntityStatusData;
import br.com.process.integration.database.model.data.mapper.example.EntityStatusDataMapper;

@Service
@Transactional
public class EntityStatusDataService extends AbstractDataService<EntityStatusData> {

	@Autowired
	private PagedResourcesAssembler<EntityStatusData> pagedResourcesAssembler;

	public EntityStatusDataService(EntityStatusDataMapper mapper) {
		super(QueryMyBatisController.class, EntityStatusData.class);
		this.mapper = mapper;
	}

	@Override
	public EntityStatusData toModel(EntityStatusData data) {
		EntityStatusData model = new EntityStatusData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntityStatusData data) {
		this.data = data;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
