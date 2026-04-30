package de.deloma.tools.sepa.pain.wrapper;

import java.util.Objects;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.util.ParserUtils;

/**
 * A wrapper class to populate debtor informations in Pain file formats
 *
 */
public class DebtorInfo
{
	private String name;
	private String iban;
	private String bic;

	public DebtorInfo()
	{
	}

	public DebtorInfo(final String name, final String iban, final String bic)
	{
		this.name = name;
		this.iban = iban;
		this.bic = bic;
	}

	public static void validate(final DebtorInfo debtorInfo) throws PainParserException
	{
		Objects.requireNonNull(debtorInfo, "debtorInfo must not be null");
		ParserUtils.checkPropertyLengthMax(debtorInfo.name, 70);
		ParserUtils.checkPropertyLength(debtorInfo.iban, 5, 34);
		ParserUtils.checkPropertyLength(debtorInfo.bic, 8, 11);
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
}