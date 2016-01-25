package ar.com.inmune.deutsch.exercise.test;

import java.io.IOException;

import org.junit.Test;

import ar.com.inmune.deutsch.exercise.Exercise;
import ar.com.inmune.deutsch.exercise.ExerciseRunner;
import ar.com.inmune.deutsch.exercise.TextExerciseRunner;
import ar.com.inmune.deutsch.exercise.builder.ExerciseBuilder;
import ar.com.inmune.deutsch.exercise.builder.PrepositionInUse;
import ar.com.inmune.deutsch.exercise.builder.VerbConjugationPresent;

public class TestExerciseBuilders {

	@Test
	public void testVerbConjugationExercise() throws IOException {
		ExerciseBuilder vceb = new VerbConjugationPresent();
		
		TextExerciseRunner runner = new TextExerciseRunner(System.out, System.in);
		doExercises(runner, vceb, 10);
	}
	
	@Test
	public void testInUse() throws IOException {
		PrepositionInUse piu = new PrepositionInUse();
		TextExerciseRunner runner = new TextExerciseRunner(System.out, System.in);
		doExercises(runner, piu, 20);
	}
	
	@Test
	public void test() {
		try {

			TextExerciseRunner runner = new TextExerciseRunner(System.out, System.in);

			for (Exercise.Type type : Exercise.Type.values()) {
				ExerciseBuilder builder = type.getBuilder();
				doExercises(runner, builder, 200);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	private void printExercises(ExerciseBuilder builder, int n) {
//		for (int i = 0 ; i < n ; i++) {
//			Exercise ex = builder.getRandomExercise();
//			System.out.println(ex.toString());
//		}
//	}

	private void doExercises(ExerciseRunner runner, ExerciseBuilder builder, int n) throws IOException {
		for (int i = 0 ; i < n ; i++) {
			Exercise ex = builder.getRandomExercise();
			System.out.print(ex.getQuestion() + "," + ex.correctStr());
			for (String opt : ex.options) {
				System.out.print("," + opt);
			}
			System.out.println();
		}
	}
}
