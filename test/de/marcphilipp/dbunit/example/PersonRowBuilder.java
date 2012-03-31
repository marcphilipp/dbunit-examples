package de.marcphilipp.dbunit.example;

import static org.dbunit.dataset.builder.ColumnSpec.newColumn;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.builder.ColumnSpec;
import org.dbunit.dataset.builder.DataSetBuilder;

public class PersonRowBuilder {

	private static final String TABLE_NAME = "PERSON";

	private static final ColumnSpec<String> NAME = newColumn("NAME");
	private static final ColumnSpec<String> LAST_NAME = newColumn("LAST_NAME");
	private static final ColumnSpec<Integer> AGE = newColumn("AGE");

	private final DataSetBuilder dataSetBuilder;

	private String firstName;
	private String lastName;
	private int age;

	public PersonRowBuilder(DataSetBuilder dataSetBuilder) {
		this.dataSetBuilder = dataSetBuilder;
	}

	public PersonRowBuilder withFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public PersonRowBuilder withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public PersonRowBuilder withAge(int age) {
		this.age = age;
		return this;
	}

	public void add() throws DataSetException {
		dataSetBuilder.newRow(TABLE_NAME).with(NAME, firstName).with(LAST_NAME, lastName).with(AGE, age).add();
	}

	public static PersonRowBuilder newPerson(DataSetBuilder builder) {
		return new PersonRowBuilder(builder);
	}

}
