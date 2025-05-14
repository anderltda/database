package br.com.process.integration.database.model.entity.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityNine;
import br.com.process.integration.database.model.entity.dto.example.EntityNineId;

@Service
@Transactional
public class EntityNineService extends AbstractJpaService<EntityNine, EntityNine, EntityNineId> {
	
    @Autowired
    private PagedResourcesAssembler<EntityNine> pagedResourcesAssembler;

    protected EntityNineService() {
        super(QueryJpaController.class, EntityNine.class);
    }

    @Override
    public EntityNine toModel(EntityNine entity) {
        EntityNine model = new EntityNine();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public void setId(EntityNineId id) {
        this.id = id;
    }

    @Override
    public void setEntity(EntityNine entity) {
        this.entity = entity;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
