package br.com.process.integration.database.domain.service.view;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.domain.model.view.EntityOneView;

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
	public void setView(EntityOneView entityOneView) {
		this.view = entityOneView;
	}
	
	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}

}
