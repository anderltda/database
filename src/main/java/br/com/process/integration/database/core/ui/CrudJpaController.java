package br.com.process.integration.database.core.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.process.integration.database.core.domain.Entity;
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.reflection.MethodReflection;

@RestController
@RequestMapping("/v1/api-rest-database")
public class CrudJpaController extends AbstractController {


	@DeleteMapping(value = "/delete/all/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> deleteAll(@PathVariable String entity) throws ServiceException {

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "deleteAll");

		return new ResponseEntity<>(HttpStatus.OK);

	}
	
	@DeleteMapping(value = "/delete/all/id/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> deleteAllById(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {
		
		List<Object> ids = MethodReflection.transformsJsonIds(jsonNode);

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "deleteAllById", ids);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/delete/id/{entity}/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> deleteById(@PathVariable String entity, @PathVariable String id) throws ServiceException {

		setId(entity, id);
		
		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "deleteById");

		return new ResponseEntity<>(HttpStatus.OK);

	}
	
	@DeleteMapping(value = "/delete/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> delete(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "delete");

		return new ResponseEntity<>(HttpStatus.OK);

	}
	
	@PostMapping(value = "/save/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> save(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		Entity<?> entityFound = createEntity(entity, jsonNode);

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "save");

		final String body = gson.toJson(entityFound);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}
	
	@PostMapping(value = "/save/flush/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> saveAndFlush(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		Entity<?> entityFound = createEntity(entity, jsonNode);

		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), "saveAndFlush");

		final String body = gson.toJson(entityFound);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}
	
	@PostMapping(value = "/save/all/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> saveAll(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		String body = persisteAll("saveAll", entity, jsonNode);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}
	
	@PostMapping(value = "/save/all/flush/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> saveAllAndFlush(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws ServiceException {

		String body = persisteAll("saveAllAndFlush", entity, jsonNode);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}
	
	private Entity<?> createEntity(String entity, JsonNode jsonNode) throws ServiceException {
		
		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		MethodReflection.transformsJsonModel(jsonNode, entityFound);
		
		setEntity(entity, entityFound);
		
		return entityFound;
	}
	
	private String persisteAll(String method, String entity, JsonNode jsonNode) throws ServiceException {

		Entity<?> entityFound = null;
		
		List<Entity<?>> list = new ArrayList<>();

		for (Iterator<JsonNode> iterator = jsonNode.elements(); iterator.hasNext();) {
			
			entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);
		
			MethodReflection.transformsJsonModel(iterator.next(), entityFound);
		
			list.add(entityFound);
		}
		
		methodInvoker.invokeMethodWithParameters(MethodReflection.getNameService(entity), method, list);
		
		return gson.toJson(list);
	}

}
