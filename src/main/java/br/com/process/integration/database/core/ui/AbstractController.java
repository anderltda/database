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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.process.integration.database.core.domain.BeanEntity;
import br.com.process.integration.database.core.exception.CheckedException;
import br.com.process.integration.database.core.reflection.MethodInvoker;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.ui.adapter.EmptyLinksExclusionStrategy;
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
				.setExclusionStrategies(new EmptyLinksExclusionStrategy())
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
	}
	
	protected String removeEmptyLinks(String json) {
		JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
		for (JsonElement element : jsonArray) {
			JsonObject jsonObject = element.getAsJsonObject();
			if (jsonObject.has(Constants.HATEOAS_LINKS) && jsonObject.get(Constants.HATEOAS_LINKS).getAsJsonArray().size() == 0) {
				jsonObject.remove(Constants.HATEOAS_LINKS);
			}
		}
		return jsonArray.toString();
	}
	
	protected void removeTrashFilter(Map<String, Object> params) {
		params.remove(Constants.NUMBER_PAGE);
		params.remove(Constants.NUMBER_SIZE);
		params.remove(Constants.SORT_LIST);
		params.remove(Constants.SORT_ORDERS);
		params.remove(Constants.SORT);
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
		BeanEntity<?> entity = (BeanEntity<?>) MethodReflection.findEntityUsingClassLoader(nameEntity);
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), Constants.METHOD_SET_ID, DynamicTypeConverter.convert(entity, id));
	}
	
	protected void setEntity(String nameEntity, BeanEntity<?> entity) throws CheckedException {
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), Constants.METHOD_SET_ENTITY, entity);
	}
	
	protected void setEntity(String nameEntity) throws CheckedException {
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), Constants.METHOD_SET_ENTITY, MethodReflection.findEntityUsingClassLoader(nameEntity));
	}
	
	protected void setView(String nameView) throws CheckedException {
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameView), Constants.METHOD_SET_VIEW, MethodReflection.findViewUsingClassLoader(nameView));
	}
	
	protected void setData(String nameData) throws CheckedException {
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameData), Constants.METHOD_SET_DATA, MethodReflection.findDataUsingClassLoader(nameData));
	}
}
