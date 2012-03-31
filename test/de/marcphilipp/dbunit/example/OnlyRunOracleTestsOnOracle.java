package de.marcphilipp.dbunit.example;

import static org.junit.matchers.JUnitMatchers.containsString;

import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hamcrest.Matcher;
import org.junit.runner.Description;

public class OnlyRunOracleTestsOnOracle extends ConditionalTestExecuter {

	private final DataSource dataSource;
	private final Matcher<String> jdbcUrlMatcher;

	public OnlyRunOracleTestsOnOracle(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcUrlMatcher = containsString("jdbc:oracle:thin:");
	}

	@Override
	protected boolean shouldSkipTest(Description description) {
		return hasAnnotation(description, OracleOnly.class) && !isOracleDataSource();
	}

	private boolean hasAnnotation(Description description, Class<? extends Annotation> annotation) {
		return hasAnnotationOnClass(description, annotation) || hasAnnotationOnMethod(description, annotation);
	}

	private boolean hasAnnotationOnMethod(Description description, Class<? extends Annotation> annotation) {
		return description.getAnnotation(annotation) != null;
	}

	private boolean hasAnnotationOnClass(Description description, Class<? extends Annotation> annotation) {
		Class<?> testClass = description.getTestClass();
		return testClass != null && testClass.getAnnotation(annotation) != null;
	}

	private boolean isOracleDataSource() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			return jdbcUrlMatcher.matches(connection.getMetaData().getURL());
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		} finally {
			closeQuietly(connection);
		}
	}

	private void closeQuietly(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException exception) {
			}
		}
	}
}
