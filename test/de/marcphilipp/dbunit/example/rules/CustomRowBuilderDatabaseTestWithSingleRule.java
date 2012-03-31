package de.marcphilipp.dbunit.example.rules;

import static de.marcphilipp.dbunit.example.h2.TestDataSource.dataSource;
import static de.marcphilipp.dbunit.example.wrapped.PersonRowBuilder.newPerson;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.builder.DataSetBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import de.marcphilipp.dbunit.example.Person;
import de.marcphilipp.dbunit.example.PersonRepository;

public class CustomRowBuilderDatabaseTestWithSingleRule {

	@Rule
	public TestRule database = new PrepareDatabase(dataSource(), this);

	@DataSet
	public IDataSet dataSet() throws DataSetException {
		DataSetBuilder builder = new DataSetBuilder();
		newPerson(builder).withFirstName("Bob").withLastName("Doe").withAge(18).add();
		newPerson(builder).withFirstName("Alice").withLastName("Foo").withAge(23).add();
		newPerson(builder).withFirstName("Charlie").withLastName("Brown").withAge(42).add();
		return builder.build();
	}

	@Test
	@OracleOnly
	public void oracleSpecificStuff() throws Exception {
		System.out.println("Hello from Delphi!");
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
}
