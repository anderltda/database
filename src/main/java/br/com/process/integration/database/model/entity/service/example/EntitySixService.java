package br.com.process.integration.database.model.entity.service.example;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntitySix;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntitySixService extends AbstractJpaService<EntitySix, EntitySix, Integer> {
    @Autowired
    private PagedResourcesAssembler<EntitySix> pagedResourcesAssembler;

    protected EntitySixService() {
        super(QueryJpaController.class, EntitySix.class);
    }

    @Override
    public EntitySix toModel(EntitySix entity) {
        EntitySix model = new EntitySix();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setEntity(EntitySix entity) {
        this.entity = entity;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
