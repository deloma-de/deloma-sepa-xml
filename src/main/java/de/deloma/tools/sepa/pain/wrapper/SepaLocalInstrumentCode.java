package de.deloma.tools.sepa.pain.wrapper;

/**
 * normal direct debit : LOCAL_INSTRUMENT_NORMAL = 'CORE'; urgent direct
 * debit : LOCAL_INSTRUMENT_URGENT = 'COR1'; business direct debit :
 * LOCAL_INSTRUMENT_BUSINESS = 'B2B';
 * 
 * private static String LOCAL_INSTRUMENT_NORMAL = "CORE"; private static
 * String LOCAL_INSTRUMENT_URGENT = "COR1"; private static String
 * LOCAL_INSTRUMENT_BUSINESS = "B2B";
 * 
 * @author Azahar Hossain
 */
public enum SepaLocalInstrumentCode {

	CORE("CORE"), 
	COR1("COR1"), 
	B2B("B2B");

	private String value;

	SepaLocalInstrumentCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}