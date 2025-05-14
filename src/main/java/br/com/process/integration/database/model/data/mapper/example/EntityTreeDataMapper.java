package br.com.process.integration.database.model.data.mapper.example;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.model.data.dto.example.EntityTreeData;

@Mapper
public interface EntityTreeDataMapper extends DataMapper<EntityTreeData> {

	List<EntityTreeData> findAll();

	EntityTreeData findById(@Param("idEntityTree") UUID idEntityTree);
}
