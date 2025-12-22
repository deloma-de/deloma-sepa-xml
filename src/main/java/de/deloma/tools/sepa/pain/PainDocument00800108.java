package de.deloma.tools.sepa.pain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.exception.PainParserException.ParserExceptionType;
import de.deloma.tools.sepa.model.pain.pain0800108.AccountIdentification4Choice;
import de.deloma.tools.sepa.model.pain.pain0800108.ActiveOrHistoricCurrencyAndAmount;
import de.deloma.tools.sepa.model.pain.pain0800108.BranchAndFinancialInstitutionIdentification6;
import de.deloma.tools.sepa.model.pain.pain0800108.CashAccount38;
import de.deloma.tools.sepa.model.pain.pain0800108.ChargeBearerType1Code;
import de.deloma.tools.sepa.model.pain.pain0800108.CustomerDirectDebitInitiationV08;
import de.deloma.tools.sepa.model.pain.pain0800108.DirectDebitTransaction10;
import de.deloma.tools.sepa.model.pain.pain0800108.DirectDebitTransactionInformation23;
import de.deloma.tools.sepa.model.pain.pain0800108.Document;
import de.deloma.tools.sepa.model.pain.pain0800108.FinancialInstitutionIdentification18;
import de.deloma.tools.sepa.model.pain.pain0800108.GenericPersonIdentification1;
import de.deloma.tools.sepa.model.pain.pain0800108.GroupHeader83;
import de.deloma.tools.sepa.model.pain.pain0800108.LocalInstrument2Choice;
import de.deloma.tools.sepa.model.pain.pain0800108.MandateRelatedInformation14;
import de.deloma.tools.sepa.model.pain.pain0800108.Party38Choice;
import de.deloma.tools.sepa.model.pain.pain0800108.PartyIdentification135;
import de.deloma.tools.sepa.model.pain.pain0800108.PaymentIdentification6;
import de.deloma.tools.sepa.model.pain.pain0800108.PaymentInstruction29;
import de.deloma.tools.sepa.model.pain.pain0800108.PaymentMethod2Code;
import de.deloma.tools.sepa.model.pain.pain0800108.PaymentTypeInformation29;
import de.deloma.tools.sepa.model.pain.pain0800108.PersonIdentification13;
import de.deloma.tools.sepa.model.pain.pain0800108.PersonIdentificationSchemeName1Choice;
import de.deloma.tools.sepa.model.pain.pain0800108.RemittanceInformation16;
import de.deloma.tools.sepa.model.pain.pain0800108.SequenceType3Code;
import de.deloma.tools.sepa.model.pain.pain0800108.ServiceLevel8Choice;
import de.deloma.tools.sepa.pain.wrapper.CollectorPaymentInfoPain;
import de.deloma.tools.sepa.pain.wrapper.CreditorInfo;
import de.deloma.tools.sepa.pain.wrapper.GroupHeaderInfo;
import de.deloma.tools.sepa.pain.wrapper.PainTransaction;
import de.deloma.tools.sepa.util.ParserUtils;

/**
 * pain.008.001.08 document creator
 *
 * @author Marco Janc 2025
 */
public class PainDocument00800108
{
	/**
	 * creates a new document of given collector infos
	 *
	 * @param groupHeaderInfo
	 * @param collectorPaymentInfos
	 *
	 * @return
	 *
	 * @throws PainParserException
	 */
	public static Document createDocument(final GroupHeaderInfo groupHeaderInfo,
		final List<CollectorPaymentInfoPain> collectorPaymentInfos) throws PainParserException
	{
		GroupHeaderInfo.validate(groupHeaderInfo);

		// create payment info
		final List<PaymentInstruction29> paymentInfoList = new ArrayList<>(collectorPaymentInfos.size());
		for (final CollectorPaymentInfoPain collectorPaymentInfo : collectorPaymentInfos)
			paymentInfoList.add(PainDocument00800108.createPayInstrInf(collectorPaymentInfo));

		// Number of total transactions in all paymentInfos
		final int numTxs = paymentInfoList.stream()
			.collect(Collectors.summingInt(p -> Integer.parseInt(p.getNbOfTxs())));
		if (numTxs < 0)
			throw new PainParserException(ParserExceptionType.GENERAL, "invalid number of transactions!");

		// There are many other fields for initiating party identification, but
		// only name is recommended.
		final PartyIdentification135 partyIdentification = new PartyIdentification135();
		partyIdentification.setNm(groupHeaderInfo.getInitiator());

		final GroupHeader83 grpHdr = new GroupHeader83();
		grpHdr.setMsgId(groupHeaderInfo.getMsgId());
		grpHdr.setNbOfTxs(String.valueOf(numTxs));
		grpHdr.setCreDtTm(groupHeaderInfo.getCreationDateTime());
		grpHdr.setInitgPty(partyIdentification);

		final BigDecimal totalAmount = collectorPaymentInfos.stream().map(pi -> pi.getTotalAmount())
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		grpHdr.setCtrlSum(totalAmount);

		final CustomerDirectDebitInitiationV08 ddIntiation = new CustomerDirectDebitInitiationV08();
		ddIntiation.setGrpHdr(grpHdr);
		/*---------Payment Info--------*/
		ddIntiation.getPmtInves().addAll(paymentInfoList);

		final Document document = new Document();
		document.setCstmrDrctDbtInitn(ddIntiation);
		return document;
	}

