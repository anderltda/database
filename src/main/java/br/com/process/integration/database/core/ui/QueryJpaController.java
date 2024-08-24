package br.com.process.integration.database.core.ui;

import java.util.Arrays;
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

import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.util.Constants;

@RestController
@RequestMapping("/v1/database")
public class QueryJpaController extends AbstractController {
	
	 /*****************************************************************************************************************************************************************
	 * SERVICES UTILIZANDO CRITERIA																														        	  *	
	 *****************************************************************************************************************************************************************/	
	
	/**
	 * 	public Long count(Map<String, Object> filter)
	 */
	@GetMapping(value = "/count/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> count(@PathVariable String entity, @RequestParam(defaultValue = "") Map<String, Object> filter) throws CheckedException {

		setEntity(entity);

		int count = (int) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_COUNT, filter);

		final String body = gson.toJson(count);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	/**
	 * 	public E findBySingle(Map<String, Object> filter)
	 */
	@GetMapping(value = "/single/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findBySingle(@PathVariable String entity, @RequestParam(defaultValue = "") Map<String, Object> params) throws CheckedException {

		setEntity(entity);

		BeanEntity<?> entityFound = (BeanEntity<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_BY_SINGLE, params);

		if (entityFound != null) {

			final String body = gson.toJson(entityFound, entityFound.getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * 	List<E> findAll(Map<String, Object> filter, List<String> sortList, List<String> sortOrders)
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> filter,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = Constants.DESC) List<String> sortOrders) throws CheckedException {
		
		removeTrashFilter(filter);
		
		setEntity(entity);

		List<BeanEntity<?>> list = (List<BeanEntity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL, filter, sortList, sortOrders);

		if (!list.isEmpty()) {
	        
			final String body = removeEmptyLinks(gson.toJson(list));

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * 	public PagedModel<E> findAll(Map<String, Object> filter, Integer page, Integer size, List<String> sortList, List<String> sortOrders)
	 */
	@SuppressWarnings({ "rawtypes" })
	@GetMapping(value = "/paginator/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PagedModel> findAll(@PathVariable String entity,
			@RequestParam(defaultValue = "") Map<String, Object> filter, 
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = Constants.DESC) List<String> sortOrders) throws CheckedException {
		
		removeTrashFilter(filter);

		setEntity(entity);

		PagedModel pagedModel = (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL, filter, page, size, sortList, sortOrders);
		
		if (!pagedModel.getContent().isEmpty()) {
			return ResponseEntity.ok(pagedModel);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	 /*****************************************************************************************************************************************************************
	 * SERVICES UTILIZANDO JPQL																															        	  *	
	 *****************************************************************************************************************************************************************/

	/**
	 * public Long count(Map<String, Object> filter, String method)
	 */
	@GetMapping(value = "/jpql/count/{entity}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> count(
			@PathVariable String entity, 
			@RequestParam Map<String, Object> filter,
			@PathVariable String method) throws CheckedException {

		setEntity(entity);

		int count = (int) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_COUNT, filter, method);

		final String body = gson.toJson(count);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}	

	/**
	 * public E findBySingle(Map<String, Object> filter, String method)
	 */
	@GetMapping(value = "/jpql/single/{entity}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findBySingle(
			@PathVariable String entity, 
			@RequestParam Map<String, Object> params, 
			@PathVariable String method) throws CheckedException {

		setEntity(entity);

		BeanEntity<?> entityFound = (BeanEntity<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_BY_SINGLE, params, method);

		if (entityFound != null) {

			final String body = gson.toJson(entityFound, entityFound.getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}	
	
	/**
	 * 	public List<E> findAll(Map<String, Object> filter, String method
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/jpql/{entity}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(
			@PathVariable String entity, 
			@RequestParam Map<String, Object> filter, 
			@PathVariable String method) throws CheckedException {
		
		addAjustOrderByFilter(filter);

		setEntity(entity);

		List<BeanEntity<?>> list = (List<BeanEntity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL, filter, method);

		if (!list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * 	public PagedModel<E> findAll(Map<String, Object> filter, String method
	 */
	@SuppressWarnings({ "rawtypes" })
	@GetMapping(value = "/jpql/paginator/{entity}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PagedModel> paginator(
			@PathVariable String entity, 
			@RequestParam Map<String, Object> filter, 
			@PathVariable String method) throws CheckedException {

		addAjustPaginatorFilter(filter);
		addAjustOrderByFilter(filter);

		setEntity(entity);

		PagedModel pagedModel = (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_PAGINATOR, filter, method);
		
		if (!pagedModel.getContent().isEmpty()) {
			return ResponseEntity.ok(pagedModel);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	 /*****************************************************************************************************************************************************************
	 * SERVICES UTILIZANDO GENERIC CRUD REPOSITORY																													  *	
	 *****************************************************************************************************************************************************************/
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/{entity}/ids/{ids}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAllIds(@PathVariable String entity, @PathVariable String ids) throws CheckedException {
		
		List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();

		List<BeanEntity<?>> list = (List<BeanEntity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL_BY_ID, idList);

		if (!list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(value = "/{entity}/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findById(@PathVariable String entity, @PathVariable String id) throws CheckedException {

		setId(entity, id);

		BeanEntity<?> entityFound = (BeanEntity<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_BY_ID);

		if (entityFound != null) {

			final String body = gson.toJson(entityFound, entityFound.getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/exist/{entity}/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> existsById(@PathVariable String entity, @PathVariable String id) throws CheckedException {

		setId(entity, id);

		Boolean existsById = (Boolean) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_EXISTS_BY_ID);

		final String body = gson.toJson(existsById);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}
}
