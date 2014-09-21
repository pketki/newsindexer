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
	private boolean isFound;

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
				isFound = false;
				// Pattern pattern = Pattern
				// .compile("[(\\d)(\\s)]*\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\D*[(\\s)(\\d),]*(\\d{2,4})*");
				// Matcher match = pattern.matcher(token.getTermText());
				// if (match.find()) {
				//
				// }
				if (token
						.getTermText()
						.matches(
								"\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\D*")) {
					// current token has month name
					// check previous token for day, if found then check next
					// token
					// for year. Combine 3 token in one, pass it to simpleformat
					// and
					// get datestring
					//
					// if not found check next of month token for day. Check
					// year
					// token and repeat same steps as above
				}
				if (token.getTermText().matches("\\d{4}")) {
					calendar.set(Calendar.ERA, GregorianCalendar.AD);
					calendar.set(Integer.parseInt(token.getTermText()), 1, 1,
							0, 0, 0);
				}
				if (token.getTermText().matches("\\d{1,4}(\\s)*(BC)")) {
					String year = token.getTermText().split("BC")[0].toString()
							.trim();
					calendar.set(Calendar.ERA, GregorianCalendar.BC);
					calendar.set(Integer.parseInt(year), 1, 1, 0, 0, 0);
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyyMMdd");
				String dateString = simpleDateFormat.format(calendar.getTime());
				token.setTermText(dateString);

				String timeFormat = null;
				if (token.getTermText().matches("[0-9]+(am|AM|pm|PM){1}?")) {
					isFound = true;
					timeFormat = "hha";
				} else if (!isFound
						&& token.getTermText().matches(
								"[0-9:]+(am|AM|pm|PM){1}?")) {
					isFound = true;
					timeFormat = "hh:mma";
				} else if (!isFound
						&& token.getTermText().matches("(am|AM|pm|PM){1}?")) {
					/**
					 * replace token.getTermText() with previous token value
					 */
					if (!isFound
							&& token.getTermText().matches("[0-9:]+(\\s)*")) {
						isFound = true;
						timeFormat = "hh:mm a";
					} else if (!isFound
							&& token.getTermText().matches("[0-9]+(\\s)*")) {
						isFound = true;
						timeFormat = "hh a";
					}
				}

				Date date = null;
				try {
					date = new SimpleDateFormat(timeFormat).parse(token
							.getTermText());
				} catch (ParseException e) {
					throw new TokenizerException();
				}
				DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
				token.setTermText(sdf.format(date));
				return true;
			}
		}
		return false;
	}

}
