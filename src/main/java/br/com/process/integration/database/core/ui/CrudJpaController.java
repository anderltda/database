package br.com.process.integration.database.core.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.process.integration.database.core.domain.Entity;
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.reflection.MethodReflection;

@RestController
@RequestMapping("/v1/api-rest-database")
public class CrudJpaController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrudJpaController.class);

	@RequestMapping(value = "/delete/all/{entity}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> deleteAll(@PathVariable String entity) throws ServiceException {

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "deleteAll");

		LOGGER.info("#### service deleteAll {}", entity);

		return new ResponseEntity<String>(HttpStatus.OK);

	}
	
	@RequestMapping(value = "/delete/all/id/{entity}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> deleteAllById(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {
		
		Entity<?> entity_ = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);
		
		List<Object> ids = MethodReflection.transformsJsonIds(jsonNode, entity_);

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "deleteAllById", ids);

		LOGGER.info("#### service deleteAllById {} ids {}", entity, ids);

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete/id/{entity}/{id}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> deleteById(@PathVariable String entity, @PathVariable String id) throws ServiceException {

		setId(entity, id);
		
		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "deleteById");

		LOGGER.info("#### service deleteById id {} tabela {}", id, entity);

		return new ResponseEntity<String>(HttpStatus.OK);

	}
	
	@RequestMapping(value = "/delete/{entity}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> delete(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		Entity<?> entity_ = createEntity(entity, jsonNode);

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "delete");

		final String body = gson.toJson(entity_);

		LOGGER.info("#### service delete {} ", body);

		return new ResponseEntity<String>(HttpStatus.OK);

	}
	
	@RequestMapping(value = "/save/{entity}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> save(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		Entity<?> entity_ = createEntity(entity, jsonNode);

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "save");

		final String body = gson.toJson(entity_);

		LOGGER.info("#### service save {} ", body);

		return new ResponseEntity<String>(body, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/save/flush/{entity}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> saveAndFlush(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		Entity<?> entity_ = createEntity(entity, jsonNode);

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "saveAndFlush");

		final String body = gson.toJson(entity_);

		LOGGER.info("#### service save {} ", body);

		return new ResponseEntity<String>(body, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/save/all/{entity}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> saveAll(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		String method = "saveAll";
		
		String body = persisteAll(method, entity, jsonNode);

		LOGGER.info("#### service {} {} ", method, body);

		return new ResponseEntity<String>(body, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/save/all/flush/{entity}", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> saveAllAndFlush(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		String method = "saveAllAndFlush";
		
		String body = persisteAll(method, entity, jsonNode);

		LOGGER.info("#### service {} {} ", method, body);

		return new ResponseEntity<String>(body, HttpStatus.OK);

	}
	
	private Entity<?> createEntity(String entity, JsonNode jsonNode) throws ServiceException {
		
		Entity<?> entity_ = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		MethodReflection.transformsJsonModel(jsonNode, entity_);
		
		setEntity(entity, entity_);
		
		return entity_;
	}
	
	private String persisteAll(String method, String entity, JsonNode jsonNode) throws ServiceException {

		Entity<?> entity_ = null;
		
		List<Entity<?>> list = new ArrayList<Entity<?>>();

		for (Iterator<JsonNode> iterator = jsonNode.elements(); iterator.hasNext();) {
			
			entity_ = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);
		
			MethodReflection.transformsJsonModel(iterator.next(), entity_);
		
			list.add(entity_);
		}
		
		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), method, list);
		
		String body = gson.toJson(list);

		return body;
	}

}
