package br.com.process.integration.database.domain.view.assembler;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.process.integration.database.core.ui.QueryNativeController;
import br.com.process.integration.database.domain.view.ProductView;

@Component
public class ProductViewAssembler extends RepresentationModelAssemblerSupport<ProductView, ProductView> {

	public ProductViewAssembler() {
		super(QueryNativeController.class, ProductView.class);
	}

	@Override
	public ProductView toModel(ProductView entity) {
		ProductView model = new ProductView();
		BeanUtils.copyProperties(entity, model);
		return model;
	}


}
