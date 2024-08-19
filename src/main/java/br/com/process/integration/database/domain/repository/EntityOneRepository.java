package br.com.process.integration.database.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.domain.model.entity.EntityOne;

@Repository
public interface EntityOneRepository extends JpaRepository<EntityOne, Long>, JpaSpecificationExecutor<EntityOne> {
	
	public static final String QUERY_1 = "select e from EntityOne e where e.name =(?1)";
	public static final String QUERY_2 = "select e from EntityOne e";
	public static final String QUERY_3 = "select e from EntityOne e where e.name like CONCAT('%',(?1),'%')";
	public static final String QUERY_4 = "select count(e) from EntityOne e where e.prohibited >=(?1)";
	public static final String QUERY_5 = "select count(e) from EntityOne e where e.age >=(?1)";
	public static final String QUERY_6 = "select e from EntityOne e where e.age =(?1)";

	@Query(QUERY_1)
	EntityOne buscaComEqualPeloName(String name);
	
	@Query(QUERY_2)
	List<EntityOne> buscaAll(Sort sort);
	
	@Query(value = QUERY_2)
	Page<EntityOne> buscaAll(PageRequest pageRequest);

	@Query(QUERY_3)
	EntityOne buscaComLikePeloName(String name);

	@Query(QUERY_3)
	List<EntityOne> buscaComLikePeloName(String name, Sort sort);
	
	@Query(QUERY_3)
	Page<EntityOne> buscaComLikePeloName(String name, PageRequest pageRequest);
	
	@Query(value = QUERY_4)
	Long countMaiorProhibited(LocalDateTime prohibited);
	
	@Query(value = QUERY_5)
	Long countMaiorAge(String age);
	
	@Query(QUERY_6)
	List<EntityOne> buscaAll(String age, Sort sort);
	
	@Query(value = QUERY_6)
	Page<EntityOne> buscaAll(String age, PageRequest pageRequest);
}
