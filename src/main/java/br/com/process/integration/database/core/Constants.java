package br.com.process.integration.database.core;

public class Constants {
	
	private Constants() {}
	
	/**
	 *  Url api
	 */
	public static final String API_NAME_REQUEST_MAPPING = "/v1/database";
	
	/**
	 *  Inicio do nome do diretorio
	 */
	public static final String DIRECTORY_APPLICATION = "src.main.java.";
	
	/**
	 *  Inicio do nome do diretorio
	 */
	public static final String DIRECTORY_RESOURCES = "src.main.resources";
	
	/**
	 *  Pattern para aplicar no JSON mapper @JsonFormat - Date Time
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	/**
	 *  Pattern para aplicar no JSON mapper @JsonFormat - Date
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";	
	
	/**
	 * Packages JPA, MyBatis and JDBC 
	 */
	public static final String JPA_ENTITY      = "br.com.process.integration.database.model.entity.dto.";
	public static final String JPA_REPOSITORY  = "br.com.process.integration.database.model.entity.repository.";
	public static final String JPA_SERVICE     = "br.com.process.integration.database.model.entity.service.";
	
	/**
	 * Packages JPA, MyBatis and JDBC 
	 */
	public static final String MYBATIS_DATA    = "br.com.process.integration.database.model.data.dto.";
	public static final String MYBATIS_MAPPER  = "br.com.process.integration.database.model.data.mapper.";
	public static final String MYBATIS_SERVICE = "br.com.process.integration.database.model.data.service.";

	/**
	 * Packages JPA, MyBatis and JDBC 
	 */
	public static final String JDBC_VIEW       = "br.com.process.integration.database.model.view.dto.";
	public static final String JDBC_SERVICE    = "br.com.process.integration.database.model.view.service.";

	/**
	 *  Nome do pacote onde estao as classes das entidades (entitys)
	 */
	public static final String PACKAGE_NAME_ENTITY = JPA_ENTITY.substring(0, JPA_ENTITY.length() - 1);
	
	/**
	 *  Nome do pacote onde estao as classes dos views
	 */
	public static final String PACKAGE_NAME_VIEW = JDBC_VIEW.substring(0, JDBC_VIEW.length() - 1);
	
	/**
	 *  Nome do pacote onde estao as classes dos datas
	 */
	public static final String PACKAGE_NAME_DATA = MYBATIS_DATA.substring(0, MYBATIS_DATA.length() - 1);
	
	/**
	 * Referencia ao identificador da operacao
	 */
	public static final String IDENTITY_OPERATOR = "_op";
	
	public static final String HATEOAS_LINKS = "links";
	
	/**
	 * Nome do metodo do Service
	 */
	public static final String METHOD_COUNT          = "count";
	public static final String METHOD_FIND_BY_SINGLE = "findBySingle";
	public static final String METHOD_FIND_ALL       = "findAll";
	public static final String METHOD_FIND_PAGINATOR = "findPaginator";
	public static final String METHOD_FIND_ALL_BY_ID = "findAllById";
	public static final String METHOD_FIND_BY_ID     = "findById";
	public static final String METHOD_EXISTS_BY_ID   = "existsById"; 
	
	public static final String METHOD_DELETE_ALL       = "deleteAll"; 
	public static final String METHOD_DELETE_ALL_BY_ID = "deleteAllById"; 
	public static final String METHOD_DELETE_BY_ID     = "deleteById"; 
	public static final String METHOD_DELETE           = "delete"; 
	
	public static final String METHOD_SAVE               = "save"; 
	public static final String METHOD_SAVE_AND_FLUSH     = "saveAndFlush"; 
	public static final String METHOD_SAVE_ALL           = "saveAll"; 
	public static final String METHOD_SAVE_ALL_AND_FLUSH = "saveAllAndFlush"; 
	
	public static final String METHOD_EXECUTE_QUERY_NATIVE_COUNT     = METHOD_COUNT;
	public static final String METHOD_EXECUTE_QUERY_NATIVE_SINGLE    = METHOD_FIND_BY_SINGLE; 
	public static final String METHOD_EXECUTE_QUERY_NATIVE           = METHOD_FIND_ALL;
	public static final String METHOD_EXECUTE_QUERY_NATIVE_PAGINATOR = METHOD_FIND_PAGINATOR; 
	
