package br.com.process.integration.database.model.data.mapper.example;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.model.data.dto.example.EntityFourData;

@Mapper
public interface EntityFourDataMapper extends DataMapper<EntityFourData> {

	List<EntityFourData> findAll();

	EntityFourData findById(@Param("id") UUID id);
}
