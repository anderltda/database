package br.com.process.integration.database.model.entity.service.example;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityTen;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityTenService extends AbstractJpaService<EntityTen, EntityTen, Long> {
    @Autowired
    private PagedResourcesAssembler<EntityTen> pagedResourcesAssembler;

    protected EntityTenService() {
        super(QueryJpaController.class, EntityTen.class);
    }

    @Override
    public EntityTen toModel(EntityTen entity) {
        EntityTen model = new EntityTen();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setEntity(EntityTen entity) {
        this.entity = entity;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
