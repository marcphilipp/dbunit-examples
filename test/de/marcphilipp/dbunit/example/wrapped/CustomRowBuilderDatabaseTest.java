package de.marcphilipp.dbunit.example.wrapped;

import static de.marcphilipp.dbunit.example.wrapped.PersonRowBuilder.newPerson;
import static org.h2.engine.Constants.UTF8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.sql.DataSource;

import org.dbunit.IDatabaseTester;
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
import de.marcphilipp.dbunit.example.h2.H2DatabaseTester;

public class CustomRowBuilderDatabaseTest {

	private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	private static final String USER = "sa";
	private static final String PASSWORD = "";

	@BeforeClass
	public static void createSchema() throws Exception {
		RunScript.execute(JDBC_URL, USER, PASSWORD, "schema.sql", UTF8, false);
	}

	@Before
	public void importDataSet() throws Exception {
		IDataSet dataSet = buildDataSet();
		cleanlyInsert(dataSet);
	}

	private IDataSet buildDataSet() throws DataSetException {
		DataSetBuilder builder = new DataSetBuilder();
		newPerson(builder).withFirstName("Bob").withLastName("Doe").withAge(18).add();
		newPerson(builder).withFirstName("Alice").withLastName("Foo").withAge(23).add();
		newPerson(builder).withFirstName("Charlie").withLastName("Brown").withAge(42).add();
		return builder.build();
	}

	private void cleanlyInsert(IDataSet dataSet) throws Exception {
		IDatabaseTester databaseTester = new H2DatabaseTester(dataSource());
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
