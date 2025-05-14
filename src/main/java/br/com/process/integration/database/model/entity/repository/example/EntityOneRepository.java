package br.com.process.integration.database.model.entity.repository.example;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.model.entity.dto.example.EntityOne;

@Repository
public interface EntityOneRepository extends JpaRepository<EntityOne, Long>, JpaSpecificationExecutor<EntityOne> {

	public static final String QUERY_1 = "select e from EntityOne e where e.name =(?1)";
	public static final String QUERY_2 = "select e from EntityOne e";
	public static final String QUERY_3 = "select e from EntityOne e where e.name like CONCAT('%',(?1),'%') AND e.height <> ?2";
	public static final String QUERY_4 = "select count(e) from EntityOne e where e.prohibitedDateTime >=(?1)";
	public static final String QUERY_5 = "select count(e) from EntityOne e where e.age >=(?1)";
	public static final String QUERY_6 = "select e from EntityOne e where e.age =(?1)";
	public static final String QUERY_7 = "select e from EntityOne e where e.code =(?1)";

	@Query(QUERY_1)
	EntityOne buscaComEqualPeloName(String name);

	@Query(QUERY_2)
	List<EntityOne> buscaAll(Sort sort);

	@Query(value = QUERY_2)
	Page<EntityOne> buscaAll(PageRequest pageRequest);

	@Query(value = QUERY_7)
	Page<EntityOne> buscaAllParam(Boolean code, PageRequest pageRequest);

	@Query(QUERY_3)
	EntityOne buscaComLikePeloName(String name, Double height);

	@Query(QUERY_3)
	List<EntityOne> buscaComLikePeloName(String name, Double height, Sort sort);

	@Query(QUERY_3)
	Page<EntityOne> buscaComLikePeloName(String name, Double height, PageRequest pageRequest);

	@Query(value = QUERY_4)
	int countMaiorProhibited(LocalDateTime prohibitedDateTime);

	@Query(value = QUERY_5)
	int countMaiorAge(String age);

	@Query(QUERY_6)
	List<EntityOne> buscaAll(String age, Sort sort);

	@Query(QUERY_6)
	List<EntityOne> buscaAll(String age);

	@Query(value = QUERY_6)
	Page<EntityOne> buscaAll(String age, PageRequest pageRequest);

}
