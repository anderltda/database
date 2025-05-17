package br.com.process.integration.database.model.data.mapper.example;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.model.data.dto.example.EntitySixData;

@Mapper
public interface EntitySixDataMapper extends DataMapper<EntitySixData> {

	List<EntitySixData> findAll();

	EntitySixData findById(@Param("id") Long id);
}
