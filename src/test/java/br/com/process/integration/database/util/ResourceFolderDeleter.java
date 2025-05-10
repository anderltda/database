package br.com.process.integration.database.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class ResourceFolderDeleter {

	public static void deleteResourceFolder(String folderRelativeToResources) throws IOException {
		Path basePath = Paths.get("src/main/resources");
		Path targetPath = basePath.resolve(folderRelativeToResources.replace('.', '/'));

		if (!Files.exists(targetPath)) {
			System.out.println("Pasta não encontrada: " + targetPath);
			return;
		}

		Files.walkFileTree(targetPath, new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});

		System.out.println("Pasta deletada com sucesso: " + targetPath);
	}
}
