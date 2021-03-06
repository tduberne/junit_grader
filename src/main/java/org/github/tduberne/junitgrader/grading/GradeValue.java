package org.github.tduberne.junitgrader.grading;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author thibautd
 */
@Retention( RetentionPolicy.RUNTIME )
public @interface GradeValue {
	double value();
}
