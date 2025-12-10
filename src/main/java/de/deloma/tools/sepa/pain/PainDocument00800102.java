package de.deloma.tools.sepa.pain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.exception.PainParserException.ParserExceptionType;
import de.deloma.tools.sepa.model.pain.pain0800102.AccountIdentification4Choice;
import de.deloma.tools.sepa.model.pain.pain0800102.ActiveOrHistoricCurrencyAndAmount;
import de.deloma.tools.sepa.model.pain.pain0800102.BranchAndFinancialInstitutionIdentification4;
import de.deloma.tools.sepa.model.pain.pain0800102.CashAccount16;
import de.deloma.tools.sepa.model.pain.pain0800102.ChargeBearerType1Code;
import de.deloma.tools.sepa.model.pain.pain0800102.CustomerDirectDebitInitiationV02;
import de.deloma.tools.sepa.model.pain.pain0800102.DirectDebitTransaction6;
import de.deloma.tools.sepa.model.pain.pain0800102.DirectDebitTransactionInformation9;
import de.deloma.tools.sepa.model.pain.pain0800102.Document;
import de.deloma.tools.sepa.model.pain.pain0800102.FinancialInstitutionIdentification7;
import de.deloma.tools.sepa.model.pain.pain0800102.GenericPersonIdentification1;
import de.deloma.tools.sepa.model.pain.pain0800102.GroupHeader39;
import de.deloma.tools.sepa.model.pain.pain0800102.LocalInstrument2Choice;
import de.deloma.tools.sepa.model.pain.pain0800102.MandateRelatedInformation6;
import de.deloma.tools.sepa.model.pain.pain0800102.Party6Choice;
import de.deloma.tools.sepa.model.pain.pain0800102.PartyIdentification32;
import de.deloma.tools.sepa.model.pain.pain0800102.PaymentIdentification1;
import de.deloma.tools.sepa.model.pain.pain0800102.PaymentInstructionInformation4;
import de.deloma.tools.sepa.model.pain.pain0800102.PaymentMethod2Code;
import de.deloma.tools.sepa.model.pain.pain0800102.PaymentTypeInformation20;
import de.deloma.tools.sepa.model.pain.pain0800102.PersonIdentification5;
import de.deloma.tools.sepa.model.pain.pain0800102.PersonIdentificationSchemeName1Choice;
import de.deloma.tools.sepa.model.pain.pain0800102.RemittanceInformation5;
import de.deloma.tools.sepa.model.pain.pain0800102.SequenceType1Code;
import de.deloma.tools.sepa.model.pain.pain0800102.ServiceLevel8Choice;
import de.deloma.tools.sepa.pain.wrapper.CollectorPaymentInfoPain;
import de.deloma.tools.sepa.pain.wrapper.CreditorInfo;
import de.deloma.tools.sepa.pain.wrapper.GroupHeaderInfo;
import de.deloma.tools.sepa.pain.wrapper.PainTransaction;
import de.deloma.tools.sepa.util.ParserUtils;

/**
 * pain.008.001.02 document creator
 *
 * @author Marco Janc 2023
 */
