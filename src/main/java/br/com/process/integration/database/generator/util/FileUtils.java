package br.com.process.integration.database.generator.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * 
 */
public class FileUtils {

	/**
	 * @param filePath
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static boolean writeIfChanged(String filePath, String content) throws IOException {
		File file = new File(filePath);
		boolean changed = true;

		if (file.exists()) {
			String existing = Files.readString(Path.of(filePath));
			changed = !Objects.equals(existing, content);
		}

		if (changed) {
			try (FileWriter writer = new FileWriter(filePath)) {
				writer.write(content);
			}
		}

		return changed;
	}

	/**
	 * @param path
	 * @throws IOException
	 */
	public static void ensureDirectoryExists(String path) throws IOException {
		Path dir = Path.of(path);
		if (!Files.exists(dir)) {
			Files.createDirectories(dir);
		}
	}
}
