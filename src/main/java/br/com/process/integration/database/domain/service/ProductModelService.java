package br.com.process.integration.database.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.domain.model.ProductModel;
import br.com.process.integration.database.domain.model.assembler.ProductModelAssembler;

@Service
@Transactional
public class ProductModelService extends AbstractJdbcService<ProductModel> {
	
	@Autowired
	private ProductModelAssembler productModelAssembler;
	
	@Autowired
	private PagedResourcesAssembler<ProductModel> pagedResourcesAssembler;
	

	@Override
	public void setModel(ProductModel productModel) {
		this.model = productModel;
	}
	
	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, productModelAssembler);
		
	}
}
