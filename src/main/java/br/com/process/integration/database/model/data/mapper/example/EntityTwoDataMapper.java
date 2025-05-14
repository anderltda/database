package br.com.process.integration.database.model.data.mapper.example;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.model.data.dto.example.EntityTwoData;

@Mapper
public interface EntityTwoDataMapper extends DataMapper<EntityTwoData> {

	List<EntityTwoData> findAll();

	EntityTwoData findById(@Param("idEntityTwo") UUID idEntityTwo);
}
