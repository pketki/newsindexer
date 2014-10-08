/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.buffalo.cse.irf14.IndexHelper;
import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

/**
 * @author jlimaye
 * 
 */
public class DateFilter extends TokenFilter {
	private final Calendar calendar;
	private boolean isFound, isDate, isTime;

	/**
	 * @param stream
	 */
	public DateFilter(TokenStream stream) {
		super(stream);
		calendar = new GregorianCalendar();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.irf14.analysis.Analyzer#increment()
	 */
	@Override
	public boolean increment() throws TokenizerException {
		// if (getStream().hasNext()) {
		Token token = null;

		if (this.isChaining())
			token = getStream().getCurrent();
		if (token == null && getStream().hasNext()) {
			token = getStream().next();
		}
		// Token token = getStream().next();
		if (token != null) {
			// System.out.println(token.getTermText());
			isDate = false;
			isTime = false;
			isFound = false;
			String addChars = "";
			String dateString = null;
			Token tempToken = null;
			if (token.getTermText().trim().matches("\\d+(AD){1}[\\/.,\\-]?")) {
				isFound = true;
				String[] yearSplit = token.getTermText().split("AD");
				if (yearSplit.length > 1) {
					addChars = yearSplit[1];
				}
				calendar.set(Calendar.ERA, GregorianCalendar.AD);
				calendar.set(
						Integer.parseInt(token.getTermText().replaceAll(
								"(AD){1}[\\/.,\\-]?", "")), 0, 1, 0, 0, 0);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyyMMdd");
				dateString = simpleDateFormat.format(calendar.getTime());
				token.setTermText(dateString + addChars);
				return true;
			}
			if (token.getTermText().trim().matches("\\d+(BC){1}")) {
				isFound = true;
				calendar.set(Calendar.ERA, GregorianCalendar.BC);
				calendar.set(
						Integer.parseInt(token.getTermText().replace("BC", "")),
						0, 1, 0, 0, 0);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyyMMdd");
				dateString = simpleDateFormat.format(calendar.getTime());
				token.setTermText("-" + dateString);
				return true;
			}
			if (token.getTermText().trim().matches("\\d{1,2}(st|th|nd|rd)?")) {
				isDate = true;
				dateString = token.getTermText().trim()
						.replaceAll("(st|th|nd|rd)?", "");
				getStream().savePoint();
				tempToken = getStream().next();

				if (tempToken != null
						&& tempToken.getTermText().matches("(BC){1}")) {
					isFound = true;
					calendar.set(Calendar.ERA, GregorianCalendar.BC);
					calendar.set(Integer.parseInt(token.getTermText()), 0, 1,
							0, 0, 0);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyyMMdd");
					dateString = simpleDateFormat.format(calendar.getTime());
					getStream().remove();
					token.setTermText("-" + dateString);
					return true;
				}

				if (!isFound
						&& tempToken != null
						&& tempToken.getTermText().trim()
								.matches(IndexHelper.monthForFilter.pattern())) {
					isFound = true;
					dateString = tempToken.getTermText().trim()
							.replaceAll(",", "")
							+ " " + dateString;
					getStream().remove();
				} else if (tempToken != null
						&& tempToken.getTermText().matches("(am|AM|pm|PM){1}?")) {
					isDate = false;
				} else
					getStream().rollBack();
			}

			if (!isFound
					&& token.getTermText().matches(
							IndexHelper.monthForFilter.pattern())) {

				dateString = token.getTermText().trim().replaceAll(",", "");
				isFound = true;
				isDate = true;
				getStream().savePoint();
				tempToken = getStream().next();

				if (tempToken != null
						&& tempToken.getTermText().trim()
								.matches("\\d{1,2}(st|th|nd|rd|,)?")) {
					dateString += " "
							+ tempToken.getTermText().trim()
									.replaceAll("(st|th|nd|rd|,)?", "");
					getStream().remove();
				} else
					dateString += " 01";
			}

			if (isFound && isDate) {
				if (tempToken != null
						&& !tempToken.getTermText().trim().matches("\\d{4}")) {
					getStream().savePoint();
					tempToken = getStream().next();

				}

				if (tempToken != null
						&& tempToken.getTermText().trim()
								.matches("\\d{4}[\\/.,\\-]?")) {
					dateString += " " + tempToken.getTermText().substring(0, 4);
					addChars = tempToken.getTermText().substring(4);
					getStream().remove();
				} else {
					dateString += " 1900";
					getStream().rollBack();
				}

				calendar.set(Calendar.ERA, GregorianCalendar.AD);
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"MMMM dd yyyy");
				dateFormat.setCalendar(calendar);
				Date date = null;
				try {
					date = dateFormat.parse(dateString);
				} catch (ParseException e) {
					// TODO to be handled
					e.printStackTrace();
				}
				calendar.setTime(date);
			}

			if (!isFound && token.getTermText().trim().matches("\\d{4}")) {
				isFound = true;
				isDate = true;
				calendar.set(Calendar.ERA, GregorianCalendar.AD);
				calendar.set(Integer.parseInt(token.getTermText()), 0, 1, 0, 0,
						0);
			}

			if (isFound && isDate) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyyMMdd");
				dateString = simpleDateFormat.format(calendar.getTime());
				token.setTermText(dateString + addChars);
			}
			if (!isFound
					&& token.getTermText().trim()
							.matches("\\d{4}(-\\/){1}\\d{2}[\\/.,\\-]?")) {
				isFound = true;
				isDate = true;
				String delim = "-";
				String[] yearSplit = token.getTermText().split("-");
				if (yearSplit.length < 2) {
					yearSplit = token.getTermText().split("/");
					delim = "/";
				}
				String yearStr = yearSplit[1].substring(0, 2);
				addChars = yearSplit[1].substring(2);
				yearSplit[1] = yearSplit[0].substring(0, 2) + yearStr;
				calendar.set(Calendar.ERA, GregorianCalendar.AD);
				calendar.set(Integer.parseInt(yearSplit[0]), 0, 1, 0, 0, 0);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyyMMdd");
				dateString = simpleDateFormat.format(calendar.getTime())
						+ delim;
				calendar.set(Integer.parseInt(yearSplit[1]), 0, 1, 0, 0, 0);

				token.setTermText(dateString
						+ simpleDateFormat.format(calendar.getTime())
						+ addChars);
			}

			String timeFormat = null;
			String timeString = null;

			if (!isFound
					&& token.getTermText().matches(
							"[0-9]+(am|AM|pm|PM){1}[\\/.,\\-]?")) {
				isFound = true;
				isTime = true;
				timeFormat = "hha";
				timeString = token.getTermText();
			}
			if (!isFound
					&& token.getTermText().matches(
							"[0-9:]+(am|AM|pm|PM){1}[\\/.,\\-]?")) {
				isFound = true;
				isTime = true;
				timeFormat = "hh:mma";
				String[] timeSplit = token.getTermText().split(":");
				addChars = timeSplit[1].substring(4);
				timeString = timeSplit[0] + ":" + timeSplit[1].substring(0, 4);
			}

			if (!isFound && token.getTermText().matches("\\d+(:){1}\\d+")) {
				isFound = true;
				isTime = true;
				timeFormat = "hh:mm a";
			}
			if (!isFound && token.getTermText().matches("[0-9]+")) {
				isFound = true;
				isTime = true;
				timeFormat = "hh a";
			}
			if (isTime && timeString == null) {
				if (tempToken == null) {
					getStream().savePoint();
					tempToken = getStream().next();
				}

				if (tempToken != null
						&& tempToken.getTermText().matches(
								"(am|AM|pm|PM){1}?[\\/.,\\-]?")) {
					timeString = token.getTermText() + " "
							+ tempToken.getTermText().substring(0, 2);
					addChars = tempToken.getTermText().substring(2);
					getStream().remove();
				} else {
					isFound = false;
					getStream().rollBack();
				}
			}

			if (isFound && isTime && timeFormat != null && timeString != null) {
				Date date = null;
				try {
					date = new SimpleDateFormat(timeFormat).parse(timeString);
				} catch (ParseException e) {
					throw new TokenizerException();
				}
				DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				token.setTermText(sdf.format(date) + addChars);
			}
			return true;
		}
		// }
		return false;
	}
}
