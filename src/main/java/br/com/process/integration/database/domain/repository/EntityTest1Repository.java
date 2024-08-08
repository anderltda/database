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

import br.com.process.integration.database.domain.entity.EntityTest1;

@Repository
public interface EntityTest1Repository extends JpaRepository<EntityTest1, Long>, JpaSpecificationExecutor<EntityTest1> {
	
	public static final String QUERY_1 = "select e from EntityTest1 e where e.name =(?1)";
	public static final String QUERY_2 = "select e from EntityTest1 e";
	public static final String QUERY_3 = "select e from EntityTest1 e where e.name like CONCAT('%',(?1),'%')";
	public static final String QUERY_4 = "select count(e) from EntityTest1 e where e.prohibited >=(?1)";

	@Query(QUERY_1)
	EntityTest1 buscaComEqualPeloName(String name);
	
	@Query(QUERY_2)
	List<EntityTest1> buscaAll(Sort sort);
	
	@Query(value = QUERY_2)
	Page<EntityTest1> buscaAll(PageRequest pageRequest);

	@Query(QUERY_3)
	EntityTest1 buscaComLikePeloName(String name);

	@Query(QUERY_3)
	List<EntityTest1> buscaComLikePeloName(String name, Sort sort);
	
	@Query(QUERY_3)
	Page<EntityTest1> buscaComLikePeloName(String name, PageRequest pageRequest);
	
	@Query(value = QUERY_4)
	Long countMaiorProhibited(LocalDateTime prohibited);
}
