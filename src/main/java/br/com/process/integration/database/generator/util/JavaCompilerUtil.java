package br.com.process.integration.database.generator.util;

import javax.tools.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JavaCompilerUtil {

    private static final String SOURCE_DIR = "src/main/java";
    private static final String CLASS_OUTPUT_DIR = "target/classes";

    public static List<Class<?>> compileAndLoadClasses(List<String> qualifiedClassNames) {
        List<Class<?>> loadedClasses = new ArrayList<>();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("JavaCompiler não encontrado. Verifique se está usando um JDK e não apenas um JRE.");
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        List<File> javaFiles = new ArrayList<>();

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            for (String qualifiedClassName : qualifiedClassNames) {
                String path = SOURCE_DIR + "/" + qualifiedClassName.replace('.', '/') + ".java";
                File file = new File(path);
                if (!file.exists()) {
                    throw new RuntimeException("Arquivo .java não encontrado: " + path);
                }
                javaFiles.add(file);
            }

            File outputDir = new File(CLASS_OUTPUT_DIR);
            if (!outputDir.exists()) outputDir.mkdirs();

            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(javaFiles);
            List<String> options = List.of("-d", CLASS_OUTPUT_DIR, "-proc:none");

            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
            boolean success = task.call();

            if (!success) {
                StringWriter errorOutput = new StringWriter();
                PrintWriter pw = new PrintWriter(errorOutput);
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    pw.println("Erro de compilação:");
                    pw.println("  Arquivo: " + diagnostic.getSource());
                    pw.println("  Linha: " + diagnostic.getLineNumber());
                    pw.println("  Mensagem: " + diagnostic.getMessage(null));
                }
                pw.flush();
                throw new RuntimeException("Falha ao compilar classes:\n" + errorOutput);
            }

            try (URLClassLoader classLoader = new URLClassLoader(
                    new URL[]{outputDir.toURI().toURL()},
                    JavaCompilerUtil.class.getClassLoader()
            )) {
                for (String qualifiedClassName : qualifiedClassNames) {
                    try {
                        Class<?> clazz = Class.forName(qualifiedClassName, true, classLoader);
                        loadedClasses.add(clazz);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Falha ao carregar a classe compilada: " + qualifiedClassName, e);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao compilar ou carregar classes", e);
        }

        return loadedClasses;
    }
}