package br.com.process.integration.database.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.domain.entity.Product;
import br.com.process.integration.database.domain.entity.ProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, JpaSpecificationExecutor<ProductCategory> {

	public static final String QUERY_1 = "select pc from ProductCategory pc where pc.id =(?1)";
	
	@Query(QUERY_1)
	Product findByMyId(Long id);
}
