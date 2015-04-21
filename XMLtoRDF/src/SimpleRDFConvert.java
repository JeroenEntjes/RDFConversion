import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SimpleRDFConvert {

	String[] stringArray = new String[25];
	int arrayLength = 0;

	String[] rdfArray = new String[25];
	int rdfLength = 0;

	String prefix;
	String label;

	String tab = "\t";
	int tabLength = 17;
	String rdfWrite;
	String tempWrite;

	void init(String prefix, String label, String tempWrite, String rdfWrite) {
		this.prefix = prefix;
		this.label = label;
		this.tempWrite = tempWrite;
		this.rdfWrite = rdfWrite;

		try {
			FileWriter fw = new FileWriter((rdfWrite), true);
		} catch (IOException e) {

		}
		read();
	}

	void insert(String s) {

		stringArray[arrayLength] = s;
		arrayLength++;

	}

	void process(BufferedReader br) {

		String s;
		try {

			if ((s = br.readLine().trim()).equals("")) {
				return;
			}

			insert(s);
			process(br);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void process(BufferedReader br, String s) {
		// inserts group into array

		if (((s.trim()).equals(""))) {
			return;
		}
		insert(s);
		process(br);
		convert();

	}

	void insertRDF(String s, String t) {

		rdfArray[rdfLength] = s + t;
		rdfLength++;

	}

	String removeSpaces(String s) {

		String t = "";

		for (int i = 0; i < s.length(); i++) {

			if (s.charAt(i) == ' ') {
				t = t + '_';
			} else {
				t = t + s.charAt(i);
			}

		}

		return t;
	}

	String relation(String s) {

		Scanner sScanner = new Scanner(s);

		String a = sScanner.next().trim();
		String b = removeSpaces(sScanner.nextLine().trim());

		sScanner.close();

		String c;
		String d;

		c = (tab + prefix + ":has" + a);
		if (c.length() < tabLength) {
			c = c + tab;
		}
		d = (tab + prefix + ":" + b);

		return (c + d);
	}

	void convert() {
		// converts to simple RDF

		String s = (prefix + ":" + stringArray[0]);
		String a = (tab + "a " + s + " ;");

		insertRDF(s, "");
		insertRDF(a, "");

		for (int i = 1; i < (arrayLength - 1); i++) {
			insertRDF(relation(stringArray[i]), " ;");
		}
		insertRDF(relation(stringArray[arrayLength-1]), " .");

	}

	void write() {

		try (FileWriter fw = new FileWriter((rdfWrite), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			for (int i = 0; i < rdfLength; i++) {
				out.println(rdfArray[i]);
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

		for (int j = 0; j < rdfLength; j++) {
			rdfArray[j] = "";
		}

		rdfLength = 0;

	}

	void read() {

		String s;
		try (BufferedReader br = new BufferedReader(new FileReader(tempWrite))) {

			while ((s = br.readLine()) != null) {

				if (!(s.trim().equals(""))) {
					process(br, s.trim());
					write();
					clearArray();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Array files read and converted.\nPlease wait...");

	}
}
