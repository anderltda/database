package br.com.process.integration.database.core;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ 
	SaveInsertsFlushTests.class,
	DeleteAllTests.class, 
	SaveFlushTests.class, 
	DeleteTests.class, 
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
	DeleteTests.class, 
	SaveTests.class
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
