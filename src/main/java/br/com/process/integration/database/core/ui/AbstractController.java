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

	/**
	 * Remove parametros que nao fazem parte da entidade
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 */
	protected void removeParams(Map<String, Object> params) {
		params.remove("page");
		params.remove("size");
		params.remove("sortList");
		params.remove("sortOrder");
		params.remove("sortOrders");
		params.remove("sort");
	}
	
	/**
	 * Remove parametros que nao fazem parte da entidade e ajusta
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 */
	protected void addParam(Map<String, Object> params) {

		Map<String, Object> paramsRefactory = new HashMap<>();

		params.forEach((key, value) -> {
			if (value.toString().equals(Constants.HTML_BETWEEN)) {
				String valueRefactory = params.get(key.replace(Constants.IDENTITY_OPERATOR, "")).toString();
				String[] split = valueRefactory.replaceAll("[\\[\\]]", "").split(",");
				paramsRefactory.put(key.replace(Constants.IDENTITY_OPERATOR, Constants.BETWEEN_START),
						split[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "").trim());
				paramsRefactory.put(key.replace(Constants.IDENTITY_OPERATOR, Constants.BETWEEN_END),
						split[1].replaceAll("^\\s+", "").replaceAll("\\s+$", "").trim());
			} else {
				params.put(key, value.toString().contains(",") ? Arrays.asList(value.toString().split(",")) : value);
			}
		});

		params.putAll(paramsRefactory);
	}
	
	/**
	 * Informar (Setando) a Entidade (Tabela) com apenas o ID, que a classe <EntityService> irá trabalhar passando o nome da entidade e ID
	 * @param nameEntity - Nome da Entidade (Tabela)
	 * @param id - ID da Entidade (Tabela)
	 */
	protected void setId(String nameEntity, String id) {
		Entity<?> entity = (Entity<?>) MethodReflection.findEntityUsingClassLoader(nameEntity);
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), "setId", DynamicTypeConverter.convert(entity, id));
	}
	
	/**
	 * Informar (Setando) a Entidade (Tabela) que a classe <EntityService> irá trabalhar passando o nome da entidade
	 * @param nameEntity - Nome da Entidade (Tabela)
	 */
	protected void setEntity(String nameEntity) {
		Entity<?> entity = (Entity<?>) MethodReflection.findEntityUsingClassLoader(nameEntity);
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), "setEntity", entity);
	}
	
	/**
	 * Informar (Setando) a Entidade (Tabela) que a classe <EntityService> irá trabalhar passando o nome da entidade e a instancia da entity
	 * @param nameEntity - Nome da Entidade (Tabela)
	 * @param entity - Object Entity - Entidade (Tabela)
	 */
	protected void setEntity(String nameEntity, Entity<?> entity) {
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), "setEntity", entity);
	}
	
	/**
	 * Informar (Setando) o Model (QUERY NATIVE) que a classe <ViewService> irá trabalhar passando o nome do Model
	 * @param nameView - Nome do Model (QUERY NATIVE)
	 */
	protected void setView(String nameView) {
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameView), "setView", MethodReflection.findDtoUsingClassLoader(nameView));
	}
}
