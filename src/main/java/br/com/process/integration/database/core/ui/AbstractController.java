package br.com.process.integration.database.core.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.process.integration.database.core.domain.Entity;
import br.com.process.integration.database.core.domain.Model;
import br.com.process.integration.database.core.exception.ServiceException;
import br.com.process.integration.database.core.reflection.MethodInvoker;
import br.com.process.integration.database.core.reflection.MethodReflection;
import br.com.process.integration.database.core.ui.adapter.LocalDateAdapter;
import br.com.process.integration.database.core.ui.adapter.LocalDateTimeAdapter;
import br.com.process.integration.database.core.util.Constants;
import br.com.process.integration.database.core.util.DynamicTypeConverter;
import br.com.process.integration.database.domain.entity.ProductCategory;
import br.com.process.integration.database.domain.entity.ProductOption;

@Component
public abstract class AbstractController {

	@Autowired
	protected MethodInvoker methodInvoker;

	protected Gson gson = null;
	
	public AbstractController() {
		gson = new GsonBuilder().addSerializationExclusionStrategy(strategy)
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
		params.remove("sort");
	}
	
	/**
	 * Remove parametros que nao fazem parte da entidade e ajusta
	 * @param params - Map<String, Object> de parametros com key é o atributo da entidade e value é o valor do atributo
	 */
	protected void addParam(Map<String, Object> params) {

		Map<String, Object> params_ = new HashMap<String, Object>();

		params.forEach((key, value) -> {
			if (value.toString().equals(Constants.HTML_BETWEEN)) {
				String value_ = params.get(key.replace(Constants.IDENTITY_OPERATOR, "")).toString();
				String[] split = value_.replaceAll("[\\[\\]]", "").split(",");
				params_.put(key.replace(Constants.IDENTITY_OPERATOR, Constants.BETWEEN_START),
						split[0].replaceAll("^\\s+|\\s+$", ""));
				params_.put(key.replace(Constants.IDENTITY_OPERATOR, Constants.BETWEEN_END),
						split[1].replaceAll("^\\s+|\\s+$", ""));
			} else {
				params.put(key, value.toString().contains(",") ? Arrays.asList(value.toString().split(",")) : value);
			}
		});

		params.putAll(params_);
	}
	
	/**
	 * Informar (Setando) a Entidade (Tabela) com apenas o ID, que a classe <EntityManager> irá trabalhar passando o nome da entidade e ID
	 * @param nameEntity - Nome da Entidade (Tabela)
	 * @param id - ID da Entidade (Tabela)
	 * @throws ServiceException
	 */
	protected void setId(String nameEntity, String id) throws ServiceException {
		Entity<?> entity = (Entity<?>) MethodReflection.findEntityUsingClassLoader(nameEntity);
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), "setId", DynamicTypeConverter.convert(entity, id));
	}
	
	/**
	 * Informar (Setando) a Entidade (Tabela) que a classe <EntityManager> irá trabalhar passando o nome da entidade
	 * @param nameEntity - Nome da Entidade (Tabela)
	 * @throws ServiceException
	 */
	protected void setEntity(String nameEntity) throws ServiceException {
		Entity<?> entity = (Entity<?>) MethodReflection.findEntityUsingClassLoader(nameEntity);
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), "setEntity", entity);
	}
	
	/**
	 * Informar (Setando) a Entidade (Tabela) que a classe <EntityManager> irá trabalhar passando o nome da entidade e a instancia da entity
	 * @param nameEntity - Nome da Entidade (Tabela)
	 * @param entity - Object Entity - Entidade (Tabela)
	 * @throws ServiceException
	 */
	protected void setEntity(String nameEntity, Entity<?> entity) throws ServiceException {
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameEntity), "setEntity", entity);
	}
	
	/**
	 * Informar (Setando) o Model (QUERY NATIVE) que a classe <ModelManager> irá trabalhar passando o nome do Model
	 * @param nameModel - Nome do Model (QUERY NATIVE)
	 * @throws ServiceException
	 */
	protected void setModel(String nameModel) throws ServiceException {
		Model<?> model = (Model<?>) MethodReflection.findDtoUsingClassLoader(nameModel);
		methodInvoker.invokeMethodReturnObjectWithParameters(MethodReflection.getNameService(nameModel), "setModel", model);
	}
	
	private ExclusionStrategy strategy = new ExclusionStrategy() {
		@Override
		public boolean shouldSkipField(FieldAttributes field) {
			if (field.getName().contains("links")) {
				return true;
			}
			if (field.getDeclaringClass() == ProductCategory.class && field.getName().equals("id")) {
				return false;
			}
			if (field.getDeclaringClass() == ProductOption.class && field.getName().equals("id")) {
				return false;
			}

			return false;
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	};

}
