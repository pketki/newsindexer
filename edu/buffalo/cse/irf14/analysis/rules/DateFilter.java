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
		isFound = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.buffalo.cse.irf14.analysis.Analyzer#increment()
	 */
	@Override
	public boolean increment() throws TokenizerException {
		if (getStream().hasNext()) {
			Token token = getStream().next();
			if (token != null) {
				isDate = false;
				isTime = false;
				isFound = false;
				String dateString = null;
				Token tempToken = null;
				if (token.getTermText().trim()
						.matches("\\d{1,2}(st|th|nd|rd)?")) {
					isDate = true;
					dateString = token.getTermText().trim()
							.replaceAll("(st|th|nd|rd)?", "");

					if (getStream().hasNext()) {
						tempToken = getStream().next();
					}
					if (tempToken != null
							&& tempToken.getTermText().matches("(BC){1}")) {
						isFound = true;
						calendar.set(Calendar.ERA, GregorianCalendar.BC);
						calendar.set(Integer.parseInt(token.getTermText()), 0,
								1, 0, 0, 0);
					}

					if (!isFound
							&& tempToken != null
							&& tempToken
									.getTermText()
									.trim()
									.matches(
											"\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\D*")) {
						dateString = tempToken.getTermText().trim()
								.replaceAll(",", "")
								+ " " + dateString;
					} else if (tempToken != null
							&& tempToken.getTermText().matches(
									"(am|AM|pm|PM){1}?")) {
						isDate = false;
						isFound = false;
					} else
						dateString = "January " + dateString;
				}

				if (!isFound
						&& token.getTermText()
								.matches(
										"\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\D*")) {

					dateString = token.getTermText().trim().replaceAll(",", "");
					isFound = true;
					isDate = true;
					if (getStream().hasNext()) {
						tempToken = getStream().next();
					}

					if (tempToken != null
							&& tempToken.getTermText().trim()
									.matches("\\d{1,2}")) {
						dateString += " " + tempToken.getTermText().trim();
					} else
						dateString += " 01";
				}

				if (isFound && isDate) {
					if (tempToken != null
							&& !tempToken.getTermText().trim()
									.matches("\\d{4}")) {
						if (getStream().hasNext()) {
							tempToken = getStream().next();
						}
					}

					if (tempToken != null
							&& tempToken.getTermText().trim().matches("\\d{4}"))
						dateString += " " + tempToken.getTermText().trim();
					else
						dateString += " 1900";

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
					calendar.set(Integer.parseInt(token.getTermText()), 0, 1,
							0, 0, 0);
				}
				// if (!isFound
				// && token.getTermText().trim()
				// .matches("\\d{1,4}(\\s)*(BC)")) {
				// isFound = true;
				// isDate = true;
				// String year = token.getTermText().split("BC")[0].toString()
				// .trim();
				// calendar.set(Calendar.ERA, GregorianCalendar.BC);
				// calendar.set(Integer.parseInt(year), 0, 1, 0, 0, 0);
				// }
				if (isFound && isDate) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyyMMdd");
					dateString = simpleDateFormat.format(calendar.getTime());
					token.setTermText(dateString);
				}
				if (!isFound
						&& token.getTermText().trim()
								.matches("\\d{4}[(-\\/)+]\\d{2}")) {
					isFound = true;
					isDate = true;
					String delim = "-";
					String[] yearSplit = token.getTermText().split("-");
					if (yearSplit.length < 2) {
						yearSplit = token.getTermText().split("/");
						delim = "/";
					}
					calendar.set(Calendar.ERA, GregorianCalendar.AD);
					calendar.set(Integer.parseInt(yearSplit[0]), 0, 1, 0, 0, 0);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyyMMdd");
					dateString = simpleDateFormat.format(calendar.getTime())
							+ delim;
					calendar.set(Integer.parseInt(yearSplit[1]), 0, 1, 0, 0, 0);

					token.setTermText(dateString
							+ simpleDateFormat.format(calendar.getTime()));
				}

				String timeFormat = null;
				String timeString = null;

				if (!isFound
						&& token.getTermText().matches(
								"[0-9]+(am|AM|pm|PM){1}?")) {
					isFound = true;
					isTime = true;
					timeFormat = "hha";
					timeString = token.getTermText();
				}
				if (!isFound
						&& token.getTermText().matches(
								"[0-9:]+(am|AM|pm|PM){1}?")) {
					isFound = true;
					isTime = true;
					timeFormat = "hh:mma";
					timeString = token.getTermText();
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
					if (getStream().hasNext()) {
						tempToken = getStream().next();
					}
					if (tempToken != null
							&& tempToken.getTermText().matches(
									"(am|AM|pm|PM){1}?")) {
						timeString = token.getTermText() + " "
								+ tempToken.getTermText();
					} else {
						isFound = false;
					}
				}

				if (isFound && isTime && timeFormat != null
						&& timeString != null) {
					Date date = null;
					try {
						date = new SimpleDateFormat(timeFormat)
								.parse(timeString);
					} catch (ParseException e) {
						throw new TokenizerException();
					}
					DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
					token.setTermText(sdf.format(date));
				}
				return true;
			}
		}
		return false;
	}
}
