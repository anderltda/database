package br.com.process.integration.database.model.view.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.model.view.dto.example.EntityEightView;

@Service
@Transactional
public class EntityEightViewService extends AbstractJdbcService<EntityEightView> {

	@Autowired
	private PagedResourcesAssembler<EntityEightView> pagedResourcesAssembler;

	protected EntityEightViewService() {
		super(QueryNativeController.class, EntityEightView.class);
	}

	@Override
	public EntityEightView toModel(EntityEightView view) {
		EntityEightView model = new EntityEightView();
		BeanUtils.copyProperties(view, model);
		return model;
	}

	@Override
	public void setView(EntityEightView view) {
		this.view = view;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, this);
	}
}
