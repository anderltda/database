package br.com.process.integration.database.model.view.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.model.view.dto.example.EntityOneView;

@Service
@Transactional
public class EntityOneViewService extends AbstractJdbcService<EntityOneView> {

	@Autowired
	private PagedResourcesAssembler<EntityOneView> pagedResourcesAssembler;

	protected EntityOneViewService() {
		super(QueryNativeController.class, EntityOneView.class);
	}

	@Override
	public EntityOneView toModel(EntityOneView view) {
		EntityOneView model = new EntityOneView();
		BeanUtils.copyProperties(view, model);
		return model;
	}

	@Override
	public void setView(EntityOneView view) {
		this.view = view;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
