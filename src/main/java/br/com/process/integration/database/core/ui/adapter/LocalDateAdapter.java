package br.com.process.integration.database.core.ui.adapter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
	
	private boolean revert = false;
	
	public LocalDateAdapter() {}
	
	public LocalDateAdapter(Boolean revert) {
		this.revert = revert;
	}

	@Override
	public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
		jsonWriter.value(localDate != null ? localDate.format(formatter) : null);
	}

	@Override
	public LocalDate read(JsonReader jsonReader) throws IOException {

		if (revert) {
			jsonReader.beginArray();
			int year = jsonReader.nextInt();
			int month = jsonReader.nextInt();
			int day = jsonReader.nextInt();
			jsonReader.endArray();
			return LocalDate.of(year, month, day);
		}

		return LocalDate.parse(jsonReader.nextString(), formatter);
	}
}
