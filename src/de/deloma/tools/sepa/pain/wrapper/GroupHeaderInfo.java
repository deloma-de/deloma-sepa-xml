package de.deloma.tools.sepa.pain.wrapper;

import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.pain.PainParser;
import de.deloma.tools.sepa.util.ParserUtils;

/**
 * A wrapper class to populate Group Header with all required informations in
 * Camt and Pain file formats
 *
 * TODO: currently only PainParser uses this wrapper, refactor CamtParser to use
 * it.
 *
 * @author Azahar
 * @since 2023
 * @see PainParser#createDocumentRaw(String, java.util.Date, String,
 *      java.util.List)
 */
public class GroupHeaderInfo
{

	/**
	 * required "msgId" property unique per file, max length 35
	 */
	private String msgId;

	/**
	 * required property when the file is created, max length 35
	 */
	private XMLGregorianCalendar creationDateTime;

	/**
	 * For identification : Initiating party, xml tag = <InitgPty>
	 *
	 * According to following link, other properties like id, address etc. are
	 * not recommended to use.
	 */
	private String initiator;

	public GroupHeaderInfo()
	{
	}

	public GroupHeaderInfo(final String msgId, final Date creationDateTime, final String initiator)
	{

		final XMLGregorianCalendar creationDateTimeXml = ParserUtils.dateToXmlGregorian(creationDateTime, TimeZone.getTimeZone(ParserUtils.utcZoneID));
		this.msgId = msgId;
		this.creationDateTime = creationDateTimeXml;

		this.initiator = initiator;

	}

	public static void validate(final GroupHeaderInfo groupHeaderInfo) throws PainParserException
	{
		Objects.requireNonNull(groupHeaderInfo, "groupHeaderInfo must not be null");
		ParserUtils.checkPropertyLength(groupHeaderInfo.getMsgId(), 1, 35);
		ParserUtils.checkPropertyLength(groupHeaderInfo.getInitiator(), 1, 70);
	}

	/*
	 * getter and setter
	 */

	public String getMsgId()
	{
		return this.msgId;
	}

	public void setMsgId(final String msgId)
	{
		this.msgId = msgId;
	}

	public XMLGregorianCalendar getCreationDateTime()
	{
		return this.creationDateTime;
	}

	public void setCreationDateTime(final XMLGregorianCalendar creationDateTime)
	{
		this.creationDateTime = creationDateTime;
	}

	public String getInitiator()
	{
		return this.initiator;
	}

	public void setInitiator(final String initiator)
	{
		this.initiator = initiator;
	}

}
