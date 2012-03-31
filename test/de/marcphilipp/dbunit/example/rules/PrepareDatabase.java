package de.marcphilipp.dbunit.example.rules;

import javax.sql.DataSource;

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class PrepareDatabase implements TestRule {

	private final DataSource dataSource;
	private final Object testInstance;

	public PrepareDatabase(DataSource dataSource, Object testInstance) {
		this.dataSource = dataSource;
		this.testInstance = testInstance;
	}

	@Override
	public Statement apply(Statement base, Description description) {
		return chain().apply(base, description);
	}

	private RuleChain chain() {
		return RuleChain.outerRule(new OnlyRunOracleTestsOnOracle(dataSource))
				.around(new CreateSchemaIfNecessary(dataSource, "schema.sql"))
				.around(new ImportDataSet(dataSource, testInstance));
	}

}
