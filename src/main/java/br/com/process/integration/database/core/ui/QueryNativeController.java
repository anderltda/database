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
	public ResponseEntity<String> count(@PathVariable String view, @RequestParam Map<String, Object> filter, @PathVariable String queryName) throws CheckedException {

		addAjustFilter(filter);

		setView(view);

		int count = (int) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(view), Constants.METHOD_EXECUTE_QUERY_NATIVE_COUNT, filter, queryName);

		final String body = gson.toJson(count);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	@GetMapping(value = "/query/single/{view}/{queryName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findSingle(@PathVariable String view, @RequestParam Map<String, Object> filter, @PathVariable String queryName) throws CheckedException {

		addAjustFilter(filter);

		setView(view);

		BeanView<?> beanView = (BeanView<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(view), Constants.METHOD_EXECUTE_QUERY_NATIVE_SINGLE, filter, queryName);

		if (beanView != null) {

			final String body = gson.toJson(beanView, beanView.getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/query/{view}/{queryName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String view, @RequestParam Map<String, Object> filter, @PathVariable String queryName) throws CheckedException {

		addAjustFilter(filter);

		setView(view);

		List<BeanView<?>> list = (List<BeanView<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(view), Constants.METHOD_EXECUTE_QUERY_NATIVE, filter, queryName);

		if (!list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings({ "rawtypes" })
	@GetMapping(value = "/query/paginator/{view}/{queryName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PagedModel executeQuery(@PathVariable String view, @RequestParam(defaultValue = "") Map<String, Object> filter, @PathVariable String queryName) throws CheckedException {

		addAjustFilter(filter);

		setView(view);

		return (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(view), Constants.METHOD_EXECUTE_QUERY_NATIVE_PAGINATOR, filter, queryName);
	}
}
