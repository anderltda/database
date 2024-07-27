package br.com.process.integration.database.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.reflection.MethodReflection;

@RestController
@RequestMapping("/v1/api-rest-database")
public class QueryJpaController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryJpaController.class);
	
	/**
	 * Retornar tudo dado nenhum parametro ou utilizando filtros com parametros da Entidade (Tabela)
	 * @method_reference findAll: Returns all entities matching the given {@link Example}. In case no match could be found an empty {@link Iterable}
	 * is returned.
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @param sortList - Lista com atributos para ordenar
	 * @param sortOrder - ASC: Ascendentes e DESC: decrescentes
	 * @return - Retorna uma lista de registros da Entidade (Tabela)
	 * @throws ServiceException
	 * @obs é uma lista (nao paginado)
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params,
			@RequestParam(defaultValue = "") ArrayList<String> sortList,
			@RequestParam(defaultValue = "DESC") String sortOrder) throws ServiceException {
		
		removeParams(params);
		
		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		List<Entity<?>> list = (List<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), "findAll", params, sortList, sortOrder);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			LOGGER.info("#### service total: {} findAll {}", list.size(), body);

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
	 * @param sortOrder - ASC: Ascendentes e DESC: decrescentes
	 * @return - Retorna uma lista de registros da Entidade (Tabela)
	 * @throws ServiceException
	 * @obs é paginado
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/page/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PagedModel<Entity<?>> findAll(@PathVariable String entity,
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(defaultValue = "") ArrayList<String> sortList,
			@RequestParam(defaultValue = "DESC") String sortOrder) throws ServiceException {
		
		removeParams(params);

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		PagedModel<Entity<?>> pagedModel = (PagedModel<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), "findAll", params, page, size, sortList, sortOrder);

		return pagedModel;
	}
	
	
	/**
	 * Retornar tudo dado um metodo de um repository(QUERY JQPL) e filtros com parametros da Entidade (Tabela) respeitando as condicoes que estao na propria query
	 * @method_reference findAll: Returns all entities matching the given {@link Example}. In case no match could be found an empty {@link Iterable}
	 * is returned.
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @param methodQueryJPQL - Nome do metodo que está no repository<EntityRepository>
	 * @return - Retorna uma lista de registros da Entidade (Tabela)
	 * @throws ServiceException
	 * @obs é uma lista(nao paginado)
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/jpql/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodQueryJPQL) throws ServiceException {

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		List<Entity<?>> list = (List<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), "findAll", params, methodQueryJPQL);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			LOGGER.info("#### service findAll {}", body);

			return new ResponseEntity<String>(body, HttpStatus.OK);
		}

		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
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
	 * @param sortOrder - ASC: Ascendentes e DESC: decrescentes
	 * @return - Retorna uma lista de registros da Entidade (Tabela)
	 * @throws ServiceException
	 * @obs é paginado
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/jpql/page/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PagedModel<Entity<?>> findAll(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodQueryJPQL,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "30") Integer size,
			@RequestParam(defaultValue = "") ArrayList<String> sortList,
			@RequestParam(defaultValue = "DESC") String sortOrder) throws ServiceException {

		removeParams(params);

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		PagedModel<Entity<?>> pagedModel = (PagedModel<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entityFound.getClass().getSimpleName()), "findAll", params, methodQueryJPQL, page, size, sortList, sortOrder.toString());

		return pagedModel;
	}

	/**
	 * Retorna 1 unico objeto de um repository(QUERY JQPL) e filtros com parametros da Entidade (Tabela) respeitando as condicoes que estao na propria query
	 * @method_reference findBy: Retrieves an entity by its filters.
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @param methodQueryJPQL - Nome do metodo que está no repository<EntityRepository>
	 * @return - Retorna um unico registro da Entidade (Tabela)
	 * @throws ServiceException
	 * @obs caso a query retorne mais de um registro ocorrerá uma exception do tipo ServiceException
	 */
	@GetMapping(value = "/find/single/jpql/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findBySingle(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodQueryJPQL) throws ServiceException {

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		entityFound = (Entity<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), "findBySingle", params, methodQueryJPQL);

		if (entityFound != null && entityFound.getId() != null) {

			final String body = gson.toJson(entityFound, entityFound.getClass());

			LOGGER.info("#### findBySingle {}", body);

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
	 * @throws ServiceException
	 * @obs retorna apenas a quantidade de registros da Entidade (Tabela)
	 */
	@GetMapping(value = "/count/jpql/{entity}/{methodQueryJPQL}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> count(@PathVariable String entity, 
			@RequestParam(defaultValue = "") Map<String, Object> params,
			@PathVariable String methodQueryJPQL) throws ServiceException {

		Entity<?> entity_ = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entity_);

		Long count = (Long) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), "count", params, methodQueryJPQL);

		final String body = gson.toJson(count);

		LOGGER.info("#### service count {} total de registro(s) na tabela {} ", count, entity);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}
	
	/**
	 * Retornar tudo dado seus IDs
	 * @method_reference findAllById: Returns all instances of the type {@code T} with the given IDs.
	 * @param entity - Entidade (Tabela)
	 * @param ids - Lista dos IDs
	 * @return - Retorna uma lista registros da Entidade (Tabela)
	 * @throws ServiceException
	 * @obs é uma lista(nao paginado)
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/find/all/ids/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAllIds(@PathVariable String entity, @RequestParam ArrayList<String> id) throws ServiceException {

		List<Entity<?>> list = (List<Entity<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), "findAllById", id);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			LOGGER.info("#### service findAllById {}", body);

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
	 * @throws ServiceException
	 * @obs retorna apenas um objeto da Entidade (Tabela)
	 */
	@GetMapping(value = "/find/{entity}/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findById(@PathVariable String entity, @PathVariable String id) throws ServiceException {

		setId(entity, id);

		Entity<?> entityFound = (Entity<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), "findById");

		if (entityFound != null && entityFound.getId() != null) {

			final String body = gson.toJson(entityFound, entityFound.getClass());

			LOGGER.info("#### findById {}", body);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Retorna 1 unico objeto do tipo Long com a contagem dos registros encontrados com os filtros como parametros da Entidade (Tabela).
	 * @param entity - Entidade (Tabela)
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 * @return - Retorna a quantidade de registros da Entidade (Tabela)
	 * @throws ServiceException
	 * @obs retorna apenas a quantidade de registros da Entidade (Tabela)
	 */
	@GetMapping(value = "/count/{entity}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> count(@PathVariable String entity, @RequestParam(defaultValue = "") Map<String, Object> params) throws ServiceException {

		Entity<?> entityFound = (Entity<?>) MethodReflection.findEntityUsingClassLoader(entity);

		setEntity(entity, entityFound);

		Long count = (Long) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), "count", params);

		final String body = gson.toJson(count);

		LOGGER.info("#### service count {} total de registro(s) na tabela {} ", count, entity);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}

	/**
	 * Retorna 1 unico objeto do tipo Boolena informando se existe o ID passado como parametro na Entidade (Tabela).
	 * @param entity - Entidade (Tabela)
	 * @param id - PrimaryKey da Tabela
	 * @return - Retorna um boolean TRUE se existir o ID o registro na Entidade (Tabela) FALSE se nao existir
	 * @throws ServiceException
	 * @obs retorna apenas um TRUE|FALSE
	 */
	@GetMapping(value = "/exist/id/{entity}/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> existsById(@PathVariable String entity, @PathVariable String id) throws ServiceException {

		setId(entity, id);

		Boolean existsById = (Boolean) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity), "existsById");

		final String body = gson.toJson(existsById);

		LOGGER.info("#### ID {} service existsById {} na tabela {}", id, existsById, entity);

		return new ResponseEntity<>(body, HttpStatus.OK);

	}
}
