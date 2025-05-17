package br.com.process.integration.database.model.view.service.example;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.model.view.dto.example.EntityNineView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityNineViewService extends AbstractJdbcService<EntityNineView> {
    @Autowired
    private PagedResourcesAssembler<EntityNineView> pagedResourcesAssembler;

    protected EntityNineViewService() {
        super(QueryNativeController.class, EntityNineView.class);
    }

    @Override
    public EntityNineView toModel(EntityNineView view) {
        EntityNineView model = new EntityNineView();
        BeanUtils.copyProperties(view, model);
        return model;
    }

    @Override
    public void setView(EntityNineView view) {
        this.view = view;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
