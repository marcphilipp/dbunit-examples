package de.marcphilipp.dbunit.example;

import static de.marcphilipp.dbunit.example.Schema.PersonTable.AGE;
import static de.marcphilipp.dbunit.example.Schema.PersonTable.LAST_NAME;
import static de.marcphilipp.dbunit.example.Schema.PersonTable.NAME;
import static de.marcphilipp.dbunit.example.Schema.Tables.PERSON;
import static de.marcphilipp.dbunit.example.TestDataSource.dataSource;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.builder.DataSetBuilder;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class TypeSafeBuilderDatabaseTestWithRules {

	@ClassRule
	public static TestRule schema = CreateSchema.in(dataSource()).using("schema.sql");

	@Rule
	public ImportDataSet database = new ImportDataSet(this, dataSource());

	@DataSet
	public IDataSet dataSet() throws DataSetException {
		DataSetBuilder builder = new DataSetBuilder();
		builder.newRow(PERSON).with(NAME, "Bob").with(LAST_NAME, "Doe").with(AGE, 18).add();
		builder.newRow(PERSON).with(NAME, "Alice").with(LAST_NAME, "Foo").with(AGE, 23).add();
		builder.newRow(PERSON).with(NAME, "Charlie").with(LAST_NAME, "Brown").with(AGE, 42).add();
		return builder.build();
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
