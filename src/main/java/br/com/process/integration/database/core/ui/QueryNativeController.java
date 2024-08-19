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

import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.util.Constants;

@RestController
@RequestMapping("/v1/database")
public class QueryNativeController extends AbstractController {
	
	@GetMapping(value = "/execute/query/count/{instance}/{query}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> count(@PathVariable String instance, @RequestParam Map<String, Object> filter, @PathVariable String query) throws CheckedException {

		addAjustFilter(filter);

		setView(instance);

		Integer count = (Integer) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(instance), Constants.METHOD_EXECUTE_QUERY_NATIVE_COUNT, filter, query);

		final String body = gson.toJson(count);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	@GetMapping(value = "/execute/query/find/single/{instance}/{query}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findSingle(@PathVariable String instance, @RequestParam Map<String, Object> filter, @PathVariable String query) throws CheckedException {

		addAjustFilter(filter);

		setView(instance);

		Object object = methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(instance), Constants.METHOD_EXECUTE_QUERY_NATIVE_SINGLE, filter, query);

		if (object != null) {

			final String body = gson.toJson(object, MethodReflection.findDtoUsingClassLoader(instance).getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/execute/query/all/{instance}/{query}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String instance, @RequestParam Map<String, Object> filter, @PathVariable String query) throws CheckedException {

		addAjustFilter(filter);

		setView(instance);

		List<?> list = (List<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(instance), Constants.METHOD_EXECUTE_QUERY_NATIVE, filter, query);

		if (!list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings({ "rawtypes" })
	@GetMapping(value = "/execute/query/page/{instance}/{query}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PagedModel executeQuery(@PathVariable String instance, @RequestParam(defaultValue = "") Map<String, Object> filter, @PathVariable String query) throws CheckedException {

		addAjustFilter(filter);

		setView(instance);

		return (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(instance), Constants.METHOD_EXECUTE_QUERY_NATIVE_PAGINATOR, filter, query);
	}
}
