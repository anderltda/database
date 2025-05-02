package br.com.process.integration.database.model.view.service.example;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.model.view.dto.example.EntityEigthtView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityEigthtViewService extends AbstractJdbcService<EntityEigthtView> {
    @Autowired
    private PagedResourcesAssembler<EntityEigthtView> pagedResourcesAssembler;

    protected EntityEigthtViewService() {
        super(QueryNativeController.class, EntityEigthtView.class);
    }

    @Override
    public EntityEigthtView toModel(EntityEigthtView view) {
        EntityEigthtView model = new EntityEigthtView();
        BeanUtils.copyProperties(view, model);
        return model;
    }

    @Override
    public void setView(EntityEigthtView view) {
        this.view = view;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
