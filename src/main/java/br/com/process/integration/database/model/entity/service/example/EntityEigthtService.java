package br.com.process.integration.database.model.entity.service.example;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityEigtht;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityEigthtService extends AbstractJpaService<EntityEigtht, EntityEigtht, Long> {
    @Autowired
    private PagedResourcesAssembler<EntityEigtht> pagedResourcesAssembler;

    protected EntityEigthtService() {
        super(QueryJpaController.class, EntityEigtht.class);
    }

    @Override
    public EntityEigtht toModel(EntityEigtht entity) {
        EntityEigtht model = new EntityEigtht();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setEntity(EntityEigtht entity) {
        this.entity = entity;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
