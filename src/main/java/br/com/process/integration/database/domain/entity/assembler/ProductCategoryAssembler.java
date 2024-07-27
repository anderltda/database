package br.com.process.integration.database.domain.entity.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.domain.entity.ProductCategory;

@Component
public class ProductCategoryAssembler extends RepresentationModelAssemblerSupport<ProductCategory, ProductCategory> {

	public ProductCategoryAssembler() {
		super(QueryJpaController.class, ProductCategory.class);
	}

	@Override
	public ProductCategory toModel(ProductCategory entity) {
		ProductCategory model = new ProductCategory();
		BeanUtils.copyProperties(entity, model);
		return model;
	}


}
