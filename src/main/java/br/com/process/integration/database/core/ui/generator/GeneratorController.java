package br.com.process.integration.database.core.ui.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.process.integration.database.generator.GeneratorOrm;

/**
 * 
 */
@Controller
@RequestMapping("/v1/database")
public class GeneratorController {

	private final DataSource dataSource;
	private final GeneratorOrm generator;
	
    /**
     * @param dataSource
     * @param mainGenerator
     */
    public GeneratorController(DataSource dataSource, GeneratorOrm generator) {
		this.dataSource = dataSource;
		this.generator = generator;
	}

	/**
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping()
    public String index(Model model) throws Exception {
        model.addAttribute("title", "Gerador de Entidades ORM!");
        model.addAttribute("tables", getTables()); // Adiciona a lista de tabelas no model
        return "index";
    }
    
	/**
	 * @param formData
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/generator")
	public String save(@ModelAttribute FormData formData, Model model) throws Exception {

		generator.generateAll(formData.getDomain(), new LinkedHashSet<>(formData.getTables()), formData.getTypes());

		model.addAttribute("dados", formData);
		
		return "confirmacao";
	}
    
	private List<String> getTables() throws Exception {
		List<String> tableList = new ArrayList<>();
		try (Connection conn = dataSource.getConnection()) {
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
			while (tables.next()) {
				String table = tables.getString("TABLE_NAME");
				tableList.add(table);
			}
		}
		return tableList;
	}
    
}