public class PainDocument00800102
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
	public static Document createDocument(final GroupHeaderInfo groupHeaderInfo, final List<CollectorPaymentInfoPain> collectorPaymentInfos)
		throws PainParserException
	{
		GroupHeaderInfo.validate(groupHeaderInfo);

		// create payment info
		final List<PaymentInstructionInformation4> paymentInfoList = new ArrayList<>(collectorPaymentInfos.size());
		for (final CollectorPaymentInfoPain collectorPaymentInfo : collectorPaymentInfos)
			paymentInfoList.add(PainDocument00800102.createPayInstrInf(collectorPaymentInfo));

		// Number of total transactions in all paymentInfos
		final int numTxs = paymentInfoList.stream().collect(Collectors.summingInt(p -> Integer.parseInt(p.getNbOfTxs())));
		if (numTxs < 0)
			throw new PainParserException(ParserExceptionType.GENERAL, "invalid number of transactions!");

		// There are many other fields for initiating party identification, but
		// only name is recommended.
		final PartyIdentification32 partyIdentification = new PartyIdentification32();
		partyIdentification.setNm(groupHeaderInfo.getInitiator());

		final GroupHeader39 grpHdr = new GroupHeader39();
		grpHdr.setMsgId(groupHeaderInfo.getMsgId());
		grpHdr.setNbOfTxs(String.valueOf(numTxs));
		grpHdr.setCreDtTm(groupHeaderInfo.getCreationDateTime());
		grpHdr.setInitgPty(partyIdentification);

		final CustomerDirectDebitInitiationV02 ddIntiation = new CustomerDirectDebitInitiationV02();
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
	private static PaymentInstructionInformation4 createPayInstrInf(final CollectorPaymentInfoPain collectorPaymentInfo) throws PainParserException
	{
		CollectorPaymentInfoPain.validate(collectorPaymentInfo);

		final CreditorInfo creditorInfo = collectorPaymentInfo.getCreditorInfo();

		// -- Transactions
		final List<DirectDebitTransactionInformation9> transactions = collectorPaymentInfo.getTransactions().stream()
			.map(t -> PainDocument00800102.createTransaction(t)).collect(Collectors.toList());

		// -- Creditor
		final PartyIdentification32 creditor = new PartyIdentification32();
		creditor.setNm(creditorInfo.getName());

		// -- PaymentTypeInfo
		final LocalInstrument2Choice localInstrumentSEPA = new LocalInstrument2Choice();
		localInstrumentSEPA.setCd(collectorPaymentInfo.getSepaLocalInstrumentCode().getValue());

		// --- SEPA ---
		final ServiceLevel8Choice serviceLevel = new ServiceLevel8Choice();
		serviceLevel.setCd("SEPA");

		final PaymentTypeInformation20 paymentTypeInfo = new PaymentTypeInformation20();
		paymentTypeInfo.setSvcLvl(serviceLevel);
		paymentTypeInfo.setLclInstrm(localInstrumentSEPA);
		paymentTypeInfo.setSeqTp(SequenceType1Code.valueOf(collectorPaymentInfo.getSequenceTypeCode().toString()));

		// -- PaymentInstructionInfo
		final PaymentInstructionInformation4 paymentInfo = new PaymentInstructionInformation4();
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
		final CashAccount16 creditorBankinfoIBAN = new CashAccount16();
		final AccountIdentification4Choice id = new AccountIdentification4Choice();
		id.setIBAN(creditorInfo.getIban());
		creditorBankinfoIBAN.setId(id);
		paymentInfo.setCdtrAcct(creditorBankinfoIBAN);

		// BIC or Bank branch
		final BranchAndFinancialInstitutionIdentification4 crdtrBankAndBranch = new BranchAndFinancialInstitutionIdentification4();
		final FinancialInstitutionIdentification7 crdtrBankId = new FinancialInstitutionIdentification7();
		crdtrBankId.setBIC(creditorInfo.getBic());
		crdtrBankAndBranch.setFinInstnId(crdtrBankId);
		paymentInfo.setCdtrAgt(crdtrBankAndBranch);

		// Glaeubiger Id
		final PartyIdentification32 cdtrSchmeId = new PartyIdentification32();
		final Party6Choice schemeId = new Party6Choice();
		final PersonIdentification5 prvtId = new PersonIdentification5();
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
	private static DirectDebitTransactionInformation9 createTransaction(final PainTransaction painTransaction)
	{
		final DirectDebitTransactionInformation9 transaction = new DirectDebitTransactionInformation9();

		// pmtId: EndtoEnd
		final PaymentIdentification1 pmtId = new PaymentIdentification1();
		pmtId.setEndToEndId(painTransaction.getEndToEndId());
		transaction.setPmtId(pmtId);

		// Amount
		final ActiveOrHistoricCurrencyAndAmount amountSEPA = new ActiveOrHistoricCurrencyAndAmount();
		amountSEPA.setValue(painTransaction.getAmount());
		amountSEPA.setCcy("EUR");
		transaction.setInstdAmt(amountSEPA);

		// Debitor name
		final PartyIdentification32 dbtr = new PartyIdentification32();
		dbtr.setNm(painTransaction.getDbtrName());
		transaction.setDbtr(dbtr);

		// Debitor Bank Account

		// IBAN
		final CashAccount16 dbtrAcct = new CashAccount16();
		final AccountIdentification4Choice dbtrAccId = new AccountIdentification4Choice();
		dbtrAccId.setIBAN(painTransaction.getDbtrIban());
		dbtrAcct.setId(dbtrAccId);
		transaction.setDbtrAcct(dbtrAcct);

		// BIC
		final BranchAndFinancialInstitutionIdentification4 dbtrBicInf = new BranchAndFinancialInstitutionIdentification4();

		final FinancialInstitutionIdentification7 dbtrBankInfo = new FinancialInstitutionIdentification7();

		dbtrBankInfo.setBIC(painTransaction.getDbtrBic());
		dbtrBicInf.setFinInstnId(dbtrBankInfo);
		transaction.setDbtrAgt(dbtrBicInf);

		// mandate
		final DirectDebitTransaction6 ddtxValue = new DirectDebitTransaction6();
		final MandateRelatedInformation6 mandateInfo = new MandateRelatedInformation6();

		// Mandate Id
		mandateInfo.setMndtId(painTransaction.getMandateId());

		// mandate date of signature
		mandateInfo.setDtOfSgntr(ParserUtils.dateToXmlGregorianNoOffset(painTransaction.getDtOfSgntr()));
		mandateInfo.setAmdmntInd(false);
		ddtxValue.setMndtRltdInf(mandateInfo);
		transaction.setDrctDbtTx(ddtxValue);

		// Debitor other name
		final PartyIdentification32 ultDbtrNm = new PartyIdentification32();
		ultDbtrNm.setNm(painTransaction.getUltDbtrName());
		transaction.setUltmtDbtr(ultDbtrNm);

		// Verbindungszweck = RechnungsNr.
		final RemittanceInformation5 rmtInf = new RemittanceInformation5();
		rmtInf.getUstrds().add(painTransaction.getUstrdRemInf());
		transaction.setRmtInf(rmtInf);

		return transaction;
	}

}
