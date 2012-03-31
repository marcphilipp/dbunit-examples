package de.marcphilipp.dbunit.example.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract class ConditionalTestExecuter implements TestRule {

	private static final class EmptyStatement extends Statement {
		@Override
		public void evaluate() throws Throwable {
			// do nothing
		}
	}

	@Override
	public Statement apply(Statement base, Description description) {
		if (shouldSkipTest(description)) {
			return new EmptyStatement();
		}
		return base;
	}

	protected abstract boolean shouldSkipTest(Description description);

}
