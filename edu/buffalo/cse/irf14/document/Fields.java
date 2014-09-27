/**
 * 
 */
package edu.buffalo.cse.irf14.document;

/**
 * @author ketkiram
 * 
 */
/**
 * @author Dell
 * 
 */
public class Fields {
	private String[] fileId;
	private String[] category;
	private String[] author;
	private String[] authorOrg;
	private String[] title;
	private String[] place;
	private String[] newsDate;
	private String[] content;

	/**
	 * Default constructor
	 */
	public Fields() {
		fileId = null;
		category = null;
		author = null;
		authorOrg = null;
		title = null;
		place = null;
		newsDate = null;
		content = null;
	}

	/**
	 * Method to get field value based on FieldName type
	 * 
	 * @param fn
	 * @return String array value of that field
	 */
	public String[] getFieldFromEnum(FieldNames fn) {
		switch (fn) {
		case FILEID:
			return fileId;
		case AUTHOR:
			return author;
		case AUTHORORG:
			return authorOrg;
		case CATEGORY:
			return category;
		case CONTENT:
			return content;
		case NEWSDATE:
			return newsDate;
		case PLACE:
			return place;
		case TITLE:
			return title;
		default:
			return null;
		}
	}

	/**
	 * Method to set field of type FieldName with given value
	 * 
	 * @param fn
	 * @param value
	 */
	public void setFieldFromEnum(FieldNames fn, String[] value) {
		switch (fn) {
		case FILEID:
			fileId = value;
			break;
		case AUTHOR:
			author = value;
			break;
		case AUTHORORG:
			authorOrg = value;
			break;
		case CATEGORY:
			category = value;
			break;
		case CONTENT:
			content = value;
			break;
		case NEWSDATE:
			newsDate = value;
			break;
		case PLACE:
			place = value;
			break;
		case TITLE:
			title = value;
			break;
		default:
			break;
		}
	}

}
