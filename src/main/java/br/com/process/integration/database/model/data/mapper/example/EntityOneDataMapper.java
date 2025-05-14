package br.com.process.integration.database.model.data.mapper.example;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.model.data.dto.example.EntityOneData;

@Mapper
public interface EntityOneDataMapper extends DataMapper<EntityOneData> {

	List<EntityOneData> findAll();

	EntityOneData findById(@Param("idEntityOne") Long idEntityOne);

	int countEntitiesByName(@Param("name") String name);

	EntityOneData methodNoMapping(@Param("name") String name);

	EntityOneData findEntityDataByName(@Param("name") String name);

	List<EntityOneData> findEntityOneByName(@Param("name") String name, 
			@Param("height") Double height,
			@Param(Constants.SORT_LIST) List<String> sortList, 
			@Param(Constants.SORT_ORDERS) List<String> sortOrders);

	List<EntityOneData> findEntityOneByAll(@Param("code") Boolean code, 
			@Param(Constants.NAME_PAGE) Integer page,
			@Param(Constants.NAME_SIZE) Integer size, 
			@Param(Constants.SORT_LIST) List<String> sortList,
			@Param(Constants.SORT_ORDERS) List<String> sortOrders);

	int countFindEntityOneByAll(@Param("code") Boolean code);
}
