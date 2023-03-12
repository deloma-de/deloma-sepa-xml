package de.deloma.tools.sepa.pain.wrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.xml.datatype.XMLGregorianCalendar;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.exception.PainParserException.ParserExceptionType;
import de.deloma.tools.sepa.util.ParserUtils;

/**
 * Direct debit collector info holding a creditor, instrument and sequence type
 * code aswell as transactions.
 *
 * @author Azahar Hossain 2022
 * @author Marco Janc 2023
 */
public class CollectorPaymentInfoPain
{
	private CreditorInfo creditorInfo;

	private String paymentInfoId;

	private SepaLocalInstrumentCode sepaLocalInstrumentCode;

	private SequenceTypeCode sequenceTypeCode;

	private XMLGregorianCalendar collectionDate;

	private final List<PainTransaction> transactions = new ArrayList<>();

	public CollectorPaymentInfoPain()
	{
	}

	public CollectorPaymentInfoPain(final CreditorInfo creditorInfo, final String paymentInfoId, final SepaLocalInstrumentCode sepaLocalInstrumentCode,
									final SequenceTypeCode sequenceTypeCode, final Date collectionDate, final List<PainTransaction> transactions)
	{
		this.creditorInfo = creditorInfo;
		this.paymentInfoId = paymentInfoId;
		this.sepaLocalInstrumentCode = sepaLocalInstrumentCode;
		this.sequenceTypeCode = sequenceTypeCode;
		this.collectionDate = ParserUtils.dateToXmlGregorianNoOffset(collectionDate);
		this.transactions.addAll(transactions);
	}

	public CreditorInfo getCreditorInfo()
	{
		return this.creditorInfo;
	}

	public String getPaymentInfoId()
	{
		return this.paymentInfoId;
	}

	public SepaLocalInstrumentCode getSepaLocalInstrumentCode()
	{
		return this.sepaLocalInstrumentCode;
	}

	public SequenceTypeCode getSequenceTypeCode()
	{
		return this.sequenceTypeCode;
	}

	public XMLGregorianCalendar getCollectionDate()
	{
		return this.collectionDate;
	}

	public List<PainTransaction> getTransactions()
	{
		return this.transactions;
	}

	public static void validate(final CollectorPaymentInfoPain paymentInfo) throws PainParserException
	{
		Objects.requireNonNull(paymentInfo, "paymentInfo must not be null");

		CreditorInfo.validate(paymentInfo.getCreditorInfo());

		ParserUtils.checkPropertyLengthMin(paymentInfo.transactions, 1);
		ParserUtils.checkPropertyLengthMax(paymentInfo.paymentInfoId, 35);

		if (!paymentInfo.collectionDate.isValid() || paymentInfo.collectionDate.toGregorianCalendar().before(new Date()))
			throw new PainParserException(ParserExceptionType.PAYMENT_INFO_ERROR, "Invalid collection date");

	}

	public BigDecimal getTotalAmount()
	{
		final BigDecimal totalAmount = this.transactions.stream().map(t -> t.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2,
			RoundingMode.CEILING);
		return totalAmount;
	}

}
