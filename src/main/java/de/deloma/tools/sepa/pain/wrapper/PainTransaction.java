package de.deloma.tools.sepa.pain.wrapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * PAIN transaction definition class
 *
 * @author Marco Janc
 */
public class PainTransaction
{
	private final String endToEndId;
	private final BigDecimal amount;
	private final String dbtrName;
	private final String dbtrIban;
	private final String dbtrBic;
	private final String mandateId;
	private final Date dtOfSgntr;
	private final String ultDbtrName;
	private final String ustrdRemInf;

	public PainTransaction(	final String endToEndId, final BigDecimal amount, final String dbtrName, final String dbtrIban, final String dbtrBic,
							final String mandateId, final Date dtOfSgntr, final String ultDbtrName, final String ustrdRemInf)
	{
		this.endToEndId = endToEndId;
		this.amount = amount;
		this.dbtrName = dbtrName;
		this.dbtrIban = dbtrIban;
		this.dbtrBic = dbtrBic;
		this.mandateId = mandateId;
		this.dtOfSgntr = dtOfSgntr;
		this.ultDbtrName = ultDbtrName;
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

	public String getDbtrName()
	{
		return this.dbtrName;
	}

	public String getDbtrIban()
	{
		return this.dbtrIban;
	}

	public String getDbtrBic()
	{
		return this.dbtrBic;
	}

	public String getMandateId()
	{
		return this.mandateId;
	}

	public Date getDtOfSgntr()
	{
		return this.dtOfSgntr;
	}

	public String getUltDbtrName()
	{
		return this.ultDbtrName;
	}

	public String getUstrdRemInf()
	{
		return this.ustrdRemInf;
	}
}
