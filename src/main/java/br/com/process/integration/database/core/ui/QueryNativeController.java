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

import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.util.Constants;

@RestController
@RequestMapping("/v1/api-rest-database")
public class QueryNativeController extends AbstractController {

	@GetMapping(value = "/execute/query/find/single/{instance}/{methodInvokerQuery}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findBySingle(@PathVariable String instance,
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodInvokerQuery) {

		addParam(params);

		setView(instance);

		Object object = methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(instance), Constants.METHOD_EXECUTE_QUERY_NATIVE_FIND_BY_SINGLE, params, methodInvokerQuery);

		if (object != null) {

			final String body = gson.toJson(object, MethodReflection.findDtoUsingClassLoader(instance).getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/execute/query/find/all/{instance}/{methodInvokerQuery}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String instance, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodInvokerQuery) {

		addParam(params);

		setView(instance);

		List<?> list = (List<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(instance), Constants.METHOD_EXECUTE_QUERY_NATIVE, params, methodInvokerQuery);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings({ "rawtypes" })
	@GetMapping(value = "/execute/query/page/{instance}/{methodInvokerQuery}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PagedModel executeQuery(@PathVariable String instance,
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodInvokerQuery,
			@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "30") Integer size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") String sortOrder) {

		addParam(params);

		setView(instance);

		return (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(instance), Constants.METHOD_EXECUTE_QUERY_NATIVE, params,
				methodInvokerQuery, page, size, sortList, sortOrder);
	}
}
