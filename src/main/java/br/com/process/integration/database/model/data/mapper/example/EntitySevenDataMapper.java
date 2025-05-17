package br.com.process.integration.database.model.data.mapper.example;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.model.data.dto.example.EntitySevenData;
import br.com.process.integration.database.model.data.dto.example.EntitySevenId;

@Mapper
public interface EntitySevenDataMapper extends DataMapper<EntitySevenData> {

	List<EntitySevenData> findAll();

	EntitySevenData findById(@Param("id") EntitySevenId id);
}
