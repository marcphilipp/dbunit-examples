package de.marcphilipp.dbunit.example;

import static org.dbunit.operation.DatabaseOperation.CLEAN_INSERT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.ExternalResource;

public class ImportDataSet extends ExternalResource {

	private final Object testInstance;
	private final DataSource dataSource;

	public ImportDataSet(DataSource dataSource, Object testInstance) {
		this.testInstance = testInstance;
		this.dataSource = dataSource;
	}

	private DefaultDataSet emptyDataSet() {
		return new DefaultDataSet();
	}

	@Override
	protected void before() throws Throwable {
		for (Method method : testInstance.getClass().getMethods()) {
			if (method.isAnnotationPresent(DataSet.class)) {
				assertEquals("@DataSet method must not have parameters", 0, method.getParameterTypes().length);
				assertTrue("return type of @DataSet method must be assignable from IDataSet", method.getReturnType()
						.isAssignableFrom(IDataSet.class));
				IDataSet dataSet = (IDataSet) method.invoke(testInstance);
				execute(CLEAN_INSERT, dataSet);
			}
		}
		emptyDataSet();
	}

	public ImportDataSet execute(DatabaseOperation operation, IDataSet dataSet) throws Exception {
		IDatabaseTester databaseTester = createTester();
		databaseTester.setSetUpOperation(operation);
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
		return this;
	}

	protected H2DatabaseTester createTester() {
		return new H2DatabaseTester(dataSource);
	}

}
