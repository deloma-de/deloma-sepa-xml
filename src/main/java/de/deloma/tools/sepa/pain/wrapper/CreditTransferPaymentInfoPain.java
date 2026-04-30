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
 * Credit transfer payment info holding a debtor, execution date and transactions.
 *
 */
public class CreditTransferPaymentInfoPain
{
	private DebtorInfo debtorInfo;

	private String paymentInfoId;

	private XMLGregorianCalendar executionDate;

	private final List<CreditTransferTransactionPain> transactions = new ArrayList<>();

	public CreditTransferPaymentInfoPain()
	{
	}

	public CreditTransferPaymentInfoPain(final DebtorInfo debtorInfo, final String paymentInfoId,
		final Date executionDate, final List<CreditTransferTransactionPain> transactions)
	{
		this.debtorInfo = debtorInfo;
		this.paymentInfoId = paymentInfoId;
		this.executionDate = ParserUtils.dateToXmlGregorianNoOffset(executionDate);
		this.transactions.addAll(transactions);
	}

	public DebtorInfo getDebtorInfo()
	{
		return this.debtorInfo;
	}

	public String getPaymentInfoId()
	{
		return this.paymentInfoId;
	}

	public XMLGregorianCalendar getExecutionDate()
	{
		return this.executionDate;
	}

	public List<CreditTransferTransactionPain> getTransactions()
	{
		return this.transactions;
	}

	public static void validate(final CreditTransferPaymentInfoPain paymentInfo) throws PainParserException
	{
		Objects.requireNonNull(paymentInfo, "paymentInfo must not be null");

		DebtorInfo.validate(paymentInfo.getDebtorInfo());

		ParserUtils.checkPropertyLengthMin(paymentInfo.transactions, 1);
		ParserUtils.checkPropertyLengthMax(paymentInfo.paymentInfoId, 35);

		if (!paymentInfo.executionDate.isValid() || paymentInfo.executionDate.toGregorianCalendar().before(new Date()))
			throw new PainParserException(ParserExceptionType.PAYMENT_INFO_ERROR, "Invalid execution date");
	}

	public BigDecimal getTotalAmount()
	{
		final BigDecimal totalAmount = this.transactions.stream().map(t -> t.getAmount())
			.reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.CEILING);
		return totalAmount;
	}
}