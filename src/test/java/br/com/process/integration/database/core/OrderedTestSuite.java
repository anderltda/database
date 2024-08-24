package br.com.process.integration.database.core;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ 
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
	MyBatisTests.class
})
public class OrderedTestSuite {

}
