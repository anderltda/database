package br.com.process.integration.database.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.process.integration.database.core.application.AbstractJpaService;
import br.com.process.integration.database.domain.entity.ProductOption;
import br.com.process.integration.database.domain.entity.assembler.ProductOptionAssembler;

@Service
@Transactional
public class ProductOptionService extends AbstractJpaService<ProductOption, Long> {

	@Autowired
	private ProductOptionAssembler productOptionAssembler;
	
	@Autowired
	private PagedResourcesAssembler<ProductOption> pagedResourcesAssembler;
	
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setEntity(ProductOption product) {
		this.entity = product;
	}

	@Override
	public void setPagedModel() {
		pagedModel = pagedResourcesAssembler.toModel(pages, productOptionAssembler);
	}

}
