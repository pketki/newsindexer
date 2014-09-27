/**
 * 
 */
package edu.buffalo.cse.irf14.document;

/**
 * @author nikhillo Wrapper class that holds {@link FieldNames} to value mapping
 */
public class Document {

	private final Fields fields;

	/**
	 * Default constructor
	 */
	public Document() {
		fields = new Fields();
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
		fields.setFieldFromEnum(fn, o);
	}

	/**
	 * Method to get the field value for a given {@link FieldNames} field
	 * 
	 * @param fn
	 *            : The field name to query
	 * @return The associated value, null if not found
	 */
	public String[] getField(FieldNames fn) {
		return fields.getFieldFromEnum(fn);
	}

	/**
	 * Method to see what's in the document. Used mostly for internal testing
	 */
	@Override
	public String toString() {
		StringBuffer retVal = new StringBuffer("Document [fileId: "
				+ getField(FieldNames.FILEID)[0]);
		retVal.append(" category: ");
		retVal.append(getField(FieldNames.CATEGORY)[0]);

		if (getField(FieldNames.AUTHOR) != null) {
			retVal.append(" author: ");
			retVal.append(getField(FieldNames.AUTHOR)[0]);
		}
		if (getField(FieldNames.AUTHORORG) != null) {
			retVal.append(" author org: ");
			retVal.append(getField(FieldNames.AUTHORORG)[0]);
		}
		if (getField(FieldNames.TITLE) != null) {
			retVal.append(" title: ");
			retVal.append(getField(FieldNames.TITLE)[0]);
		}
		if (getField(FieldNames.PLACE) != null) {
			retVal.append(" place: ");
			retVal.append(getField(FieldNames.PLACE)[0]);
		}
		if (getField(FieldNames.NEWSDATE) != null) {
			retVal.append(" date: ");
			retVal.append(getField(FieldNames.NEWSDATE)[0]);
		}
		if (getField(FieldNames.CONTENT) != null) {
			retVal.append(" content: ");
			retVal.append(getField(FieldNames.CONTENT)[0]);
		}
		retVal.append("]");
		return retVal.toString();
	}
}
