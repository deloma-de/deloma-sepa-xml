package de.deloma.tools.sepa.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.model.pain.pain0800302.Document;
import de.deloma.tools.sepa.pain.PainDocument00800102;
import de.deloma.tools.sepa.pain.PainDocument00800302;
import de.deloma.tools.sepa.pain.PainDocumentType;
import de.deloma.tools.sepa.pain.wrapper.CollectorPaymentInfoPain;
import de.deloma.tools.sepa.pain.wrapper.GroupHeaderInfo;

/**
 * class to generate pain XML files
 *
 * @see https://www.hettwer-beratung.de/sepa-spezialwissen/sepa-technische-anforderungen/pain-format-xml-nachrichtentyp/
 *
 * @author Azahar Hossain (c) 2022
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
	public static String createDocumentXml(final PainDocumentType type, final GroupHeaderInfo headerInfo, final List<CollectorPaymentInfoPain> paymentInfoList)
		throws IOException, PainParserException
	{
		switch (type)
		{
			case PAIN00800302:
				final Document document00800302 = PainDocument00800302.createDocument(headerInfo, paymentInfoList);
				return PainParser.createDocumentXml(type, document00800302);

			case PAIN00800102:
				final de.deloma.tools.sepa.model.pain.pain0800102.Document document00800102 = PainDocument00800102.createDocument(headerInfo, paymentInfoList);
				return PainParser.createDocumentXml(type, document00800102);

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

		String schemaLocation = "";

		@SuppressWarnings("rawtypes")
		Class factoryClazz;

		switch (type)
		{

			case PAIN00800302:
				schemaLocation += "urn:iso:std:iso:20022:tech:xsd:pain.008.003.02 pain.008.003.02.xsd";
				factoryClazz = de.deloma.tools.sepa.model.pain.pain0800302.ObjectFactory.class;

				return BaseXmlFactory.createXmlFile(document, schemaLocation, factoryClazz);
			case PAIN00800102:
				schemaLocation += "urn:iso:std:iso:20022:tech:xsd:pain.008.001.02 pain.008.001.02.xsd";
				factoryClazz = de.deloma.tools.sepa.model.pain.pain0800102.ObjectFactory.class;

				return BaseXmlFactory.createXmlFile(document, schemaLocation, factoryClazz);
			default:
				throw new UnsupportedOperationException("unknown type");

		}
	}

}
