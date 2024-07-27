package br.com.process.integration.database.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.domain.entity.ProductCategory;
import br.com.process.integration.database.domain.entity.assembler.ProductCategoryAssembler;

@Service
@Transactional
public class ProductCategoryService extends AbstractJpaService<ProductCategory, Long> {

	@Autowired
	private ProductCategoryAssembler productCategoryAssembler;
	
	@Autowired
	private PagedResourcesAssembler<ProductCategory> pagedResourcesAssembler;
	
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setEntity(ProductCategory product) {
		this.entity = product;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, productCategoryAssembler);
	}

}
