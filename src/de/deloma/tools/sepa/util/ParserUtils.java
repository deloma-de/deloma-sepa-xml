package de.deloma.tools.sepa.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.exception.PainParserException.ParserExceptionType;

/**
 * @author Azahar Hossain (c) 2022
 *
 */
public class ParserUtils {

	public static ZoneId utcZoneID = ZoneId.of("Etc/UTC");

	public static DateTimeFormatter formatterLocal = DateTimeFormatter.ISO_LOCAL_DATE;
	public static DateTimeFormatter formatterINSTANT = DateTimeFormatter.ISO_INSTANT;

	/**
	 * Converts given Date objct to XMLGregorianCalendars
	 * 
	 * @param date
	 * @return
	 */
	@Deprecated
	public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date, TimeZone timeZone) {

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.setTimeZone(timeZone != null ? timeZone : TimeZone.getTimeZone(utcZoneID));
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static XMLGregorianCalendar gregorianCalendarToXmlGregorian(GregorianCalendar cal, TimeZone timeZone) {
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param date
	 * @param timeZone
	 * @return
	 */
	public static XMLGregorianCalendar dateToXmlGregorian(Date date, TimeZone timeZone) {

		if (date == null)
			return null;
		// Moment
		Instant intant = date.toInstant();
		ZonedDateTime zonedDateTime = intant.atZone(timeZone == null ? utcZoneID : timeZone.toZoneId());
		GregorianCalendar cal = GregorianCalendar.from(zonedDateTime);
		// TODO Is it obsolete to set timeZone ??
		// cal.setTimeZone(zonedDateTime.tim);

		return getXMLCalender(cal);

	}

	/**
	 * From a given Date Object return a XMLGregorianCalendar of Date without
	 * timezone and offset in format "YYYY-MM-DD"
	 * 
	 * @param date
	 * @return {@link XMLGregorianCalendar}
	 */
	public static XMLGregorianCalendar dateToXmlGregorianNoOffset(Date date) {
		if (date == null)
			return null;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar xmlCal = getXMLCalender(cal);
		xmlCal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		return xmlCal;

	}

	private static XMLGregorianCalendar getXMLCalender(GregorianCalendar cal) {
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void checkPropertyLengthMin(Collection<?> collection, int min) throws PainParserException {
		if (collection == null | collection.isEmpty() | collection.size()< min)
			throw new PainParserException(ParserExceptionType.GENERAL, "Invalid length of collection: " + collection);
	}

	public static void checkPropertyLengthMin(String propertyValue, int min) throws PainParserException {
		
		if (propertyValue.isEmpty() | propertyValue.length() < min)
			throw new PainParserException(ParserExceptionType.GENERAL, "Invalid length of property: " + propertyValue);
	}
	
	public static void checkPropertyLengthMax(String propertyValue, int max) throws PainParserException {
		checkPropertyLength(propertyValue, 1, max);
	}
	

	public static void checkPropertyLength(String propertyValue, int min, int max) throws PainParserException {
		Objects.requireNonNull(propertyValue, "propertyValue must not be null");

		if (propertyValue.isEmpty() | propertyValue.length() < min | propertyValue.length() > max)
			throw new PainParserException(ParserExceptionType.GENERAL, "Invalid length of property: " + propertyValue);

	}

}
