/**
 * 
 */
package edu.buffalo.cse.irf14.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

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
		if (filename == null || filename.isEmpty()
				|| !filename.matches("[\\\\_:.A-Za-z0-9\\s-]+$"))
			throw new ParserException();

		final Document document = new Document();

		String[] path = filename.split("\\\\");
		document.setField(FieldNames.FILEID, path[path.length - 1]);
		document.setField(FieldNames.CATEGORY, path[path.length - 2]);

		Pattern month = Pattern
				.compile("\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\D*");
		// String titleRegEx = "^[A-Z0-9<>()/.,'\\-\\s]*$";
		String line, authorName, authorOrg = null;
		StringBuffer place = new StringBuffer("");
		StringBuffer content = new StringBuffer("");

		try {
			Scanner reader = new Scanner(new File(filename));
			while (reader.hasNextLine()) {

				line = reader.nextLine();

				if (line.isEmpty())
					continue;

				if (document.getField(FieldNames.TITLE) == null) {
					document.setField(FieldNames.TITLE, line);

					// check for author info
					if (reader.findWithinHorizon("<AUTHOR>", 200) != null) {
						reader.findInLine("by|By|BY");
						String[] authorInfo = reader.nextLine().split(
								",|</AUTHOR>");
						authorName = authorInfo[0].trim();
						if (authorInfo.length > 1) {
							authorOrg = authorInfo[1].trim();
						}
						document.setField(FieldNames.AUTHOR, authorName);
						document.setField(FieldNames.AUTHORORG, authorOrg);
					}

					// capture place till date token arrives
					while (!reader.hasNext(month) && reader.hasNext()) {
						place.append(reader.next() + " ");
					}
					String placeString = place.toString().trim();

					// if a file is unformatted and has no date put everything
					// in content
					if (!reader.hasNext()) {
						document.setField(FieldNames.CONTENT, placeString);
						break;
					}

					// remove ending commas
					if (!placeString.isEmpty()
							&& placeString.lastIndexOf(",") == placeString
									.length() - 1) {
						placeString = placeString.substring(0,
								placeString.length() - 1);
					}
					document.setField(FieldNames.PLACE, placeString);

					String dt = reader.next() + " " + reader.next();
					document.setField(FieldNames.NEWSDATE, dt);

				} else {
					content.append(line + " ");
				}
				document.setField(FieldNames.CONTENT, content.toString().trim());
			}
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return document;
	}

}
