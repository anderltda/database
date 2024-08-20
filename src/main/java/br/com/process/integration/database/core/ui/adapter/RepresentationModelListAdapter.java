package br.com.process.integration.database.core.ui.adapter;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class RepresentationModelListAdapter implements JsonSerializer<List<RepresentationModel<?>>> {

	@Override
	public JsonElement serialize(List<RepresentationModel<?>> src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src, new TypeToken<List<RepresentationModel<?>>>() { }.getType());
	}
}