	/**
	 * creates a single payment entity with given informations
	 *
	 * @param collectorPaymentInfoPain
	 *            <code>paymentinformations like paymentInfoid, collection date, Sepa codes (e.g, B2B, CORE, RCUR etc), as well as transactions </code>
	 * @return
	 *
	 * @throws PainParserException
	 */
	private static PaymentInstruction29 createPayInstrInf(final CollectorPaymentInfoPain collectorPaymentInfo)
		throws PainParserException
	{
		CollectorPaymentInfoPain.validate(collectorPaymentInfo);

		final CreditorInfo creditorInfo = collectorPaymentInfo.getCreditorInfo();

		// -- Transactions
		final List<DirectDebitTransactionInformation23> transactions = collectorPaymentInfo.getTransactions().stream()
			.map(t -> PainDocument00800108.createTransaction(t)).collect(Collectors.toList());

		// -- Creditor
		final PartyIdentification135 creditor = new PartyIdentification135();
		creditor.setNm(creditorInfo.getName());

		// -- PaymentTypeInfo
		final LocalInstrument2Choice localInstrumentSEPA = new LocalInstrument2Choice();
		localInstrumentSEPA.setCd(collectorPaymentInfo.getSepaLocalInstrumentCode().getValue());

		// --- SEPA ---
		final ServiceLevel8Choice serviceLevel = new ServiceLevel8Choice();
		serviceLevel.setCd("SEPA");

		final PaymentTypeInformation29 paymentTypeInfo = new PaymentTypeInformation29();
		paymentTypeInfo.getSvcLvls().add(serviceLevel);
		paymentTypeInfo.setLclInstrm(localInstrumentSEPA);
		paymentTypeInfo.setSeqTp(SequenceType3Code.valueOf(collectorPaymentInfo.getSequenceTypeCode().toString()));

		// -- PaymentInstructionInfo
		final PaymentInstruction29 paymentInfo = new PaymentInstruction29();
		paymentInfo.setPmtInfId(collectorPaymentInfo.getPaymentInfoId());
		paymentInfo.setPmtMtd(PaymentMethod2Code.DD);
		paymentInfo.setNbOfTxs(String.valueOf(transactions.size()));
		paymentInfo.setCtrlSum(collectorPaymentInfo.getTotalAmount());
		paymentInfo.setPmtTpInf(paymentTypeInfo);
		// collection date
		paymentInfo.setReqdColltnDt(collectorPaymentInfo.getCollectionDate());
		paymentInfo.setCdtr(creditor);
		// transactions
		paymentInfo.getDrctDbtTxInves().addAll(transactions);

		// Constant charge bearer: SLEV
		paymentInfo.setChrgBr(ChargeBearerType1Code.SLEV);

		/*
		 * Creditor Bank Info
		 */
		// IBAN or Bank id
		final CashAccount38 creditorBankinfoIBAN = new CashAccount38();
		final AccountIdentification4Choice id = new AccountIdentification4Choice();
		id.setIBAN(creditorInfo.getIban());
		creditorBankinfoIBAN.setId(id);
		paymentInfo.setCdtrAcct(creditorBankinfoIBAN);

		// BIC or Bank branch
		final BranchAndFinancialInstitutionIdentification6 crdtrBankAndBranch = new BranchAndFinancialInstitutionIdentification6();
		final FinancialInstitutionIdentification18 crdtrBankId = new FinancialInstitutionIdentification18();
		crdtrBankId.setBICFI(creditorInfo.getBic());
		crdtrBankAndBranch.setFinInstnId(crdtrBankId);
		paymentInfo.setCdtrAgt(crdtrBankAndBranch);

		// Glaeubiger Id
		final PartyIdentification135 cdtrSchmeId = new PartyIdentification135();
		final Party38Choice schemeId = new Party38Choice();
		final PersonIdentification13 prvtId = new PersonIdentification13();
		final GenericPersonIdentification1 personIdentificationSEPA = new GenericPersonIdentification1();
		personIdentificationSEPA.setId(creditorInfo.getGlauebigerId());

		final PersonIdentificationSchemeName1Choice schemNm = new PersonIdentificationSchemeName1Choice();
		schemNm.setPrtry("SEPA");
		personIdentificationSEPA.setSchmeNm(schemNm);
		prvtId.getOthrs().add(personIdentificationSEPA);
		schemeId.setPrvtId(prvtId);
		cdtrSchmeId.setId(schemeId);
		paymentInfo.setCdtrSchmeId(cdtrSchmeId);

		return paymentInfo;
	}

