package br.com.process.integration.database.core.util;

public class Constants {
	
	private Constants() {}
	
	public static final String API_NAME_REQUEST_MAPPING = "/v1/api-rest-database";
	
	/**
	 *  Inicio do nome do diretorio
	 */
	public static final String DIRECTORY_APPLICATION = "src.main.java.";
	
	/**
	 *  Inicio do nome do diretorio
	 */
	public static final String DIRECTORY_RESOURCES = "src.main.resources";

	/**
	 *  Nome do pacote onde estao as classes das entidades (entitys)
	 */
	public static final String PACKAGE_NAME_ENTITY = "br.com.process.integration.database.domain.entity";
	
	/**
	 *  Nome do pacote onde estao as classes dos views
	 */
	public static final String PACKAGE_NAME_VIEW = "br.com.process.integration.database.domain.view";
	
	/**
	 * Referencia ao identificador da operacao
	 */
	public static final String IDENTITY_OPERATOR = "_op";
	
	/**
	 * Nome do metodo do Service
	 */
	public static final String METHOD_FIND_ALL = "findAll";
	public static final String METHOD_FIND_BY_SINGLE = "findBySingle";
	public static final String METHOD_COUNT = "count";
	public static final String METHOD_FIND_ALL_BY_ID = "findAllById";
	public static final String METHOD_FIND_BY_ID = "findById";
	public static final String METHOD_EXISTS_BY_ID = "existsById"; 
	
	public static final String METHOD_DELETE_ALL = "deleteAll"; 
	public static final String METHOD_DELETE_ALL_BY_ID = "deleteAllById"; 
	public static final String METHOD_DELETE_BY_ID = "deleteById"; 
	public static final String METHOD_DELETE = "delete"; 
	
	public static final String METHOD_SAVE = "save"; 
	public static final String METHOD_SAVE_AND_FLUSH = "saveAndFlush"; 
	public static final String METHOD_SAVE_ALL = "saveAll"; 
	public static final String METHOD_SAVE_ALL_AND_FLUSH = "saveAllAndFlush"; 
	
	public static final String METHOD_EXECUTE_QUERY_NATIVE_SINGLE = "executeQueryNativeSingle"; 
	public static final String METHOD_EXECUTE_QUERY_NATIVE = "executeQueryNative";
	public static final String METHOD_EXECUTE_QUERY_NATIVE_PAGINATOR = "executeQueryNativePaginator"; 
	public static final String METHOD_EXECUTE_QUERY_NATIVE_COUNT = "executeQueryNativeCount";
	
	public static final String SORT_LIST = "sortList";
	public static final String SORT_ORDERS = "sortOrders";
	
	/**
	 * Operadores Lógicos
	 */
	public static final String AND = " AND ";
	public static final String OR = " OR ";
	public static final String NOT = " NOT ";

	/**
	 * Operadores de Comparacao
	 */
	public static final String IGUAL = " = ";
	public static final String DIFERENTE = " <> ";
	public static final String MAIOR_QUE = " > ";
	public static final String MENOR_QUE = " < ";
	public static final String MAIOR_OU_IGUAL = " >= ";
	public static final String MENOR_OU_IGUAL = " <= ";

	/**
	 * Outros Operadores
	 */
	public static final String LIKE = "LIKE";
	public static final String IN = "IN";
	public static final String IS_NULL = "IS NULL";
	public static final String IS_NOT_NULL = "IS NOT NULL";
	public static final String BETWEEN = "BETWEEN";
	public static final String BETWEEN_START = "Start";
	public static final String BETWEEN_END = "End";
	public static final String DOIS_PONTOS = ":";
	
	/**
	 * Operadores no formato Predicate
	 */
	public static final String PREDICATE_EQUAL = "equal";
	public static final String PREDICATE_GREATER_THAN_OR_EQUAL_TO =  "greaterThanOrEqualTo";
	public static final String PREDICATE_LESS_THAN_OR_EQUAL_TO =  "lessThanOrEqualTo";
	public static final String PREDICATE_GREATER_THAN = "greaterThan";
	public static final String PREDICATE_LESS_THAN = "lessThan";
	public static final String PREDICATE_NOT_EQUAL = "notEqual";
	public static final String PREDICATE_LIKE = "like";
	public static final String PREDICATE_IN = "in";
	public static final String PREDICATE_BETWEEN = "between";
	
	/**
	 * Operadores no formato HTML
	 */
	public static final String HTML_MAIOR_QUE = "gt";
	public static final String HTML_MENOR_QUE = "lt";
	public static final String HTML_MAIOR_OU_IGUAL = "ge";
	public static final String HTML_MENOR_OU_IGUAL = "le";
	public static final String HTML_IGUAL = "eq";
	public static final String HTML_DIFERENTE = "ne";
	public static final String HTML_LIKE = "lk";
	public static final String HTML_IN = "in";
	public static final String HTML_BETWEEN = "bt";

	
	public static final String SELECT_COUNT = "SELECT COUNT(*)";
	public static final String FROM = "FROM";
	public static final String WRITERSPACE = " ";
	public static final String LIMIT = "LIMIT :size";
	public static final String OFFSET = "OFFSET :offset";
	public static final String WHERE = "WHERE 1=1";
	public static final String ORDER_BY = "ORDER BY";
	public static final String GROUP_BY = "GROUP BY";
	
	


}
