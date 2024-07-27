package br.com.process.integration.database.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.process.integration.database.domain.entity.Product;
import br.com.process.integration.database.domain.entity.ProductOption;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long>, JpaSpecificationExecutor<ProductOption> {

	public static final String QUERY_1 = "select po from ProductOption po where po.id =(?1)";
	
	@Query(QUERY_1)
	Product findByMyId(Long id);

}
