import java.io.*;

public class Read {

	int start = 1;
	String[] stringArray = new String[25];
	int arrayLength = 0;
	String tableString;
	String input;
	String tableName;
	
	String fileName;
	String tempWrite;

	void findStart(String startString, String input, String fileName, String tempWrite) {

		// Find start of db in XML code and remember line # as in XML file, set
		// fileName for writing

		this.input = input;
		this.fileName = fileName;
		this.tempWrite = tempWrite;

		String s;
		try (BufferedReader br = new BufferedReader(new FileReader(
				fileName))) {

			while ((s = br.readLine()) != null) {
				s = s.trim();
				if (s.length() > startString.length()) {
					if (s.substring(0, startString.length())
							.equals(startString)) {

						start++;
						break;

					}
				} else {
					start++;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String get(int i) {

		return stringArray[i];

	}

	void databaseProcessing(BufferedReader br) {

		// Start making RDF objects of tables until end of db file
		String s;
		try {
			
			RDFConverter RDF = new RDFConverter();
			RDF.init(input, tempWrite);
			
			while (!((s = br.readLine().trim()).equals("</database>"))) {

				if (compare(s)) {

					RDF.setTableName(s);
					RDF.tableProcessing(br);

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void findTable(String tableString) {

		// Starts scanning text file, set parameters
		this.tableString = tableString;
		try (BufferedReader br = new BufferedReader(new FileReader(
				fileName))) {

			databaseProcessing(br);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	boolean compare(String s) {
		if (s.length() > tableString.length()) {
			if (s.substring(0, tableString.length()).equals(tableString)) {
				return true;
			}
		}
		return false;
	}
}