package de.deloma.tools.sepa.pain.wrapper;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.util.ParserUtils;

/**
 * A wrapper class to populate creditor informations in Camt and Pain file
 * formats
 * 
 * @author Azahar
 * @since 2023
 * @see PainParser#createPaymentInstructionInfoSDD()
 */
public class CreditorInfo {

	private String name;
	private String iban;
	private String bic;

	private String glauebigerId;

	public CreditorInfo() {
	}

	public CreditorInfo(String name, String iban, String bic, String glauebigerId) {
		this.name = name;
		this.iban = iban;
		this.bic = bic;
		this.glauebigerId = glauebigerId;
	}

	public String getName() {
		return name;
	}

	public String getIban() {
		return iban;
	}

	public String getBic() {
		return bic;
	}

	public String getGlauebigerId() {
		return glauebigerId;
	}
	
	
	public void checkProperties() throws PainParserException{
		ParserUtils.checkPropertyLengthMax(this.name,70);
		ParserUtils.checkPropertyLength(this.iban,5, 34);
		ParserUtils.checkPropertyLength(this.bic, 8,11);
		ParserUtils.checkPropertyLengthMax(this.glauebigerId,35);
	}

}
