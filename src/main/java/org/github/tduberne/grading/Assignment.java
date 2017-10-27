package org.github.tduberne.grading;

import org.junit.runner.Description;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thibautd
 */
class Assignment {
	private final Map<String, Exercise> exercises = new HashMap<>();

	public void addExercise( Class<?> testClass ) {
		if ( !exercises.containsKey( testClass.getCanonicalName() ) ) {
			this.exercises.put( testClass.getCanonicalName() , new Exercise( testClass ) );
		}
	}

	public void handleSuccess( Description description , boolean success ) {
		Exercise exercise = exercises.get( description.getTestClass().getCanonicalName() );
		if ( exercise == null ) return; // ungraded test

		Question question = exercise.getQuestion( description.getMethodName() );
		if ( question == null ) return; // ungraded test

		question.notifySuccess( success );
	}

	public String scoreInfo() {
		StringBuilder b = new StringBuilder( "total score on all scored exercises: [" + calcScore() + " / " + calcMaxScore() + "]\n" );
		for ( Exercise e : exercises.values() ) e.appendScoreInfo( b , "\t" );
		return b.toString();
	}

	private int calcMaxScore() {
		return exercises.values().stream()
				.mapToInt( e -> e.maxPoints )
				.sum();
	}

	private int calcScore() {
		return exercises.values().stream()
				.mapToInt( Exercise::calcScore )
				.sum();
	}

	public static class Exercise {
		private final String className;
		private final int maxPoints;

		private final Map<String,Question> questions = new HashMap<>();

		public Exercise( Class<?> testClass ) {
			this.className = testClass.getCanonicalName();

			int sum = 0;
			for ( Method m : testClass.getMethods() ) {
				GradeValue value = m.getAnnotation( GradeValue.class );
				if ( value != null ) {
					addQuestion( new Question( m , value ) );
					sum += value.value();
				}
			}
			this.maxPoints = sum;
		}

		private void addQuestion( final Question question ) {
			questions.put( question.methodName , question );
		}

		public Question getQuestion( final String name ) {
			return questions.get( name );
		}

		public String scoreInfo() {
			StringBuilder builder = new StringBuilder();
			appendScoreInfo( builder , "" );
			return builder.toString();
		}

		private void appendScoreInfo( final StringBuilder builder , final String s ) {
			builder.append( s + className + " got score ["+calcScore()+" / "+maxPoints+"]\n" );
			for ( Question q : questions.values() ) {
				builder.append( s + "\t" + q.scoreInfo()+"\n" );
			}
		}

		public int calcScore() {
			return questions.values().stream()
					.mapToInt( q -> q.points )
					.sum();
		}
	}

	public static class Question {
		private final String methodName;
		private final int maxPoints;
		private int points = 0;

		public Question( Method method , GradeValue value ) {
			this.methodName = method.getName();
			this.maxPoints = value.value();
		}

		public void notifySuccess( final boolean success ) {
			points = success ? maxPoints : 0;
		}

		public String scoreInfo() {
			return methodName+" got score ["+points+" / "+maxPoints+"]";
		}
	}
}
