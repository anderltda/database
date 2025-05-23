package br.com.process.integration.database.model.data.mapper.example;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.model.data.dto.example.EntityNineData;
import br.com.process.integration.database.model.data.dto.example.EntityNineDataId;

@Mapper
public interface EntityNineDataMapper extends DataMapper<EntityNineData> {

	List<EntityNineData> findAll();

	EntityNineData findById(@Param("id") EntityNineDataId id);
}
