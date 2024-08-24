package br.com.process.integration.database.domain.service.data;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractDataService;
import br.com.process.integration.database.domain.model.data.EntityOneData;
import br.com.process.integration.database.domain.store.mapper.EntityOneDataMapper;

@Service
@Transactional
public class EntityOneDataService extends AbstractDataService<EntityOneData> {

	public EntityOneDataService(EntityOneDataMapper entityOneMapper) {
		this.mapper = entityOneMapper;
	}

	@Override
	public void setData(EntityOneData entityOneData) {
		this.data = entityOneData;
	}
	
}