	public static final String METHOD_EXECUTE_MAPPER_COUNT  = METHOD_COUNT;
	public static final String METHOD_EXECUTE_MAPPER_SINGLE = METHOD_FIND_BY_SINGLE; 
	public static final String METHOD_EXECUTE_MAPPER_ALL    = METHOD_FIND_ALL;
	public static final String METHOD_EXECUTE_PAGINATOR     = METHOD_FIND_PAGINATOR;
	
	public static final String METHOD_SET_ID     = "setId"; 
	public static final String METHOD_SET_ENTITY = "setEntity";
	public static final String METHOD_SET_VIEW   = "setView"; 
	public static final String METHOD_SET_DATA   = "setData";
	
	public static final String NAME_PAGE = "page";
	public static final String NAME_SIZE = "size";
	
	public static final int NUMBER_PAGE_DEFAULT = 0;
	public static final int NUMBER_SIZE_DEFAULT = 10;
	
	public static final String SORT_LIST   = "sortList";
	public static final String SORT_ORDERS = "sortOrders";
	public static final String SORT        = "sort";
	
	/**
	 * Operadores Lógicos
	 */
	public static final String AND = " AND ";
	public static final String OR  = " OR ";
	public static final String NOT = " NOT ";

	/**
	 * Operadores de Comparacao
	 */
	public static final String IGUAL          = " = ";
	public static final String DIFERENTE      = " <> ";
	public static final String MAIOR_QUE      = " > ";
	public static final String MENOR_QUE      = " < ";
	public static final String MAIOR_OU_IGUAL = " >= ";
	public static final String MENOR_OU_IGUAL = " <= ";

	/**
	 * Outros Operadores
	 */
	public static final String LIKE          = "LIKE";
	public static final String IN            = "IN";
	public static final String IS_NULL       = "IS NULL";
	public static final String IS_NOT_NULL   = "IS NOT NULL";
	public static final String BETWEEN       = "BETWEEN";
	public static final String BETWEEN_START = "$Start";
	public static final String BETWEEN_END   = "$End";
	public static final String DOIS_PONTOS   = ":";
	
	/**
	 * Operador Ordenacao
	 */
	public static final String ASC  = "asc";
	public static final String DESC = "desc";
	
	/**
	 * Operadores no formato Predicate
	 */
	public static final String PREDICATE_EQUAL                    = "equal";
	public static final String PREDICATE_GREATER_THAN_OR_EQUAL_TO = "greaterThanOrEqualTo";
	public static final String PREDICATE_LESS_THAN_OR_EQUAL_TO    = "lessThanOrEqualTo";
	public static final String PREDICATE_GREATER_THAN             = "greaterThan";
	public static final String PREDICATE_LESS_THAN                = "lessThan";
	public static final String PREDICATE_NOT_EQUAL                = "notEqual";
	public static final String PREDICATE_LIKE                     = "like";
	public static final String PREDICATE_IN                       = "in";
	public static final String PREDICATE_BETWEEN                  = "between";
	
	/**
	 * Operadores no formato HTML
	 */
	public static final String HTML_MAIOR_QUE      = "gt";
	public static final String HTML_MENOR_QUE      = "lt";
	public static final String HTML_MAIOR_OU_IGUAL = "ge";
	public static final String HTML_MENOR_OU_IGUAL = "le";
	public static final String HTML_IGUAL          = "eq";
	public static final String HTML_DIFERENTE      = "ne";
	public static final String HTML_LIKE           = "lk";
	public static final String HTML_IN             = "in";
	public static final String HTML_BETWEEN        = "bt";
	
	/**
	 * Operadocoes SQL
	 */
	public static final String SELECT_COUNT = "SELECT COUNT(*)";
	public static final String FROM         = "FROM";
	public static final String WRITERSPACE  = " ";
	public static final String LIMIT        = "LIMIT :size";
	public static final String OFFSET       = "OFFSET :offset";
	public static final String WHERE        = "WHERE 1=1";
	public static final String ORDER_BY     = "ORDER BY";
	public static final String GROUP_BY     = "GROUP BY";

}
