package de.marcphilipp.dbunit.example.builder;

import static org.h2.engine.Constants.UTF8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.builder.DataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.marcphilipp.dbunit.example.Person;
import de.marcphilipp.dbunit.example.PersonRepository;

public class BuilderDatabaseTest {

	private static final String JDBC_DRIVER = org.h2.Driver.class.getName();
	private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	private static final String USER = "sa";
	private static final String PASSWORD = "";

	@BeforeClass
	public static void createSchema() throws Exception {
		if (JDBC_URL.startsWith("jdbc:h2:mem:")) {
			RunScript.execute(JDBC_URL, USER, PASSWORD, "schema.sql", UTF8, false);
		}
	}

	@Before
	public void importDataSet() throws Exception {
		IDataSet dataSet = buildDataSet();
		cleanlyInsert(dataSet);
	}

	private IDataSet buildDataSet() throws DataSetException {
		DataSetBuilder builder = new DataSetBuilder();
		builder.newRow("PERSON").with("NAME", "Bob").with("LAST_NAME", "Doe").with("AGE", 18).add();
		builder.newRow("PERSON").with("NAME", "Alice").with("LAST_NAME", "Foo").with("AGE", 23).add();
		builder.newRow("PERSON").with("NAME", "Charlie").with("LAST_NAME", "Brown").with("AGE", 42).add();
		return builder.build();
	}

	private void cleanlyInsert(IDataSet dataSet) throws Exception {
		IDatabaseTester databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		databaseTester.setDataSet(dataSet);
		databaseTester.onSetup();
	}

	@Test
	public void findsAndReadsExistingPersonByFirstName() throws Exception {
		PersonRepository repository = new PersonRepository(dataSource());
		Person charlie = repository.findPersonByFirstName("Charlie");

		assertThat(charlie.getFirstName(), is("Charlie"));
		assertThat(charlie.getLastName(), is("Brown"));
		assertThat(charlie.getAge(), is(42));
	}

	@Test
	public void returnsNullWhenPersonCannotBeFoundByFirstName() throws Exception {
		PersonRepository repository = new PersonRepository(dataSource());
		Person person = repository.findPersonByFirstName("iDoNotExist");

		assertThat(person, is(nullValue()));
	}

	private DataSource dataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL(JDBC_URL);
		dataSource.setUser(USER);
		dataSource.setPassword(PASSWORD);
		return dataSource;
	}
}
