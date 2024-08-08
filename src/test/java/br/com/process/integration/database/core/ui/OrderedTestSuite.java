package br.com.process.integration.database.core.ui;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ 
	QueryJpaControllerSaveTests.class, 
	QueryJpaControllerDeleteTests.class, 
	QueryJpaController1Tests.class,
	QueryJpaController2Tests.class,
	QueryJpaController3Tests.class,
	QueryJpaController4Tests.class,
	QueryJpaController5Tests.class,
	QueryJpaController6Tests.class,
	QueryJpaController7Tests.class
})
public class OrderedTestSuite {

}
