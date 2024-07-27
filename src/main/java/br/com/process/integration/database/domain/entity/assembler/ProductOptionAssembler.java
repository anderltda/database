package br.com.process.integration.database.domain.entity.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.domain.entity.ProductOption;

@Component
public class ProductOptionAssembler extends RepresentationModelAssemblerSupport<ProductOption, ProductOption> {

	public ProductOptionAssembler() {
		super(QueryJpaController.class, ProductOption.class);
	}

	@Override
	public ProductOption toModel(ProductOption entity) {
		ProductOption model = new ProductOption();
		BeanUtils.copyProperties(entity, model);
		return model;
	}


}
