package de.deloma.tools.sepa.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import de.deloma.tools.sepa.pain.PainDocumentType;

/**
 * class to generate pain XML files
 * 
 * @see https://www.hettwer-beratung.de/sepa-spezialwissen/sepa-technische-anforderungen/pain-format-xml-nachrichtentyp/
 * 
 * @author Azahar Hossain (c) 2022
 */
public class PainParser {

	public PainDocumentType documentType;

	public PainParser() {
	}

	public PainParser(PainDocumentType documentType) {
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
	public <T> T parse(InputStream is) throws Exception {
		
		switch (this.documentType) {
			case PAIN00800302:
				return (T) BaseXmlFactory.parse(is, de.deloma.tools.sepa.model.pain.pain0800302.ObjectFactory.class);
			case PAIN00800102:
				return (T) BaseXmlFactory.parse(is, de.deloma.tools.sepa.model.pain.pain0800102.ObjectFactory.class);
			default:
				throw new UnsupportedOperationException("invalid type");
		}
	}

	public static <T extends Object> String createDocumentXml(String filePath, PainDocumentType type, T document) throws IOException {

		String schemaLocation = "";
		
		@SuppressWarnings("rawtypes")
		Class factoryClazz ; 

		
		switch (type) {

			case PAIN00800302:
				schemaLocation += "urn:iso:std:iso:20022:tech:xsd:pain.008.003.02 pain.008.003.02.xsd";
				factoryClazz = de.deloma.tools.sepa.model.pain.pain0800302.ObjectFactory.class; 
				
				return BaseXmlFactory.createXmlFile(document, schemaLocation, factoryClazz);
			case PAIN00800102:
				schemaLocation += "urn:iso:std:iso:20022:tech:xsd:pain.008.001.02 pain.008.001.02.xsd";
				factoryClazz =  de.deloma.tools.sepa.model.pain.pain0800102.ObjectFactory.class;
	
				return BaseXmlFactory.createXmlFile(document, schemaLocation, factoryClazz);
			default:
				throw new UnsupportedOperationException("unknown type");
			
		}
	}

	public PainDocumentType getDocumentType() {
		return documentType;
	}


}
