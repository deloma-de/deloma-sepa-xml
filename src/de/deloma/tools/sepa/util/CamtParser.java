package de.deloma.tools.sepa.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;

import de.deloma.tools.sepa.model.camt.camt530102.ObjectFactory;

/**
 * Main CamtParser class parses xml file to given Class type f.e
 * {@link Document052} for Camt052
 */
public class CamtParser implements Serializable {

	private static final long serialVersionUID = -7216026942252041361L;

	public enum CAMTTYPE {

		CAMT52_001_02("camt.052.001.02"), CAMT52_001_08("camt.052.001.08"),

		// NOT used yet
		CAMT53("camt.052.001.08"), CAMT54("camt.052.001.08");

		private String xsdVersion;

		private CAMTTYPE(String xsdVersion) {
			this.xsdVersion = xsdVersion;
		}

		public String getXsdVersion() {
			return xsdVersion;
		}
		

	}

	protected CAMTTYPE camtType;

	public CamtParser() {

	}

	public CamtParser(CAMTTYPE camtType) {
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
	public <T extends Object> T parse(InputStream is) throws JAXBException, XMLStreamException {
		switch (camtType) {

		case CAMT52_001_02:

			return (T) BaseXmlFactory.<T>parse(is, de.deloma.tools.sepa.model.camt.camt5200102.Document.class);

		case CAMT52_001_08:
			return (T) BaseXmlFactory.<T>parse(is, de.deloma.tools.sepa.model.camt.camt5200108.Document.class);
		default:
			throw new UnsupportedOperationException("unknown camt type: " + camtType);
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
	private <T> Class<? extends Object> getCamtDocumentClass() {
		switch (camtType) {

		case CAMT52_001_02:
			return (Class<T>) de.deloma.tools.sepa.model.camt.camt5200102.Document.class;
		// return BaseXmlFactory.<T>parse(is,
		// de.deloma.tools.sepa.model.camt.camt5200102.Document.class);

		case CAMT52_001_08:
			return (Class<T>) de.deloma.tools.sepa.model.camt.camt5200108.Document.class;

		// TODO: FIX it after model generating
		case CAMT53:
			// return BaseXmlFactory.parse(is,
			// de.deloma.tools.sepa.model.camt.camt530102.Document.class);
			// TODO: FIX it after model generating
		case CAMT54:
			// return BaseXmlFactory.parse(is,
			// de.deloma.tools.sepa.model.camt.camt540102.ObjectFactory.class);
		default:
			throw new UnsupportedOperationException("unknown camt type: " + camtType);
		}
	}

	/**
	 * Checks if 
	 * @param is
	 * @return
	 */
	public static CAMTTYPE getCamtTypeFromStream(InputStream is) {

		try {
			String fileContent = IOUtils.toString(is);
			return getCamtTypeFromXml(fileContent); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param fileContent
	 * @return
	 */
	public static CAMTTYPE getCamtTypeFromXml(String fileContent) {
		CAMTTYPE[] types = CAMTTYPE.values();

		for (CAMTTYPE camttype : types) {
			if (fileContent.contains(camttype.getXsdVersion()))
				return camttype;
		}
		return null;
	}

}
