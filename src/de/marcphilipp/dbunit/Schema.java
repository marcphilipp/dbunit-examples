package de.marcphilipp.dbunit;

import static org.dbunit.dataset.builder.ColumnSpec.newColumn;

import org.dbunit.dataset.builder.ColumnSpec;

public class Schema {

	static class Tables {
		static final String PERSON = "PERSON";
	}

	static class PersonTable {
		static final ColumnSpec<String> NAME = newColumn("NAME");
		static final ColumnSpec<String> LAST_NAME = newColumn("LAST_NAME");
		static final ColumnSpec<Integer> AGE = newColumn("AGE");
	}

}
