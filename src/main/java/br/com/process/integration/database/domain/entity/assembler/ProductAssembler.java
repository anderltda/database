package br.com.process.integration.database.domain.entity.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.domain.entity.Product;

@Component
public class ProductAssembler extends RepresentationModelAssemblerSupport<Product, Product> {

	public ProductAssembler() {
		super(QueryJpaController.class, Product.class);
	}

	@Override
	public Product toModel(Product entity) {
		Product model = new Product();
		BeanUtils.copyProperties(entity, model);
		return model;
	}


}
