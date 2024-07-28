package br.com.process.integration.database.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJdbcService;
import br.com.process.integration.database.domain.view.ProductView;
import br.com.process.integration.database.domain.view.assembler.ProductViewAssembler;

@Service
@Transactional
public class ProductViewService extends AbstractJdbcService<ProductView> {
	
	@Autowired
	private ProductViewAssembler productModelAssembler;
	
	@Autowired
	private PagedResourcesAssembler<ProductView> pagedResourcesAssembler;
	

	@Override
	public void setView(ProductView productModel) {
		this.view = productModel;
	}
	
	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, productModelAssembler);
		
	}
}
