package br.com.process.integration.database.domain.store.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.domain.model.data.EntityOneData;

@Mapper
public interface EntityOneDataMapper extends DataMapper<EntityOneData> {

	int countEntitiesByName(@Param("name") String name);

	EntityOneData methodNoMapping(@Param("name") String name);

	EntityOneData findEntityDataByName(@Param("name") String name);

	List<EntityOneData> findEntityOneByName(@Param("name") String name);

}
