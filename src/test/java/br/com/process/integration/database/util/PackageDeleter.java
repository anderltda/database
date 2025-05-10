package br.com.process.integration.database.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class PackageDeleter {

    public static void deletePackage(String baseSourcePath, String packageName) throws IOException {
        // Converte o package em path
        String relativePath = packageName.replace('.', '/');
        Path pathToDelete = Paths.get(baseSourcePath, relativePath);

        if (!Files.exists(pathToDelete)) {
            System.out.println("Pacote não encontrado: " + pathToDelete);
            return;
        }

        // Deleta recursivamente
        Files.walkFileTree(pathToDelete, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file); // Apaga o arquivo
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir); // Apaga o diretório após os arquivos
                return FileVisitResult.CONTINUE;
            }
        });

        System.out.println("Pacote deletado com sucesso: " + pathToDelete);
    }

    public static void main(String[] args) throws IOException {
        // Exemplo:
        deletePackage("src/main/resources", "test");
    }
}
