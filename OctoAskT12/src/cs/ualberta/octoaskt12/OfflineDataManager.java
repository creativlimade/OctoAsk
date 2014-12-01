package cs.ualberta.octoaskt12;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

// Grabs all of the questions that the user has asked and saves it in a save file.
// Contains functions that load and clear the .sav file as well as ones that can be used to save to a .sav file

public class OfflineDataManager {

	private static String MyQuestionFilename = "";

	public static void LoadMyQuestions(Context context,
			QuestionArrayList questions) throws ClassNotFoundException {

		QuestionArrayList MyQuestions = new QuestionArrayList();

		try {
			FileInputStream fos = context.openFileInput(MyQuestionFilename);
			ObjectInputStream ois = new ObjectInputStream(fos);
			MyQuestions = (QuestionArrayList) ois.readObject();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int instanceinarraysize = MyQuestions.getSize();
		questions.clear();
		int dummy;

		for (dummy = 0; dummy < instanceinarraysize; dummy++) {
			questions.addQuestion(MyQuestions.get(dummy));
		}
	}

	public static void SaveMyQuestions(Context context,
			QuestionArrayList questions) {
		try {
			FileOutputStream fos = context.openFileOutput(MyQuestionFilename,
					context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(questions);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
