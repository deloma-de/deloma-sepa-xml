package de.deloma.tools.sepa.pain.wrapper;

import java.math.BigDecimal;

/**
 * PAIN credit transfer transaction definition class
 */
public class CreditTransferTransactionPain
{
	private final String endToEndId;
	private final BigDecimal amount;
	private final String creditorName;
	private final String creditorIban;
	private final String creditorBic;
	private final String ustrdRemInf;

	public CreditTransferTransactionPain(final String endToEndId, final BigDecimal amount,
		final String creditorName, final String creditorIban, final String creditorBic,
		final String ustrdRemInf)
	{
		this.endToEndId = endToEndId;
		this.amount = amount;
		this.creditorName = creditorName;
		this.creditorIban = creditorIban;
		this.creditorBic = creditorBic;
		this.ustrdRemInf = ustrdRemInf;
	}

	public String getEndToEndId()
	{
		return this.endToEndId;
	}

	public BigDecimal getAmount()
	{
		return this.amount;
	}

	public String getCreditorName()
	{
		return this.creditorName;
	}

	public String getCreditorIban()
	{
		return this.creditorIban;
	}

	public String getCreditorBic()
	{
		return this.creditorBic;
	}

	public String getUstrdRemInf()
	{
		return this.ustrdRemInf;
	}
}