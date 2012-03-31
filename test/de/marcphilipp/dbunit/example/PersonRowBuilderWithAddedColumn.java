package de.marcphilipp.dbunit.example;

import static org.dbunit.dataset.builder.ColumnSpec.newColumn;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.builder.ColumnSpec;
import org.dbunit.dataset.builder.DataSetBuilder;

public class PersonRowBuilderWithAddedColumn {

	private static final String TABLE_NAME = "PERSON";

	private static final ColumnSpec<String> NAME = newColumn("NAME");
	private static final ColumnSpec<String> LAST_NAME = newColumn("LAST_NAME");
	private static final ColumnSpec<String> NICKNAME = newColumn("NICKNAME");

	private final DataSetBuilder dataSetBuilder;

	private String firstName;
	private String lastName;
	private String nickname = "Snoopy";

	public PersonRowBuilderWithAddedColumn(DataSetBuilder dataSetBuilder) {
		this.dataSetBuilder = dataSetBuilder;
	}

	public PersonRowBuilderWithAddedColumn withFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public PersonRowBuilderWithAddedColumn withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public PersonRowBuilderWithAddedColumn withAge(int age) {
		return this;
	}

	public PersonRowBuilderWithAddedColumn withNickName(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public void add() throws DataSetException {
		dataSetBuilder.newRow(TABLE_NAME).with(NAME, firstName).with(LAST_NAME, lastName).with(NICKNAME, nickname)
				.add();
	}

	public static PersonRowBuilderWithAddedColumn newPerson(DataSetBuilder builder) {
		return new PersonRowBuilderWithAddedColumn(builder);
	}

}
