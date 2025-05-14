package br.com.process.integration.database.model.data.service.example;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.core.ui.QueryMyBatisController;
import br.com.process.integration.database.model.data.dto.example.EntityEightData;
import br.com.process.integration.database.model.data.mapper.example.EntityEightDataMapper;

@Service
@Transactional
public class EntityEightDataService extends AbstractDataService<EntityEightData> {
	
    @Autowired
    private PagedResourcesAssembler<EntityEightData> pagedResourcesAssembler;

    public EntityEightDataService(EntityEightDataMapper mapper) {
        super(QueryMyBatisController.class, EntityEightData.class);
        this.mapper = mapper;
    }

    @Override
    public EntityEightData toModel(EntityEightData data) {
        EntityEightData model = new EntityEightData();
        BeanUtils.copyProperties(data, model);
        return model;
    }

    @Override
    public void setData(EntityEightData data) {
        this.data = data;
    }

    @Override
    public void setPagedModel() {
        pagedModel = pagedResourcesAssembler.toModel(pages, this);
    }
}
