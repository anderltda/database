package br.com.process.integration.database.core.infrastructure;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * @param <M>
 */
@Component
public abstract class AbstractAssembler<M extends RepresentationModel<M>> extends RepresentationModelAssemblerSupport<M, M> {

    /**
     * @param controllerClass
     * @param resourceType
     */
    protected AbstractAssembler(Class<?> controllerClass, Class<M> resourceType) {
        super(controllerClass, resourceType);
    }
    
}
