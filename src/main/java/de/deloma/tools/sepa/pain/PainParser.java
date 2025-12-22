package de.deloma.tools.sepa.pain;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.model.pain.pain0800302.Document;
import de.deloma.tools.sepa.pain.wrapper.CollectorPaymentInfoPain;
import de.deloma.tools.sepa.pain.wrapper.GroupHeaderInfo;
import de.deloma.tools.sepa.util.BaseXmlFactory;

/**
 * class to generate pain XML files
 *
 * @see https://www.hettwer-beratung.de/sepa-spezialwissen/sepa-technische-anforderungen/pain-format-xml-nachrichtentyp/
 *
 * @author Azahar Hossain (c) 2022
 * @author Marco Janc (c) 2025
 */
public class PainParser
{

	public PainDocumentType documentType;

	public PainParser()
	{
	}

	public PainParser(final PainDocumentType documentType)
	{
		Objects.requireNonNull(documentType, "documentType must not be null");
		this.documentType = documentType;
	}

	/**
	 * Read PAIN.008.003.02.xml and return a documentPain00800302
	 *
	 * @param <T>
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T parse(final InputStream is) throws Exception
	{
		switch (this.documentType)
		{
			case PAIN00800302:
				return (T) BaseXmlFactory.parse(is, de.deloma.tools.sepa.model.pain.pain0800302.ObjectFactory.class);
			case PAIN00800102:
				return (T) BaseXmlFactory.parse(is, de.deloma.tools.sepa.model.pain.pain0800102.ObjectFactory.class);
			case PAIN00800108:
				return (T) BaseXmlFactory.parse(is, de.deloma.tools.sepa.model.pain.pain0800108.ObjectFactory.class);
			default:
				throw new UnsupportedOperationException("invalid type");
		}
	}

	public PainDocumentType getDocumentType()
	{
		return this.documentType;
	}

	/*
	 * static XML generator
	 */

	/**
	 * Creates the XML document of given type
	 *
	 * @param type
	 * @param headerInfo
	 * @param paymentInfoList
	 *
	 * @return
	 *
	 * @throws IOException
	 * @throws PainParserException
	 */
	public static String createDocumentXml(final PainDocumentType type, final GroupHeaderInfo headerInfo,
		final List<CollectorPaymentInfoPain> paymentInfoList) throws IOException, PainParserException
	{
		switch (type)
		{
			case PAIN00800302:
				final Document document00800302 = PainDocument00800302.createDocument(headerInfo, paymentInfoList);
				return PainParser.createDocumentXml(type, document00800302);

			case PAIN00800102:
				final de.deloma.tools.sepa.model.pain.pain0800102.Document document00800102 = PainDocument00800102
					.createDocument(headerInfo, paymentInfoList);
				return PainParser.createDocumentXml(type, document00800102);

			case PAIN00800108:
				final de.deloma.tools.sepa.model.pain.pain0800108.Document document00800108 = PainDocument00800108
					.createDocument(headerInfo, paymentInfoList);
				return PainParser.createDocumentXml(type, document00800108);

			default:
				throw new UnsupportedOperationException("unknown type");

		}
	}

	/**
	 * Creates the XML document of given type and Document instance
	 *
	 * @param type
	 * @param document
	 *
	 * @return
	 *
	 * @throws IOException
	 */
	public static String createDocumentXml(final PainDocumentType type, final Object document) throws IOException
	{
		Objects.requireNonNull(type, "type must not be null");

		final String schemaLocation = type.getSchemaLocation();

		final Class<?> factoryClass = type.getFactoryClass();

		return BaseXmlFactory.createXmlFile(document, schemaLocation, factoryClass);
	}

}
