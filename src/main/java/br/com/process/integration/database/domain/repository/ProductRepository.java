package br.com.process.integration.database.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.domain.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	public static final String QUERY_1 = "select p from Product p where p.id =(?1)";
	
	public static final String QUERY_2 = "select p from Product p where p.title like CONCAT('%',(?1),'%') and p.type =(?2)";
	
	public static final String QUERY_3 = "select p from Product p where p.type =(?1) and p.brand =(?2)";
	
	public static final String QUERY_4 = "select p from Product p where p.scheduleDate >=(?1) and p.endDate >=(?2)";
	
	public static final String QUERY_5 = "select count(p) from Product p where p.scheduleDate >=(?1) and p.endDate >=(?2)";
	
	@Query(QUERY_1)
	Product findByMyId(Long id);
	
	@Query(QUERY_2)
	Product findByTitleAndType(String title, String type);
	
	@Query(QUERY_3)
	List<Product> findByTitleAndTypeLike(String title, String type);
	
	@Query(QUERY_4)
	List<Product> findBySchedule(LocalDate schedule, LocalDateTime endDate);

	@Query(value = QUERY_4)
	Page<Product> findBySchedulePaginate(LocalDate schedule, LocalDateTime endDate, PageRequest pageRequest);
	
	@Query(value = QUERY_5)
	Long countBrand(LocalDate schedule, LocalDateTime endDate);
}
