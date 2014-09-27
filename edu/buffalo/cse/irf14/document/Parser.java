/**
 * 
 */
package edu.buffalo.cse.irf14.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.buffalo.cse.irf14.IndexHelper;

/**
 * @author nikhillo Class that parses a given file into a Document
 */
public class Parser {
	/**
	 * Static method to parse the given file into the Document object
	 * 
	 * @param filename
	 *            : The fully qualified filename to be parsed
	 * @return The parsed and fully loaded Document object
	 * @throws ParserException
	 *             In case any error occurs during parsing
	 */
	public static Document parse(String filename) throws ParserException {
		// check for filename null or empty
		if (filename == null || filename.isEmpty())
			throw new ParserException();

		// check if given file exists at all
		File file = new File(filename);
		if (!file.exists())
			throw new ParserException();

		System.out.println(file.getName());
		final Document document = new Document();

		document.setField(FieldNames.FILEID, file.getName());
		document.setField(FieldNames.CATEGORY, file.getParentFile().getName());

		String line, date, authorOrg = null;
		String[] author = new String[10];
		StringBuilder place = new StringBuilder("");
		StringBuilder content = new StringBuilder("");

		try {
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine()) {

				line = reader.nextLine();

				if (line.isEmpty())
					continue;

				// assuming first line is always title and it is a one-liner
				if (document.getField(FieldNames.TITLE) == null) {
					document.setField(FieldNames.TITLE, line);

					// check for author info
					if (reader.findWithinHorizon("<AUTHOR>", 100) != null) {
						reader.findInLine("by|By|BY");
						line = reader.nextLine();
						String[] authorInfo = line.trim().split(
								",| and|</AUTHOR>");
						// first or only entry will be considered author name
						if (authorInfo.length > 0)
							author[0] = authorInfo[0].trim();
						if (authorInfo.length > 1) {
							// for multiple authors, add to author array
							for (int i = 1; i < authorInfo.length - 1; i++)
								author[i] = authorInfo[i].trim();
							// last string is considered to be author org
							authorOrg = authorInfo[authorInfo.length - 1]
									.trim();
						}
						document.setField(FieldNames.AUTHOR, author);
						document.setField(FieldNames.AUTHORORG, authorOrg);
					}

					// capture place till date token arrives
					while (!reader.hasNext(IndexHelper.month)
							&& reader.hasNext()) {
						place.append(reader.next() + " ");
					}
					String placeString = place.toString().trim();

					// if a file is unformatted and has no date put everything
					// in content
					if (!reader.hasNext()) {
						content.append(placeString);
						break;
					}

					// remove ending commas (to fit the test cases, ideally it
					// can stay as is)
					if (!placeString.isEmpty()
							&& placeString.lastIndexOf(",") == placeString
									.length() - 1) {
						placeString = placeString.substring(0,
								placeString.length() - 1);
					}
					document.setField(FieldNames.PLACE, placeString);

					date = reader.next() + " " + reader.next();
					document.setField(FieldNames.NEWSDATE, date);

				} else {
					content.append(line + " ");
				}
			}
			document.setField(FieldNames.CONTENT, content.toString().trim());
			reader.close();

		} catch (FileNotFoundException e) {
			System.out
					.println("The file " + file.getName() + " was not found.");
			e.printStackTrace();
		}
		return document;
	}
}
