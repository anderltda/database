package br.com.process.integration.database.core.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.process.integration.database.core.Constants;
import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.utils.StringsUtils;

/**
 * 
 */
@RestController
@RequestMapping("/v1/database")
public class CrudJpaController extends AbstractController {
	
	private final ObjectMapper objectMapper;

	/**
	 * @param objectMapper
	 */
	public CrudJpaController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * @param entity
	 * @param id
	 * @return
	 * @throws CheckedException
	 */
	@DeleteMapping(value = "/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> deleteAllOrById(@PathVariable String entity, @RequestParam(defaultValue = "") List<String> id) throws CheckedException {

		if (id.isEmpty()) {
			methodInvoker.invokeMethodWithParameters(StringsUtils.getNameService(entity), Constants.METHOD_DELETE_ALL);
		} else {
			methodInvoker.invokeMethodWithParameters(StringsUtils.getNameService(entity), Constants.METHOD_DELETE_ALL_BY_ID, id);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * @param entity
	 * @param id
	 * @return
	 * @throws CheckedException
	 */
	@DeleteMapping(value = "/{entity}/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> deleteById(@PathVariable String entity, @PathVariable String id) throws CheckedException {

		setId(entity, id);

		methodInvoker.invokeMethodWithParameters(StringsUtils.getNameService(entity), Constants.METHOD_DELETE_BY_ID);

		return new ResponseEntity<>(HttpStatus.OK);

	}
	
	/**
	 * @param entity
	 * @param ids
	 * @return
	 * @throws CheckedException
	 */
	@DeleteMapping(value = "/{entity}/ids", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> deleteById(@PathVariable String entity, @RequestParam(defaultValue = "") Map<String, Object> ids) throws CheckedException {

		setId(entity, ids);

		methodInvoker.invokeMethodWithParameters(StringsUtils.getNameService(entity), Constants.METHOD_DELETE_BY_ID);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * @param entity
	 * @param jsonNode
	 * @return
	 * @throws CheckedException
	 */
	@PostMapping(value = "/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BeanEntity<?>> save(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws CheckedException {

		BeanEntity<?> entityFound = createEntity(entity, jsonNode);

		methodInvoker.invokeMethodWithParameters(StringsUtils.getNameService(entity), Constants.METHOD_SAVE);

		return new ResponseEntity<>(entityFound, HttpStatus.OK);

	}

	/**
	 * @param entity
	 * @param jsonNode
	 * @return
	 * @throws CheckedException
	 */
	@PostMapping(value = "/flush/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BeanEntity<?>> saveAndFlush(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws CheckedException {

		BeanEntity<?> entityFound = createEntity(entity, jsonNode);

		methodInvoker.invokeMethodWithParameters(StringsUtils.getNameService(entity), Constants.METHOD_SAVE_AND_FLUSH);

		return new ResponseEntity<>(entityFound, HttpStatus.OK);

	}

	/**
	 * @param entity
	 * @param jsonNode
	 * @return
	 * @throws CheckedException
	 */
	@PostMapping(value = "/all/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<BeanEntity<?>>> saveAll(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws CheckedException {
		
		List<BeanEntity<?>> list = persisteAll(Constants.METHOD_SAVE_ALL, entity, jsonNode);

		return new ResponseEntity<>(list, HttpStatus.OK);

	}

	/**
	 * @param entity
	 * @param jsonNode
	 * @return
	 * @throws CheckedException
	 */
	@PostMapping(value = "/all/flush/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<BeanEntity<?>>> saveAllAndFlush(@PathVariable String entity, @RequestBody JsonNode jsonNode) throws CheckedException {

		List<BeanEntity<?>> list = persisteAll(Constants.METHOD_SAVE_ALL_AND_FLUSH, entity, jsonNode);

		return new ResponseEntity<>(list, HttpStatus.OK);

	}

	/**
	 * @param entity
	 * @param jsonNode
	 * @return
	 * @throws CheckedException
	 */
	private BeanEntity<?> createEntity(String entity, JsonNode jsonNode) throws CheckedException {

		BeanEntity<?> entityFound = (BeanEntity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		entityFound = (BeanEntity<?>) objectMapper.convertValue(jsonNode, entityFound.getClass());

		setEntity(entity, entityFound);

		return entityFound;
	}

	/**
	 * @param method
	 * @param entity
	 * @param jsonNode
	 * @return
	 * @throws CheckedException
	 */
	private List<BeanEntity<?>> persisteAll(String method, String entity, JsonNode jsonNode) throws CheckedException {

		BeanEntity<?> entityFound = null;

		List<BeanEntity<?>> list = new ArrayList<>();

		for (Iterator<JsonNode> iterator = jsonNode.elements(); iterator.hasNext();) {

			entityFound = (BeanEntity<?>) MethodReflection.findEntityUsingClassLoader(entity);
			
			entityFound = objectMapper.convertValue(iterator.next(), entityFound.getClass());

			list.add(entityFound);
		}

		methodInvoker.invokeMethodWithParameters(StringsUtils.getNameService(entity), method, list);

		return list;
	}
}