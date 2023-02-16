package de.deloma.tools.sepa.util;

import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
/**
 * Main CamtParser class parses xml file to given Class type f.e
 * {@link Document052} for Camt052
 */
public class CamtParser {
	public enum CAMTTYPE {
		CAMT52, CAMT53, CAMT54;

	}

	protected CAMTTYPE camtType;

	public CamtParser() {

	}

	/**
	 * @param camtType
	 */
	public CamtParser(CAMTTYPE camtType) {
		this.camtType = camtType;
	}

	/**
	 * Parses a
	 * <ul>
	 * <li>camt052.001.02.xml</li> or
	 * <li>camt053.001.02.xml</li> or
	 * <li>camt054.001.02.xml</li>
	 * </ul>
	 * file and returns a {@link Document052} object
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 * @throws JAXBException
	 * @throws XMLStreamException
	 */
	public <T> T parse(InputStream is) throws Exception {
		switch (camtType) {
		case CAMT52:
			return BaseXmlFactory.parse(is, de.deloma.tools.sepa.model.camt.camt520102.Document.class);
		// TODO: FIX it after model generating
		case CAMT53:
			return BaseXmlFactory.parse(is, de.deloma.tools.sepa.model.camt.camt520102.Document.class);
		// TODO: FIX it after model generating
		case CAMT54:
			return BaseXmlFactory.parse(is, de.deloma.tools.sepa.model.camt.camt520102.ObjectFactory.class);
		default:
			throw new UnsupportedOperationException("unknown camt type: " + camtType);
		}
	}

}
