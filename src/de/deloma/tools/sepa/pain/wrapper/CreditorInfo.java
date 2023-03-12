package de.deloma.tools.sepa.pain.wrapper;

import java.util.Objects;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.pain.PainParser;
import de.deloma.tools.sepa.util.ParserUtils;

/**
 * A wrapper class to populate creditor informations in Camt and Pain file
 * formats
 *
 * @author Azahar
 * @since 2023
 * @see PainParser#createPaymentInstructionInfoSDD()
 */
public class CreditorInfo
{

	private String name;
	private String iban;
	private String bic;

	private String glauebigerId;

	public CreditorInfo()
	{
	}

	public CreditorInfo(final String name, final String iban, final String bic, final String glauebigerId)
	{
		this.name = name;
		this.iban = iban;
		this.bic = bic;
		this.glauebigerId = glauebigerId;
	}

	public static void validate(final CreditorInfo creditorInfo) throws PainParserException
	{
		Objects.requireNonNull(creditorInfo, "creditorInfo must not be null");
		ParserUtils.checkPropertyLengthMax(creditorInfo.name, 70);
		ParserUtils.checkPropertyLength(creditorInfo.iban, 5, 34);
		ParserUtils.checkPropertyLength(creditorInfo.bic, 8, 11);
		ParserUtils.checkPropertyLengthMax(creditorInfo.glauebigerId, 35);
	}

	public String getName()
	{
		return this.name;
	}

	public String getIban()
	{
		return this.iban;
	}

	public String getBic()
	{
		return this.bic;
	}

	public String getGlauebigerId()
	{
		return this.glauebigerId;
	}

}
