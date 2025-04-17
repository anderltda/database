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

import br.com.process.integration.database.core.domain.BeanView;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.util.Constants;

@RestController
@RequestMapping("/v1/database")
public class QueryNativeController extends AbstractController {
	
	@GetMapping(value = "/query/count/{view}/{queryName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Integer> count(
			@PathVariable String view, 
			@RequestParam Map<String, Object> filter, 
			@PathVariable String queryName) throws CheckedException {

		addAjustFilter(filter);

		setView(view);

		Integer count = (int) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(view), Constants.METHOD_EXECUTE_QUERY_NATIVE_COUNT, filter, queryName);

		return new ResponseEntity<>(count, HttpStatus.OK);
	}

	@GetMapping(value = "/query/single/{view}/{queryName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> findSingle(
			@PathVariable String view, 
			@RequestParam Map<String, Object> filter, 
			@PathVariable String queryName) throws CheckedException {

		addAjustFilter(filter);
		addAjustOrderByFilter(filter);

		setView(view);

		BeanView<?> beanView = (BeanView<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(view), Constants.METHOD_EXECUTE_QUERY_NATIVE_SINGLE, filter, queryName);

		if (beanView != null) {

			return new ResponseEntity<>(beanView, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/query/{view}/{queryName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<BeanView<?>>> findAll(
			@PathVariable String view, 
			@RequestParam Map<String, Object> filter, 
			@PathVariable String queryName) throws CheckedException {

		addAjustFilter(filter);
		addAjustPaginatorFilter(filter);
		addAjustOrderByFilter(filter);

		setView(view);

		List<BeanView<?>> list = (List<BeanView<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(view), Constants.METHOD_EXECUTE_QUERY_NATIVE, filter, queryName);

		if (!list.isEmpty()) {

			return new ResponseEntity<>(list, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings({ "rawtypes" })
	@GetMapping(value = "/query/paginator/{view}/{queryName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PagedModel> executeQuery(
			@PathVariable String view, 
			@RequestParam Map<String, Object> filter, 
			@PathVariable String queryName) throws CheckedException {

		addAjustFilter(filter);

		setView(view);

		PagedModel pagedModel = (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(view), Constants.METHOD_EXECUTE_QUERY_NATIVE_PAGINATOR, filter, queryName);
		
		if (!pagedModel.getContent().isEmpty()) {
			return ResponseEntity.ok(pagedModel);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
