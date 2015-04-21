import java.io.*;
import java.util.Scanner;

public class Run {

	String startString = "<database name=";
	String tableString = "<table name=";

	String rdfs = "@prefix rdfs:	<http://www.w3.org/2000/01/rdf-schema#> .";
	String elb = "@prefix elb:	<http://purl.org/collections/nl/dss/elbing/> .";
	String elbs = "@prefix elbs:	<http://purl.org/collections/nl/dss/elbing_schema/> .";
	String arch = "@prefix arch:	<http://purl.org/collections/nl/dss/archangel/> .";

	String prefix = "elb";
	String label = "rdfs";

	int arrayLength = 0;

	String input;

	String fileName = "elbing.xml";
	String tempWrite = "temp.txt";
	String rdfWrite = "simpleRDF.ttl";

	Read read = new Read();

	void start() {
		// readfile

		// step one: convert XML to text
		getFileName();
		findStart();

		// step two: convert text to RDF
		simpleConvert();
		//insertRDF();
		
		System.out.println("Conversion complete!");
	}

	void simpleConvert() {

		SimpleRDFConvert simRDF = new SimpleRDFConvert();
		simRDF.init(prefix, label, tempWrite, input);
	}

	void getFileName() {

		System.out
				.println("Give a filename to save to, do not write any extensions:");
		Scanner scan = new Scanner(System.in);
		input = scan.next() + ".ttl";
		scan.close();
		
		System.out.println("Working...");

		printPrefix();

	}

	void printPrefix() {

		try (FileWriter fw = new FileWriter((input));
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {

			out.println(rdfs);
			out.println(elbs);
			out.println(elb);
			out.println(arch);
			out.println("\n\n");

		} catch (IOException e) {
			// File writing/opening failed at some stage.
		}
	}

	// scan document line by line for start
	void findStart() {
		// code to find line <database name=
		read.findStart(startString, input, fileName, tempWrite);

		// find out which table is up
		findTable();
	}

	// scan lines for next new table
	void findTable() {
		// code to find <table name=
		read.findTable(tableString);

		processTable(read);
	}

	// scan table properties and convert to RDF
	void processTable(Read read) {
		// code to create concept RDFnode with table names

		for (int i = 0; i < read.arrayLength; i++) {
			// System.out.println(read.get(i));
		}
		System.out.println("Array has been processed.\nPlease wait...");
	}

	public static void main(String[] args) {
		new Run().start();
	}
}
