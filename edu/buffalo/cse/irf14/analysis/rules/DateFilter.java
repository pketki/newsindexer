/**
 * 
 */
package edu.buffalo.cse.irf14.analysis.rules;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
		if (getStream().hasNext()) {
			Token token = getStream().next();
			if (token != null) {
				// Pattern pattern = Pattern
				// .compile("[(\\d)(\\s)]*\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\D*[(\\s)(\\d),]*(\\d{2,4})*");
				// Matcher match = pattern.matcher(token.getTermText());
				// if (match.find()) {
				//
				// }
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
				return true;
			}
		}
		return false;
	}

}
