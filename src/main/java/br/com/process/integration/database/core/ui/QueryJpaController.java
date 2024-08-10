package br.com.process.integration.database.core.ui;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.process.integration.database.core.domain.Entity;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.util.Constants;

@RestController
@RequestMapping("/v1/api-rest-database")
public class QueryJpaController extends AbstractController {
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO CRITERIA																														        	  *	
	 * 																																								  *
	 * INICIAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/	

	
	/**
	 * 	public Long count(Map<String, Object> filter)
	 */
	@GetMapping(value = "/count/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> count(@PathVariable String entity, @RequestParam(defaultValue = "") Map<String, Object> params) {

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		Long count = (Long) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_COUNT, params);

		final String body = gson.toJson(count);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}
	
	/**
	 * 	public E findBySingle(Map<String, Object> filter)
	 */
	@GetMapping(value = "/find/single/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findBySingle(@PathVariable String entity, @RequestParam(defaultValue = "") Map<String, Object> params) {

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		entityFound = (Entity<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_BY_SINGLE, params);

		if (entityFound != null && entityFound.getId() != null) {

			final String body = gson.toJson(entityFound, entityFound.getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * 	List<E> findAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders)
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") List<String> sortOrders) {
		
		removeParams(params);
		
		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		List<Entity<?>> list = (List<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL, params, sortList, sortOrders);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * 	public PagedModel<E> findAll(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, List<String> sortOrders)
	 */
	@SuppressWarnings({ "rawtypes" })
	@GetMapping(value = "/find/all/page/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PagedModel findAll(@PathVariable String entity,
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") List<String> sortOrders) {
		
		removeParams(params);

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		return (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL, params, page, size, sortList, sortOrders);
	}
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO CRITERIA																														        	  *	
	 * 																																								  *
	 * FINAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/	
	
	
	
	
	
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO JPQL																															        	  *	
	 * 																																								  *
	 * INICIAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/

	/**
	 * public Long count(Map<String, Object> filter, String methodQueryJPQL)
	 */
	@GetMapping(value = "/count/jpql/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> count(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params,
			@PathVariable String methodQueryJPQL) {

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		Long count = (Long) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_COUNT, params, methodQueryJPQL);

		final String body = gson.toJson(count);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}	

	/**
	 * public E findBySingle(Map<String, Object> filter, String methodQueryJPQL)
	 */
	@GetMapping(value = "/find/single/jpql/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findBySingle(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodQueryJPQL) {

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		entityFound = (Entity<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_BY_SINGLE, params, methodQueryJPQL);

		if (entityFound != null && entityFound.getId() != null) {

			final String body = gson.toJson(entityFound, entityFound.getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}	
	
	/**
	 * 	public List<E> findAll(Map<String, Object> filter, String methodQueryJPQL, List<String> sortList, List<String> sortOrders)
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/jpql/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodQueryJPQL,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") List<String> sortOrders) {
		
		removeParams(params);

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		List<Entity<?>> list = (List<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL, params, methodQueryJPQL, sortList, sortOrders);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * 	public PagedModel<E> findAll(Map<String, Object> filter, String methodQueryJPQL, Integer page, Integer size, List<String> sortList, List<String> sortOrders)
	 */
	@SuppressWarnings({ "rawtypes" })
	@GetMapping(value = "/find/all/jpql/page/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PagedModel findAll(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodQueryJPQL,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "30") Integer size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") List<String> sortOrders) {

		removeParams(params);

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		return (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entityFound.getClass().getSimpleName()), Constants.METHOD_FIND_ALL, 
				params, methodQueryJPQL, page, size, sortList, sortOrders);
	}
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO JPQL																															        	  *	
	 * 																																								  *
	 * FINAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/
	
	
	
	
	
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO GENERIC CRUD REPOSITORY																													  *	
	 * 																																								  *
	 * INICIAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/ids/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAllIds(@PathVariable String entity, @RequestParam List<String> ids) {

		List<Entity<?>> list = (List<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL_BY_ID, ids);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(value = "/find/{entity}/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findById(@PathVariable String entity, @PathVariable String id) {

		setId(entity, id);

		Entity<?> entityFound = (Entity<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_BY_ID);

		if (entityFound != null && entityFound.getId() != null) {

			final String body = gson.toJson(entityFound, entityFound.getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/exist/{entity}/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> existsById(@PathVariable String entity, @PathVariable String id) {

		setId(entity, id);

		Boolean existsById = (Boolean) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_EXISTS_BY_ID);

		final String body = gson.toJson(existsById);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}
	
	 /*****************************************************************************************************************************************************************
	 *																																								  *
	 * SERVICES UTILIZANDO GENERIC CRUD REPOSITORY																													  *	
	 * 																																								  *
	 * FINAL																																						  *
	 *  																																							  *
	 *****************************************************************************************************************************************************************/
}
