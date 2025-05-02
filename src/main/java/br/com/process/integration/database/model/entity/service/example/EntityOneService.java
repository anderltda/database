package br.com.process.integration.database.model.entity.service.example;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityOne;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityOneService extends AbstractJpaService<EntityOne, EntityOne, Long> {
    @Autowired
    private PagedResourcesAssembler<EntityOne> pagedResourcesAssembler;

    protected EntityOneService() {
        super(QueryJpaController.class, EntityOne.class);
    }

    @Override
    public EntityOne toModel(EntityOne entity) {
        EntityOne model = new EntityOne();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setEntity(EntityOne entity) {
        this.entity = entity;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
