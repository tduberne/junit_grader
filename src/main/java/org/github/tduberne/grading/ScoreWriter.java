package org.github.tduberne.grading;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author thibautd
 */
public class ScoreWriter {
	private final String file;

	public ScoreWriter( final String file ) {
		this.file = file;
	}

	public void writeScores( Assignment assignment ) {
		try ( BufferedWriter writer = new BufferedWriter( new FileWriter( file ) ) ) {
			writer.write( assignment.scoreInfo() );
		}
		catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}
}
