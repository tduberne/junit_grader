package org.github.tduberne.grading;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.function.Consumer;

/**
 * @author thibautd
 */
public class GradingRule implements TestRule {
	private static final Assignment assignment = new Assignment();

	private Consumer<Assignment> listeners = assignment -> {};

	public GradingRule() {
		this( true );
	}

	public GradingRule( final boolean useDefaultListeners ) {
		if ( useDefaultListeners ) withDefaultListeners();
	}

	public GradingRule withDefaultListeners() {
		return withLoggingListener()
				.withWriterListener( "grades.txt" );
	}

	public GradingRule withLoggingListener() {
		return withListener( a -> System.out.println( a.scoreInfo() ) );
	}

	public GradingRule withWriterListener( String file ) {
		return withListener( new ScoreWriter( file )::writeScores );
	}

	public GradingRule withListener( Consumer<Assignment> listener ) {
		this.listeners = listeners.andThen( listener );
		return this;
	}

	@Override
	public Statement apply( final Statement statement , final Description description ) {
		assignment.addExercise( description.getTestClass() );

		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				try {
					statement.evaluate();
					assignment.handleSuccess( description , true );
				}
				catch ( Throwable t ) {
					assignment.handleSuccess( description , false );
					throw t;
				}
				finally {
					listeners.accept( assignment );
				}
			}
		};
	}

	public String scoreSummary() {
		return assignment.scoreInfo();
	}

}
