package br.com.process.integration.database.generator;

import java.util.Set;

public class MainApplication {
    public static void main(String[] args) {
        try {
            Set<String> tabelas = Set.of("entity_status", "entity_one", "entity_two", "entity_tree", "entity_four", "entity_five");

            String url = "jdbc:postgresql://localhost:5432/root-database";
            String user = "anderson";
            String pass = "admin";
            
    		String packageName = "br.com.process.integration.database.model.entity.dto.example";

            EntityGenerator generator = new EntityGenerator(url, user, pass, packageName, tabelas);
            generator.run();
            System.out.println("Entidades geradas com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao gerar entidades: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
