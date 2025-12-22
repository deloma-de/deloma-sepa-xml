package de.deloma.tools.sepa.camt;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import jakarta.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;

import de.deloma.tools.sepa.util.BaseXmlFactory;

/**
 * Main CamtParser class parses xml file to given Class type f.e
 * {@link Document052} for Camt052
 */
public class CamtParser implements Serializable
{

	private static final long serialVersionUID = -7216026942252041361L;

	public enum CAMTTYPE
	{

		CAMT52_001_02("camt.052.001.02"),
		CAMT52_001_08("camt.052.001.08"),

		// NOT used yet
		CAMT53("camt.052.001.08"),
		CAMT54("camt.052.001.08");

		private String xsdVersion;

		private CAMTTYPE(final String xsdVersion)
		{
			this.xsdVersion = xsdVersion;
		}

		public String getXsdVersion()
		{
			return this.xsdVersion;
		}

	}

	protected CAMTTYPE camtType;

	public CamtParser()
	{

	}

	public CamtParser(final CAMTTYPE camtType)
	{
		this.camtType = camtType;
	}

	/**
	 * Parses a
	 * <ul>
	 * <li>camt052.001.02.xml</li> or
	 * <li>camt052.001.08.xml</li> or
	 * <li>camt053.001.02.xml</li> or
	 * <li>camt054.001.02.xml</li>
	 * </ul>
	 * file and returns a Document object
	 *
	 * @param is
	 * @return
	 * @throws Exception
	 * @throws JAXBException
	 * @throws XMLStreamException
	 *
	 *             TODO: USE CUSTOM EXCEPTION
	 */
	public <T extends Object> T parse(final InputStream is) throws JAXBException, XMLStreamException
	{
		switch (this.camtType)
		{

			case CAMT52_001_02:

				return BaseXmlFactory.<T> parse(is, de.deloma.tools.sepa.model.camt.camt5200102.Document.class);

			case CAMT52_001_08:
				return BaseXmlFactory.<T> parse(is, de.deloma.tools.sepa.model.camt.camt5200108.Document.class);
			default:
				throw new UnsupportedOperationException("unknown camt type: " + this.camtType);
		}
	}

	/**
	 * Get Root Document class from given camt version
	 *
	 * TODO: Retrieve version from input stream dynamically by using regex
	 *
	 * @param <T>
	 *
	 * @return
	 */
	private <T> Class<? extends Object> getCamtDocumentClass()
	{
		switch (this.camtType)
		{

			case CAMT52_001_02:
				return de.deloma.tools.sepa.model.camt.camt5200102.Document.class;
			// return BaseXmlFactory.<T>parse(is,
			// de.deloma.tools.sepa.model.camt.camt5200102.Document.class);

			case CAMT52_001_08:
				return de.deloma.tools.sepa.model.camt.camt5200108.Document.class;

			// TODO: FIX it after model generating
			case CAMT53:
				// return BaseXmlFactory.parse(is,
				// de.deloma.tools.sepa.model.camt.camt530102.Document.class);
				// TODO: FIX it after model generating
			case CAMT54:
				// return BaseXmlFactory.parse(is,
				// de.deloma.tools.sepa.model.camt.camt540102.ObjectFactory.class);
			default:
				throw new UnsupportedOperationException("unknown camt type: " + this.camtType);
		}
	}

	/**
	 * Checks if
	 *
	 * @param is
	 * @return
	 */
	public static CAMTTYPE getCamtTypeFromStream(final InputStream is)
	{

		try
		{
			final String fileContent = IOUtils.toString(is);
			return CamtParser.getCamtTypeFromXml(fileContent);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param fileContent
	 * @return
	 */
	public static CAMTTYPE getCamtTypeFromXml(final String fileContent)
	{
		final CAMTTYPE[] types = CAMTTYPE.values();

		for (final CAMTTYPE camttype : types)
			if (fileContent.contains(camttype.getXsdVersion()))
				return camttype;
		return null;
	}

}
