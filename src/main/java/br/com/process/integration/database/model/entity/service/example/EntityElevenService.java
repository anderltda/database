package br.com.process.integration.database.model.entity.service.example;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.model.entity.dto.example.EntityEleven;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityElevenService extends AbstractJpaService<EntityEleven, EntityEleven, Long> {
    @Autowired
    private PagedResourcesAssembler<EntityEleven> pagedResourcesAssembler;

    protected EntityElevenService() {
        super(QueryJpaController.class, EntityEleven.class);
    }

    @Override
    public EntityEleven toModel(EntityEleven entity) {
        EntityEleven model = new EntityEleven();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void setEntity(EntityEleven entity) {
        this.entity = entity;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
