package br.com.process.integration.database.model.entity.service.example;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityRow;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityRowService extends AbstractJpaService<EntityRow, EntityRow, Long> {
    @Autowired
    private PagedResourcesAssembler<EntityRow> pagedResourcesAssembler;

    protected EntityRowService() {
        super(QueryJpaController.class, EntityRow.class);
    }

    @Override
    public EntityRow toModel(EntityRow entity) {
        EntityRow model = new EntityRow();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setEntity(EntityRow entity) {
        this.entity = entity;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
