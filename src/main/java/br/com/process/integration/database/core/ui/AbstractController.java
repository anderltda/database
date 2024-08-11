package br.com.process.integration.database.core.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.process.integration.database.core.domain.Entity;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.reflection.MethodInvoker;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.ui.adapter.LocalDateAdapter;
import br.com.process.integration.database.core.ui.adapter.LocalDateTimeAdapter;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.core.util.DynamicTypeConverter;

@Component
public abstract class AbstractController {

	@Autowired
	protected MethodInvoker methodInvoker;

	protected Gson gson = null;
	
	protected AbstractController() {
		gson = new GsonBuilder()
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
	}
	
	protected void removeTrashFilter(Map<String, Object> params) {
		params.remove("page");
		params.remove("size");
		params.remove("sortList");
		params.remove("sortOrders");
		params.remove("sort");
	}
	
	protected void addAjustFilter(Map<String, Object> filter) {
		
		Map<String, Object> filterRefactory = new HashMap<>();

		filter.forEach((key, value) -> {
			if (value.toString().equals(Constants.HTML_BETWEEN)) {
				String valueRefactory = filter.get(key.replace(Constants.IDENTITY_OPERATOR, "")).toString();
				String[] split = valueRefactory.replaceAll("[\\[\\]]", "").split(",");
				filterRefactory.put(key.replace(Constants.IDENTITY_OPERATOR, Constants.BETWEEN_START), split[0].trim());
				filterRefactory.put(key.replace(Constants.IDENTITY_OPERATOR, Constants.BETWEEN_END), split[1].trim());
			} else {
				filter.put(key, value.toString().contains(",") ? Arrays.asList(value.toString().split(",")) : value);
			}
		});

		filter.putAll(filterRefactory);
	}
	
	protected void setId(String nameEntity, String id) throws CheckedException {
		Entity<?> entity = (Entity<?>) MethodReflection.findEntityUsingClassLoader(nameEntity);
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), "setId", DynamicTypeConverter.convert(entity, id));
	}
	
	protected void setEntity(String nameEntity) throws CheckedException {
		Entity<?> entity = (Entity<?>) MethodReflection.findEntityUsingClassLoader(nameEntity);
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), "setEntity", entity);
	}
	
	protected void setEntity(String nameEntity, Entity<?> entity) throws CheckedException {
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), "setEntity", entity);
	}
	
	protected void setView(String nameView) throws CheckedException {
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameView), "setView", MethodReflection.findDtoUsingClassLoader(nameView));
	}
}
