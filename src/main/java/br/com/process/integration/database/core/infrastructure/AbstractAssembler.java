package br.com.process.integration.database.core.infrastructure;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public abstract class AbstractAssembler<V extends RepresentationModel<V>> extends RepresentationModelAssemblerSupport<V, V> {

    protected AbstractAssembler(Class<?> controllerClass, Class<V> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public abstract V toModel(V view);
}
