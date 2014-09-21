/**
 * 
 */
package edu.buffalo.cse.irf14;

import java.io.File;

import edu.buffalo.cse.irf14.analysis.Analyzer;
import edu.buffalo.cse.irf14.analysis.AnalyzerFactory;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.Tokenizer;
import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.FieldNames;
import edu.buffalo.cse.irf14.document.Parser;
import edu.buffalo.cse.irf14.document.ParserException;

/**
 * @author nikhillo
 * 
 */
public class Runner {

	/**
	 * 
	 */
	public Runner() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// String ipDir = "D:/MS at UB/Semester 1/IR/news_training/training";//
		// args[0];
		String ipDir = "C:/KettWorkspace/inputfile";
		// String indexDir = args[1];
		// more? idk!

		File ipDirectory = new File(ipDir);
		String[] catDirectories = ipDirectory.list();
		System.out.println(catDirectories[0]);

		String[] files;
		File dir;

		Document d = null;
		// IndexWriter writer = new IndexWriter(indexDir);

		try {
			for (String cat : catDirectories) {
				dir = new File(ipDir + File.separator + cat);
				System.out.println(dir);
				files = dir.list();

				if (files == null)
					continue;

				for (String f : files) {
					try {
						d = Parser.parse(dir.getAbsolutePath() + File.separator
								+ f);
						System.out.println(d);

						Tokenizer tokenizer = new Tokenizer();
						if (d.getField(FieldNames.AUTHOR) != null) {
							TokenStream ts = tokenizer.consume(d
									.getField(FieldNames.AUTHOR)[0]);
							System.out.println(ts);
							AnalyzerFactory af = AnalyzerFactory.getInstance();
							Analyzer an = af.getAnalyzerForField(
									FieldNames.AUTHOR, ts);
							while (an.increment())
								;

							System.out.println(ts);
						}
						if (d.getField(FieldNames.TITLE) != null) {
							TokenStream ts = tokenizer.consume(d
									.getField(FieldNames.TITLE)[0]);
							System.out.println(ts);
							AnalyzerFactory af = AnalyzerFactory.getInstance();
							Analyzer an = af.getAnalyzerForField(
									FieldNames.TITLE, ts);
							while (an.increment())
								;

							System.out.println(ts);
						}
						if (d.getField(FieldNames.PLACE) != null) {
							TokenStream ts = tokenizer.consume(d
									.getField(FieldNames.PLACE)[0]);
							System.out.println(ts);
							AnalyzerFactory af = AnalyzerFactory.getInstance();
							Analyzer an = af.getAnalyzerForField(
									FieldNames.PLACE, ts);
							while (an.increment())
								;

							System.out.println(ts);
						}
						if (d.getField(FieldNames.CONTENT) != null) {
							TokenStream ts = tokenizer.consume(d
									.getField(FieldNames.CONTENT)[0]);
							System.out.println(ts);
							AnalyzerFactory af = AnalyzerFactory.getInstance();
							Analyzer an = af.getAnalyzerForField(
									FieldNames.CONTENT, ts);
							while (an.increment())
								;

							System.out.println(ts);
						}

						// writer.addDocument(d);
					} catch (ParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

			// writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
