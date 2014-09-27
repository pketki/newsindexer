/**
 * 
 */
package edu.buffalo.cse.irf14;

import java.io.File;

import edu.buffalo.cse.irf14.document.Document;
import edu.buffalo.cse.irf14.document.Parser;
import edu.buffalo.cse.irf14.document.ParserException;
import edu.buffalo.cse.irf14.index.IndexReader;
import edu.buffalo.cse.irf14.index.IndexType;
import edu.buffalo.cse.irf14.index.IndexWriter;

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
		// String ipDir = "C:/git/newsindexer/training";
		// args[0];
		String ipDir = "C:/KettWorkspace/inputfile";
		String indexDir = "C:\\KettWorkspace\\serializedfiles";// args[1];
		// String indexDir = "D:\\test";// args[1];
		// more? idk!

		File ipDirectory = new File(ipDir);
		String[] catDirectories = ipDirectory.list();

		String[] files;
		File dir;

		Document d = null;
		IndexWriter writer = new IndexWriter(indexDir);

		try {
			for (String cat : catDirectories) {
				dir = new File(ipDir + File.separator + cat);
				files = dir.list();

				if (files == null)
					continue;

				for (String f : files) {
					try {
						d = Parser.parse(dir.getAbsolutePath() + File.separator
								+ f);
						System.out.println(d);

						writer.addDocument(d);
					} catch (ParserException e) {
						e.printStackTrace();
					}

				}

			}

			writer.close();

			IndexReader reader = new IndexReader(indexDir, IndexType.TERM);
			System.out.println(reader.getPostings("bid"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
