package br.com.process.integration.database.domain.model.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryJpaController;
import br.com.process.integration.database.domain.model.ProductModel;

@Component
public class ProductModelAssembler extends RepresentationModelAssemblerSupport<ProductModel, ProductModel> {

	public ProductModelAssembler() {
		super(QueryJpaController.class, ProductModel.class);
	}

	@Override
	public ProductModel toModel(ProductModel entity) {
		ProductModel model = new ProductModel();
		BeanUtils.copyProperties(entity, model);
		return model;
	}


}
