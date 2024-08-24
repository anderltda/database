package br.com.process.integration.database.core.infrastructure;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractAssembler<M extends RepresentationModel<M>> extends RepresentationModelAssemblerSupport<M, M> {

    protected AbstractAssembler(Class<?> controllerClass, Class<M> resourceType) {
        super(controllerClass, resourceType);
    }
    
}
