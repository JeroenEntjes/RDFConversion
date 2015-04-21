import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RDFConverter {

	String[] stringArray = new String[25];
	int arrayLength = 0;
	boolean nameSet = false;

	String input;
	String tempWrite;

	void insert(String s) {

		stringArray[arrayLength] = s;
		arrayLength++;

	}

	void init(String input, String tempWrite) {

		this.tempWrite = tempWrite;
		
		try {
			FileWriter fw = new FileWriter((tempWrite));

		} catch (IOException e) {

		}
		this.input = input;

	}

	String processedInsert(String s, int i, int j) {
		// adds column String information if it is not a column name
		String t = s.substring(i, j);

		if (nameSet == false) {
			return t;
		} else {

			t = t + " ";

			for (int k = j + 2; k < s.length(); k++) {
				char c = s.charAt(k);
				if (c == '<') {
					break;
				} else {
					t = t + c;
				}
			}

			return t;
		}
	}

	void isolateString(String s) {

		// insert column name
		StringBuffer sb = new StringBuffer(s);

		int firstNameChar = 0;
		int finalNameChar = 0;

		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (c == '\"') {
				firstNameChar = i + 1;

				for (int j = i + 1; j < sb.length(); j++) {

					char d = sb.charAt(j);
					if (d == '\"') {
						finalNameChar = j;

						break;
					}
				}
				break;
			}
		}

		insert(processedInsert(s, firstNameChar, finalNameChar));

	}

	void setTableName(String s) {
		// Sets name which acts as RFIDlabel

		isolateString(s);
		nameSet = true;

	}

	void appendArray() {

		// append string to file
		try (FileWriter fw = new FileWriter((tempWrite), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			for (int i = 0; i < arrayLength; i++) {
				out.println(stringArray[i]);
			}
			out.println("\n");
		} catch (IOException e) {
			// File writing/opening failed at some stage.
		}
	}

	void clearArray() {

		for (int i = 0; i < arrayLength; i++) {
			stringArray[i] = "";
		}

		arrayLength = 0;
		nameSet = false;

	}

	void tableProcessing(BufferedReader br) {

		// recursive method to add lines of one table to an array in the method
		// tableWorker(s), return when table ends
		String s;
		try {
			if ((s = br.readLine().trim()).equals("</table>")) {

				appendArray();
				clearArray();

			} else {
				isolateString(s);
				tableProcessing(br);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
