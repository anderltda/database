package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntityTreeData;
import br.com.process.integration.database.model.data.mapper.example.EntityTreeDataMapper;

@Service
@Transactional
public class EntityTreeDataService extends AbstractDataService<EntityTreeData> {

	@Autowired
	private PagedResourcesAssembler<EntityTreeData> pagedResourcesAssembler;

	public EntityTreeDataService(EntityTreeDataMapper mapper) {
		super(QueryMyBatisController.class, EntityTreeData.class);
		this.mapper = mapper;
	}

	@Override
	public EntityTreeData toModel(EntityTreeData data) {
		EntityTreeData model = new EntityTreeData();
		BeanUtils.copyProperties(data, model);
		return model;
	}

	@Override
	public void setData(EntityTreeData data) {
		this.data = data;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
