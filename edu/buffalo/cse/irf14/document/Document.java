/**
 * 
 */
package edu.buffalo.cse.irf14.document;

import java.util.HashMap;

/**
 * @author nikhillo Wrapper class that holds {@link FieldNames} to value mapping
 */
public class Document {
	// Sample implementation - you can change this if you like
	private final HashMap<FieldNames, String[]> map;

	/**
	 * Default constructor
	 */
	public Document() {
		map = new HashMap<FieldNames, String[]>();
	}

	/**
	 * Method to set the field value for the given {@link FieldNames} field
	 * 
	 * @param fn
	 *            : The {@link FieldNames} to be set
	 * @param o
	 *            : The value to be set to
	 */
	public void setField(FieldNames fn, String... o) {
		map.put(fn, o);
	}

	/**
	 * Method to get the field value for a given {@link FieldNames} field
	 * 
	 * @param fn
	 *            : The field name to query
	 * @return The associated value, null if not found
	 */
	public String[] getField(FieldNames fn) {
		return map.get(fn);
	}

	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer("Document [fileId: "
				+ map.get(FieldNames.FILEID)[0]);
		retVal.append(" category: ");
		retVal.append(map.get(FieldNames.CATEGORY)[0]);

		if (map.get(FieldNames.AUTHOR) != null) {
			retVal.append(" author: ");
			retVal.append(map.get(FieldNames.AUTHOR)[0]);
		}
		if (map.get(FieldNames.AUTHORORG) != null) {
			retVal.append(" author org: ");
			retVal.append(map.get(FieldNames.AUTHORORG)[0]);
		}
		if (map.get(FieldNames.TITLE) != null) {
			retVal.append(" title: ");
			retVal.append(map.get(FieldNames.TITLE)[0]);
		}
		if (map.get(FieldNames.PLACE) != null) {
			retVal.append(" place: ");
			retVal.append(map.get(FieldNames.PLACE)[0]);
		}
		if (map.get(FieldNames.NEWSDATE) != null) {
			retVal.append(" date: ");
			retVal.append(map.get(FieldNames.NEWSDATE)[0]);
		}
		if (map.get(FieldNames.CONTENT) != null) {
			retVal.append(" content: ");
			retVal.append(map.get(FieldNames.CONTENT)[0]);
		}
		retVal.append("]");
		return retVal.toString();
	}
}
