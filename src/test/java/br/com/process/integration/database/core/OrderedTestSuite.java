package br.com.process.integration.database.core;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ 
	InitTests.class,
	SaveInsertsFlushTests.class,
	DeleteAllTests.class, 
	SaveTests.class,
	CriteriaTests.class,
	CriteriaJoinTests.class,
	CriteriaPaginatorTests.class,
	CriteriaPaginatorJoinTests.class,
	JPQLTests.class,
	JPQLPaginatorTests.class,
	JDBCTests.class,
	JDBCPaginatorTests.class,
	MyBatisTests.class,
	MyBatisPaginatorTests.class,
	EntityGeneratorTests.class,
	ViewGeneratorTests.class,
	DataGeneratorTests.class,
	ClearAllTests.class
})
public class OrderedTestSuite {
	
	public static void sysout(String url) {
		
        String formatted = url.substring(url.indexOf("?")).replace("&", "&\n")
                .lines()
                .map(line -> "    + \"" + line + "\"")
                .reduce("", (acc, line) -> acc + line + "\n");

        System.out.println(formatted);
	}

}
