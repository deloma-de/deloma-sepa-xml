package de.deloma.tools.sepa.pain.wrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.datatype.XMLGregorianCalendar;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.exception.PainParserException.ParserExceptionType;
import de.deloma.tools.sepa.model.pain.pain0800302.DirectDebitTransactionInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.SequenceType1Code;
import de.deloma.tools.sepa.util.PainParser.SepaLocalInstrumentCode;
import de.deloma.tools.sepa.util.ParserUtils;


public class CollectorPaymentInfoPain0080302 {

	private String paymentInfoId;

	private SepaLocalInstrumentCode sepaLocalInstrumentCode;

	private SequenceType1Code sequenceTypeCode;

	private XMLGregorianCalendar collectionDate;

	private final List<DirectDebitTransactionInformationSDD> transactions = new ArrayList<>();

	public CollectorPaymentInfoPain0080302() {
	}

	public CollectorPaymentInfoPain0080302(String paymentInfoId, SepaLocalInstrumentCode sepaLocalInstrumentCode,
			SequenceType1Code sequenceTypeCode, Date collectionDate,
			List<DirectDebitTransactionInformationSDD> transactions) {

		this.paymentInfoId = paymentInfoId;
		this.sepaLocalInstrumentCode = sepaLocalInstrumentCode;
		this.sequenceTypeCode = sequenceTypeCode;
		this.collectionDate = ParserUtils.dateToXmlGregorianNoOffset(collectionDate);
		this.transactions.addAll(transactions);
	}

	public String getPaymentInfoId() {
		return paymentInfoId;
	}

	public SepaLocalInstrumentCode getSepaLocalInstrumentCode() {
		return sepaLocalInstrumentCode;
	}

	public void setSepaLocalInstrumentCode(SepaLocalInstrumentCode sepaLocalInstrumentCode) {
		this.sepaLocalInstrumentCode = sepaLocalInstrumentCode;
	}

	public SequenceType1Code getSequenceTypeCode() {
		return sequenceTypeCode;
	}

	public void setSequenceTypeCode(SequenceType1Code sequenceTypeCode) {
		this.sequenceTypeCode = sequenceTypeCode;
	}

	public XMLGregorianCalendar getCollectionDate() {
		return collectionDate;
	}

	public List<DirectDebitTransactionInformationSDD> getTransactions() {
		return transactions;
	}

	public void checkProperties() throws PainParserException {
		ParserUtils.checkPropertyLengthMax(this.paymentInfoId, 35);
		if (!this.collectionDate.isValid() | this.collectionDate.toGregorianCalendar().before(new Date()))
			throw new PainParserException(ParserExceptionType.PAYMENT_INFO_ERROR, "Invalid collection date");
		ParserUtils.checkPropertyLengthMin(this.transactions, 1);
	}

	public BigDecimal getTotalAmount() {
		BigDecimal totalAmount = BigDecimal
				.valueOf(transactions.stream()
						.collect(Collectors.summingDouble(i -> i.getInstdAmt().getValue().doubleValue())))
				.setScale(2, RoundingMode.CEILING);
	return totalAmount; 
	}

}
