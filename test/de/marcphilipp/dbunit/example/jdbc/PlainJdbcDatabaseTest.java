package de.marcphilipp.dbunit.example.jdbc;

import static org.h2.engine.Constants.UTF8;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.marcphilipp.dbunit.example.Person;
import de.marcphilipp.dbunit.example.PersonRepository;

public class PlainJdbcDatabaseTest {

	private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	private static final String USER = "sa";
	private static final String PASSWORD = "";

	private Connection connection;

	@BeforeClass
	public static void createSchema() throws Exception {
		RunScript.execute(JDBC_URL, USER, PASSWORD, "schema.sql", UTF8, false);
	}

	@Before
	public void insertRows() throws Exception {
		connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
		cleanPersonTable();
		insert(new Person("Bob", "Doe", 18));
		insert(new Person("Alice", "Foo", 23));
		insert(new Person("Charlie", "Brown", 42));
	}

	protected void cleanPersonTable() throws SQLException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM PERSON");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ignore) {
				}
			}
		}
	}

	protected void insert(Person person) throws SQLException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("INSERT INTO PERSON (NAME, LAST_NAME, AGE) VALUES (?, ?, ?)");
			statement.setString(1, person.getFirstName());
			statement.setString(2, person.getLastName());
			statement.setInt(3, person.getAge());
			statement.executeUpdate();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException ignore) {
				}
			}
		}
	}

	@After
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException ignore) {
			}
		}
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
