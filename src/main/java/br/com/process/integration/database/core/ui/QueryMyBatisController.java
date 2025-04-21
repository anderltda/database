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

import br.com.process.integration.database.core.domain.BeanData;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.util.Constants;

@RestController
@RequestMapping("/v1/database")
public class QueryMyBatisController extends AbstractController {
	
	@GetMapping(value = "/mapper/count/{data}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Integer> count(@PathVariable String data, @PathVariable String method, @RequestParam(defaultValue = "") Map<String, Object> filter) throws CheckedException {

		setData(data);

		Integer count = (Integer) methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(data), Constants.METHOD_EXECUTE_MAPPER_COUNT, filter, method);

		return new ResponseEntity<>(count, HttpStatus.OK);
	}	

	@GetMapping(value = "/mapper/single/{data}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BeanData<?>> findBySingle(@PathVariable String data, @PathVariable String method, @RequestParam(defaultValue = "") Map<String, Object> filter) throws CheckedException {

		setData(data);

		BeanData<?> beanData = (BeanData<?>) methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(data), Constants.METHOD_EXECUTE_MAPPER_SINGLE, filter, method);

		if (beanData != null) {
			return new ResponseEntity<>(beanData, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}	
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/mapper/{data}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<BeanData<?>>> findAll(@PathVariable String data, @PathVariable String method, @RequestParam(defaultValue = "") Map<String, Object> filter) throws CheckedException {

		addAjustOrderByFilter(filter);
		
		setData(data);
		
		List<BeanData<?>> list = (List<BeanData<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(data), Constants.METHOD_EXECUTE_MAPPER_ALL, filter, method);

		if (!list.isEmpty()) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping(value = "/mapper/paginator/{data}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PagedModel> findAllPaginator(@PathVariable String data, @PathVariable String method, @RequestParam Map<String, Object> filter) throws CheckedException {
		
		addAjustPaginatorFilter(filter);
		addAjustOrderByFilter(filter);
		
		setData(data);
		
		PagedModel pagedModel = (PagedModel) methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(data), Constants.METHOD_EXECUTE_PAGINATOR, filter, method);
		
		if (!pagedModel.getContent().isEmpty()) {
			return ResponseEntity.ok(pagedModel);
		}
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
