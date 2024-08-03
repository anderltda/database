package br.com.process.integration.database.core.ui;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Example;
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

	/**
	 * Retornar tudo dado nenhum parametro ou utilizando filtros com parametros da Entidade (Tabela)
	 * @method_reference findAll: Returns all entities matching the given {@link Example}. In case no match could be found an empty {@link Iterable}
	 * is returned.
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @param sortList - Lista com atributos para ordenar
	 * @param sortOrders - ASC: Ascendentes e DESC: decrescentes
	 * @return - Retorna uma lista de registros da Entidade (Tabela)
	 * @obs é uma lista (nao paginado)
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
	 * Retornar tudo dado nenhum parametro ou utilizando filtros com parametros da Entidade (Tabela)
	 * @method_reference findAll: Returns all entities matching the given {@link Example}. In case no match could be found an empty {@link Iterable}
	 * is returned.
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @param page - Valor inicial da pagina
	 * @param size - Quantidade de registros por pagina
	 * @param sortList - Lista com atributos para ordenar
	 * @param sortOrders - ASC: Ascendentes e DESC: decrescentes
	 * @return - Retorna uma lista de registros da Entidade (Tabela)
	 * @obs é paginado
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
	
	
	/**
	 * Retornar tudo dado um metodo de um repository(QUERY JQPL) e filtros com parametros da Entidade (Tabela) respeitando as condicoes que estao na propria query
	 * @method_reference findAll: Returns all entities matching the given {@link Example}. In case no match could be found an empty {@link Iterable}
	 * is returned.
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @param methodQueryJPQL - Nome do metodo que está no repository<EntityRepository>
	 * @return - Retorna uma lista de registros da Entidade (Tabela)
	 * @obs é uma lista(nao paginado)
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/jpql/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodQueryJPQL) {

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		List<Entity<?>> list = (List<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL, params, methodQueryJPQL);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	/**
	 * Retornar tudo dado um metodo de um repository(QUERY JQPL) e filtros com parametros da Entidade (Tabela) respeitando as condicoes que estao na propria query
	 * @method_reference findAll: Returns all entities matching the given {@link Example}. In case no match could be found an empty {@link Iterable}
	 * is returned.
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @param methodQueryJPQL - Nome do metodo que está no repository<EntityRepository>
	 * @param page - Valor inicial da pagina
	 * @param size - Quantidade de registros por pagina
	 * @param sortList - Lista com atributos para ordenar
	 * @param sortOrders - ASC: Ascendentes e DESC: decrescentes
	 * @return - Retorna uma lista de registros da Entidade (Tabela)
	 * @obs é paginado
	 */
	@SuppressWarnings({ "rawtypes" })
	@GetMapping(value = "/find/all/jpql/page/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PagedModel findAll(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodQueryJPQL,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "30") Integer size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") String sortOrders) {

		removeParams(params);

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		return (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entityFound.getClass().getSimpleName()), Constants.METHOD_FIND_ALL, 
				params, methodQueryJPQL, page, size, sortList, sortOrders);
	}

	/**
	 * Retorna 1 unico objeto de um repository(QUERY JQPL) e filtros com parametros da Entidade (Tabela) respeitando as condicoes que estao na propria query
	 * @method_reference findBy: Retrieves an entity by its filters.
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @param methodQueryJPQL - Nome do metodo que está no repository<EntityRepository>
	 * @return - Retorna um unico registro da Entidade (Tabela)
	 * @obs caso a query retorne mais de um registro ocorrerá uma exception do tipo Exception
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
	 * Retorna 1 unico objeto do tipo Long com a contagem dos registros encontrados com os filtros como parametros da Entidade (Tabela) de um repository(QUERY JQPL).
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @param methodQueryJPQL - Nome do metodo que está no repository<EntityRepository>
	 * @return - Retorna a quantidade de registros da Entidade (Tabela)
	 * @obs retorna apenas a quantidade de registros da Entidade (Tabela)
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
	 * Retornar tudo dado seus IDs
	 * @method_reference findAllById: Returns all instances of the type {@code T} with the given IDs.
	 * @param entity - Entidade (Tabela)
	 * @param ids - Lista dos IDs
	 * @return - Retorna uma lista registros da Entidade (Tabela)
	 * @obs é uma lista(nao paginado)
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/ids/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAllIds(@PathVariable String entity, @RequestParam List<String> id) {

		List<Entity<?>> list = (List<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_FIND_ALL_BY_ID, id);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Retorna 1 unico objeto com Entidade (Tabela) por ID.
	 * @method_reference findById: Retrieves an entity by its id.
	 * @param entity - Entidade (Tabela)
	 * @param id - PrimaryKey da Tabela
	 * @return - Retorna um unico registro da Entidade (Tabela)
	 * @obs retorna apenas um objeto da Entidade (Tabela)
	 */
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

	/**
	 * Retorna 1 unico objeto do tipo Long com a contagem dos registros encontrados com os filtros como parametros da Entidade (Tabela).
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @return - Retorna a quantidade de registros da Entidade (Tabela)
	 * @obs retorna apenas a quantidade de registros da Entidade (Tabela)
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
	 * Retorna 1 unico objeto do tipo Boolena informando se existe o ID passado como parametro na Entidade (Tabela).
	 * @param entity - Entidade (Tabela)
	 * @param id - PrimaryKey da Tabela
	 * @return - Retorna um boolean TRUE se existir o ID o registro na Entidade (Tabela) FALSE se nao existir
	 * @obs retorna apenas um TRUE|FALSE
	 */
	@GetMapping(value = "/exist/id/{entity}/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> existsById(@PathVariable String entity, @PathVariable String id) {

		setId(entity, id);

		Boolean existsById = (Boolean) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), Constants.METHOD_EXISTS_BY_ID);

		final String body = gson.toJson(existsById);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}
}
