package br.com.process.integration.database.core.ui;

import java.util.List;
import java.util.Map;

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
	public ResponseEntity<String> count(@PathVariable String data, 
			@RequestParam(defaultValue = "") Map<String, Object> filter,
			@PathVariable String method) throws CheckedException {

		setData(data);

		int count = (int) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(data), Constants.METHOD_EXECUTE__MAPPER_COUNT, filter, method);

		final String body = gson.toJson(count);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}	

	@GetMapping(value = "/mapper/single/{data}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findBySingle(@PathVariable String data, 
			@RequestParam(defaultValue = "") Map<String, Object> filter, 
			@PathVariable String method) throws CheckedException {
		
		setData(data);

		BeanData<?> beanData = (BeanData<?>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(data), Constants.METHOD_EXECUTE_MAPPER_SINGLE, filter, method);

		if (beanData != null) {

			final String body = gson.toJson(beanData, beanData.getClass());

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}	
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/mapper/{data}/{method}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> findAll(@PathVariable String data, 
			@RequestParam(defaultValue = "") Map<String, Object> filter, 
			@PathVariable String method) throws CheckedException {
		
		setData(data);
		
		List<BeanData<?>> list = (List<BeanData<?>>) methodInvoker.invokeMethodReturnObjectWithParameters(
				MethodReflection.getNameService(data), Constants.METHOD_EXECUTE_MAPPER_ALL, filter, method);

		if (!list.isEmpty()) {

			final String body = gson.toJson(list);

			return new ResponseEntity<>(body, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
