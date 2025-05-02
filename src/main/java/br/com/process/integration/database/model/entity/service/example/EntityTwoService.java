package br.com.process.integration.database.model.entity.service.example;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityTwo;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityTwoService extends AbstractJpaService<EntityTwo, EntityTwo, UUID> {
    @Autowired
    private PagedResourcesAssembler<EntityTwo> pagedResourcesAssembler;

    protected EntityTwoService() {
        super(QueryJpaController.class, EntityTwo.class);
    }

    @Override
    public EntityTwo toModel(EntityTwo entity) {
        EntityTwo model = new EntityTwo();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void setEntity(EntityTwo entity) {
        this.entity = entity;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
