package br.com.process.integration.database.core.ui.adapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	
	private boolean revert = false;
	
	public LocalDateTimeAdapter() {}
	
	public LocalDateTimeAdapter(Boolean revert) {
		this.revert = revert;
	}

	@Override
	public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
		jsonWriter.value(localDateTime != null ? localDateTime.format(formatter) : null);
	}

	@Override
	public LocalDateTime read(JsonReader jsonReader) throws IOException {

		if (revert) {
			jsonReader.beginArray();
			int year = jsonReader.nextInt();
			int month = jsonReader.nextInt();
			int day = jsonReader.nextInt();
			int hour = jsonReader.nextInt();
			int minute = jsonReader.nextInt();
			int second = jsonReader.hasNext() ? jsonReader.nextInt() : 0;
			jsonReader.endArray();
			return LocalDateTime.of(year, month, day, hour, minute, second);
		}

		return LocalDateTime.parse(jsonReader.nextString(), formatter);
	}
}