	/**
	 * creates a single transaction for the given one
	 *
	 * @param painTransaction
	 *
	 * @return
	 */
	private static DirectDebitTransactionInformation23 createTransaction(final PainTransaction painTransaction)
	{
		final DirectDebitTransactionInformation23 transaction = new DirectDebitTransactionInformation23();

		// pmtId: EndtoEnd
		final PaymentIdentification6 pmtId = new PaymentIdentification6();
		pmtId.setEndToEndId(painTransaction.getEndToEndId());
		transaction.setPmtId(pmtId);

		// Amount
		final ActiveOrHistoricCurrencyAndAmount amountSEPA = new ActiveOrHistoricCurrencyAndAmount();
		amountSEPA.setValue(painTransaction.getAmount());
		amountSEPA.setCcy("EUR");
		transaction.setInstdAmt(amountSEPA);

		// Debitor name
		final PartyIdentification135 dbtr = new PartyIdentification135();
		dbtr.setNm(painTransaction.getDbtrName());
		transaction.setDbtr(dbtr);

		// Debitor Bank Account

		// IBAN
		final CashAccount38 dbtrAcct = new CashAccount38();
		final AccountIdentification4Choice dbtrAccId = new AccountIdentification4Choice();
		dbtrAccId.setIBAN(painTransaction.getDbtrIban());
		dbtrAcct.setId(dbtrAccId);
		transaction.setDbtrAcct(dbtrAcct);

		// BIC
		final BranchAndFinancialInstitutionIdentification6 dbtrBicInf = new BranchAndFinancialInstitutionIdentification6();

		final FinancialInstitutionIdentification18 dbtrBankInfo = new FinancialInstitutionIdentification18();

		dbtrBankInfo.setBICFI(painTransaction.getDbtrBic());
		dbtrBicInf.setFinInstnId(dbtrBankInfo);
		transaction.setDbtrAgt(dbtrBicInf);

		// mandate
		final DirectDebitTransaction10 ddtxValue = new DirectDebitTransaction10();
		final MandateRelatedInformation14 mandateInfo = new MandateRelatedInformation14();

		// Mandate Id
		mandateInfo.setMndtId(painTransaction.getMandateId());

		// mandate date of signature
		mandateInfo.setDtOfSgntr(ParserUtils.dateToXmlGregorianNoOffset(painTransaction.getDtOfSgntr()));
		mandateInfo.setAmdmntInd(false);
		ddtxValue.setMndtRltdInf(mandateInfo);
		transaction.setDrctDbtTx(ddtxValue);

		// Debitor other name
		final PartyIdentification135 ultDbtrNm = new PartyIdentification135();
		ultDbtrNm.setNm(painTransaction.getUltDbtrName());
		transaction.setUltmtDbtr(ultDbtrNm);

		// Verbindungszweck = RechnungsNr.
		final RemittanceInformation16 rmtInf = new RemittanceInformation16();
		rmtInf.getUstrds().add(painTransaction.getUstrdRemInf());
		transaction.setRmtInf(rmtInf);

		return transaction;
	}

}
