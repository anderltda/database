package br.com.process.integration.database.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.domain.entity.Product;
import br.com.process.integration.database.domain.entity.assembler.ProductAssembler;

@Service
@Transactional
public class ProductService extends AbstractJpaService<Product, Long> {

	@Autowired
	private ProductAssembler productModelAssembler;
	
	@Autowired
	private PagedResourcesAssembler<Product> pagedResourcesAssembler;
	
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setEntity(Product product) {
		this.entity = product;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, productModelAssembler);
	}

}
