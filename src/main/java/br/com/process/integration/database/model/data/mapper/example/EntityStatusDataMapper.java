package br.com.process.integration.database.model.data.mapper.example;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.model.data.dto.example.EntityStatusData;

@Mapper
public interface EntityStatusDataMapper extends DataMapper<EntityStatusData> {

	List<EntityStatusData> findAll();

	EntityStatusData findById(@Param("id") Long id);
}
