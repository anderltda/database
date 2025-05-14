package br.com.process.integration.database.model.data.mapper.example;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import br.com.process.integration.database.core.domain.DataMapper;
import br.com.process.integration.database.model.data.dto.example.EntityEightData;

@Mapper
public interface EntityEightDataMapper extends DataMapper<EntityEightData> {

	List<EntityEightData> findAll();

	EntityEightData findById(@Param("idEntityEight") Long idEntityEight);
}
