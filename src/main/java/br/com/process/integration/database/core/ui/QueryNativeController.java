package br.com.process.integration.database.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.process.integration.database.core.domain.Model;
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.reflection.MethodReflection;

@RestController
@RequestMapping("/v1/api-rest-database")
public class QueryNativeController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryNativeController.class);

	@GetMapping(value = "/execute/query/find/single/{instance}/{methodInvokerQuery}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findBySingle(@PathVariable String instance, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodInvokerQuery) throws ServiceException {

		setModel(instance);

		Model<?> dto = (Model<?>) methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(instance), "executeQueryNativeFindBySingle", params, methodInvokerQuery);

		if (dto != null) {

			final String body = gson.toJson(dto, dto.getClass());

			LOGGER.info("#### findBySingle {}", body);

			return new ResponseEntity<String>(body, HttpStatus.OK);
		}

		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/execute/query/find/all/{instance}/{methodInvokerQuery}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String instance, 
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodInvokerQuery) throws ServiceException {

		setModel(instance);

		List<Model<?>> list = (List<Model<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(instance), "executeQueryNative", params, methodInvokerQuery);

		if (list != null && !list.isEmpty()) {

			final String body = gson.toJson(list);

			LOGGER.info("#### service findAll {}", body);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = "/execute/query/page/{instance}/{methodInvokerQuery}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public PagedModel<Model<?>> executeQuery(@PathVariable String instance,
			@RequestParam(defaultValue = "") Map<String, Object> params, 
			@PathVariable String methodInvokerQuery,
			@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "30") Integer size,
			@RequestParam(defaultValue = "") ArrayList<String> sortList,
			@RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) throws ServiceException {
		
		addParam(params);		

		setModel(instance);

		Model<?> entity = (Model<?>) MethodReflection.findDtoUsingClassLoader(instance);

		PagedModel<Model<?>> pagedModel = (PagedModel<Model<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(entity.getClass().getSimpleName()), "executeQueryNative", params, methodInvokerQuery, page, size, sortList, sortOrder.toString());

		return pagedModel;
	}
}
