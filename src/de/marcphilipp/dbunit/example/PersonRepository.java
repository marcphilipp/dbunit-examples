package de.marcphilipp.dbunit.example;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class PersonRepository {

	private final DataSource dataSource;

	public PersonRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Person findPersonByFirstName(String name) throws SQLException {
		Person person = null;
		PreparedStatement statement = dataSource.getConnection()
				.prepareStatement("select * from PERSON where NAME = ?");
		statement.setString(1, name);
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				person = convertSingleRow(resultSet);
			}
		} finally {
			closeQuietly(resultSet);
			closeQuietly(statement);
		}
		return person;
	}

	private Person convertSingleRow(ResultSet resultSet) throws SQLException {
		String firstName = resultSet.getString("NAME");
		String lastName = resultSet.getString("LAST_NAME");
		int age = resultSet.getInt("AGE");
		Person person2 = new Person(firstName, lastName, age);
		return person2;
	}

	private void closeQuietly(ResultSet resultSet) {
		try {
			resultSet.close();
		} catch (SQLException exception) {
		}
	}

	private void closeQuietly(Statement statement) {
		try {
			statement.close();
		} catch (SQLException exception) {
		}
	}

}
