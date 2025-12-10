package de.deloma.tools.sepa.pain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.exception.PainParserException.ParserExceptionType;
import de.deloma.tools.sepa.model.pain.pain0800302.AccountIdentificationSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.ActiveOrHistoricCurrencyAndAmountSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.ActiveOrHistoricCurrencyCodeEUR;
import de.deloma.tools.sepa.model.pain.pain0800302.BranchAndFinancialInstitutionIdentificationSEPA3;
import de.deloma.tools.sepa.model.pain.pain0800302.CashAccountSEPA1;
import de.deloma.tools.sepa.model.pain.pain0800302.CashAccountSEPA2;
import de.deloma.tools.sepa.model.pain.pain0800302.ChargeBearerTypeSEPACode;
import de.deloma.tools.sepa.model.pain.pain0800302.CustomerDirectDebitInitiationV02;
import de.deloma.tools.sepa.model.pain.pain0800302.DirectDebitTransactionInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.DirectDebitTransactionSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.Document;
import de.deloma.tools.sepa.model.pain.pain0800302.FinancialInstitutionIdentificationSEPA3;
import de.deloma.tools.sepa.model.pain.pain0800302.GroupHeaderSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.IdentificationSchemeNameSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.LocalInstrumentSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.MandateRelatedInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.PartyIdentificationSEPA1;
import de.deloma.tools.sepa.model.pain.pain0800302.PartyIdentificationSEPA2;
import de.deloma.tools.sepa.model.pain.pain0800302.PartyIdentificationSEPA3;
import de.deloma.tools.sepa.model.pain.pain0800302.PartyIdentificationSEPA5;
import de.deloma.tools.sepa.model.pain.pain0800302.PartySEPA2;
import de.deloma.tools.sepa.model.pain.pain0800302.PaymentIdentificationSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.PaymentInstructionInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.PaymentMethod2Code;
import de.deloma.tools.sepa.model.pain.pain0800302.PaymentTypeInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.PersonIdentificationSEPA2;
import de.deloma.tools.sepa.model.pain.pain0800302.RemittanceInformationSEPA1Choice;
import de.deloma.tools.sepa.model.pain.pain0800302.RestrictedPersonIdentificationSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.RestrictedPersonIdentificationSchemeNameSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.SequenceType1Code;
import de.deloma.tools.sepa.model.pain.pain0800302.ServiceLevelSEPA;
import de.deloma.tools.sepa.pain.wrapper.CollectorPaymentInfoPain;
import de.deloma.tools.sepa.pain.wrapper.CreditorInfo;
import de.deloma.tools.sepa.pain.wrapper.GroupHeaderInfo;
import de.deloma.tools.sepa.pain.wrapper.PainTransaction;
import de.deloma.tools.sepa.util.ParserUtils;

/**
 * pain.008.003.02 document creator
 *
 * @author Azahar Hossain 2022 - 2023
 * @author Marco Janc 2023
 */
public class PainDocument00800302
{
	/**
	 * Creates a Document with document / page level info (e.g., msgId,
	 * creationDateTime and name of the initiator) as well as the collector
	 * level (a list of PaymentInstructionInformationSDD)
	 *
	 * @param groupHeaderInfo
	 * @param collectorPaymentInfos
	 *
	 * @return
	 *
	 * @throws PainParserException
	 *
	 * @see {@link PainParser#createPaymentInstructionInfoSDD()},<br>
	 *      {@link PainParserTest#testCreatePain00800302()}
	 */
	public static Document createDocument(final GroupHeaderInfo groupHeaderInfo, final List<CollectorPaymentInfoPain> collectorPaymentInfos)
		throws PainParserException
	{
		GroupHeaderInfo.validate(groupHeaderInfo);

		// create payment info
		final List<PaymentInstructionInformationSDD> paymentInfoList = new ArrayList<>(collectorPaymentInfos.size());
		for (final CollectorPaymentInfoPain collectorPaymentInfo : collectorPaymentInfos)
			paymentInfoList.add(PainDocument00800302.createPayInstrInf(collectorPaymentInfo));

		// Number of total transactions in all paymentInfos
		final int numTxs = paymentInfoList.stream().collect(Collectors.summingInt(p -> Integer.parseInt(p.getNbOfTxs())));
		if (numTxs < 0)
			throw new PainParserException(ParserExceptionType.GENERAL, "invalid number of transactions!");

		// There are many other fields for initiating party identification, but
		// only name is recommended.
		final PartyIdentificationSEPA1 partyIdentification = new PartyIdentificationSEPA1();
		partyIdentification.setNm(groupHeaderInfo.getInitiator());

		/*---------Group header: document info  --------*/
		final GroupHeaderSDD grpHdr = new GroupHeaderSDD();
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
	 * * creates a single {@link PaymentInstructionInformationSDD} entity with
	 * given informations
	 *
	 *
	 * All possible combinations:
	 *
	 * <ul>
	 *
	 *
	 * <li>CORE + FRST</li>
	 * <li>CORE + RECUR</li>
	 * <li>CORE + OOFF</li>
	 * <li>CORE + FNAL</li>
	 *
	 * <hr width="100" align="left">
	 * <li>B2B + FRST</li>
	 * <li>B2B+ RECUR</li>
	 * <li>B2B + OOFF</li>
	 * <li>B2B + FNAL</li>
	 *
	 * <hr width="100" align="left">
	 * <li>COR1 + FRST</li>
	 * <li>COR1 + RECUR</li>
	 * <li>COR1 + OOFF</li>
	 * <li>COR1 + FNAL</li>
	 *
	 * </ul>
	 *
	 * @param collectorPaymentInfo :
	 *            <code> To pass paymentinformations like paymentInfoid, collection date, Sepa codes (e.g, B2B, CORE, RCUR etc), as well as transactions </code>
	 *
	 * @return
	 *
	 * @throws PainParserException
	 */
	private static PaymentInstructionInformationSDD createPayInstrInf(final CollectorPaymentInfoPain collectorPaymentInfo) throws PainParserException
	{
		CollectorPaymentInfoPain.validate(collectorPaymentInfo);

		final CreditorInfo creditorInfo = collectorPaymentInfo.getCreditorInfo();

		// Tansactions
		final List<DirectDebitTransactionInformationSDD> transactions = collectorPaymentInfo.getTransactions().stream()
			.map(t -> PainDocument00800302.createTransaction(t)).collect(Collectors.toList());

		// Creditor info
		final PartyIdentificationSEPA5 creditor = new PartyIdentificationSEPA5();
		creditor.setNm(creditorInfo.getName());

		// -- PaymentTypeInfo
		final LocalInstrumentSEPA localInstrumentSEPA = new LocalInstrumentSEPA();
		localInstrumentSEPA.setCd(collectorPaymentInfo.getSepaLocalInstrumentCode().getValue());

		// --- SEPA ---
		final ServiceLevelSEPA serviceLevel = new ServiceLevelSEPA();
		serviceLevel.setCd("SEPA");

		final PaymentTypeInformationSDD paymentTypeInfo = new PaymentTypeInformationSDD();
		paymentTypeInfo.setSvcLvl(serviceLevel);
		paymentTypeInfo.setLclInstrm(localInstrumentSEPA);
		paymentTypeInfo.setSeqTp(SequenceType1Code.valueOf(collectorPaymentInfo.getSequenceTypeCode().toString()));

		// -- PaymentInstructionInfo
		final PaymentInstructionInformationSDD paymentInfo = new PaymentInstructionInformationSDD();

		// -- Creditor info
		// id ? = Referenz zur eindeutigen Identifizierung des Sammlers
		paymentInfo.setPmtInfId(collectorPaymentInfo.getPaymentInfoId());
		// constant DD
		paymentInfo.setPmtMtd(PaymentMethod2Code.DD);

		// Numer of transactions
		paymentInfo.setNbOfTxs(String.valueOf(transactions.size()));

		// Total Amount
		paymentInfo.setCtrlSum(collectorPaymentInfo.getTotalAmount());

		paymentInfo.setPmtTpInf(paymentTypeInfo);

		/*
		 * final Date colDate = new Date(2022 - 1900, Calendar.SEPTEMBER, 5);
		 */
		// collection date
		paymentInfo.setReqdColltnDt(collectorPaymentInfo.getCollectionDate());

		paymentInfo.setCdtr(creditor);

		// transactions
		paymentInfo.getDrctDbtTxInves().addAll(transactions);

		// Constant charge bearer: SLEV
		paymentInfo.setChrgBr(ChargeBearerTypeSEPACode.SLEV);

		/*
		 * Creditor Bank Info
		 */
		// IBAN or Bank id
		final CashAccountSEPA1 creditorBankinfoIBAN = new CashAccountSEPA1();
		final AccountIdentificationSEPA id = new AccountIdentificationSEPA();
		id.setIBAN(creditorInfo.getIban());
		creditorBankinfoIBAN.setId(id);
		paymentInfo.setCdtrAcct(creditorBankinfoIBAN);

		// BIC or Bank branch
		final BranchAndFinancialInstitutionIdentificationSEPA3 crdtrBankAndBranch = new BranchAndFinancialInstitutionIdentificationSEPA3();
		final FinancialInstitutionIdentificationSEPA3 crdtrBankId = new FinancialInstitutionIdentificationSEPA3();
		crdtrBankId.setBIC(creditorInfo.getBic());
		crdtrBankAndBranch.setFinInstnId(crdtrBankId);
		paymentInfo.setCdtrAgt(crdtrBankAndBranch);

		// Glaeubiger Id
		final PartyIdentificationSEPA3 cdtrSchmeId = new PartyIdentificationSEPA3();
		final PartySEPA2 schemeId = new PartySEPA2();
		final PersonIdentificationSEPA2 prvtId = new PersonIdentificationSEPA2();
		final RestrictedPersonIdentificationSEPA personIdentificationSEPA = new RestrictedPersonIdentificationSEPA();
		personIdentificationSEPA.setId(creditorInfo.getGlauebigerId());

		final RestrictedPersonIdentificationSchemeNameSEPA schemNm = new RestrictedPersonIdentificationSchemeNameSEPA();
		schemNm.setPrtry(IdentificationSchemeNameSEPA.SEPA);
		personIdentificationSEPA.setSchmeNm(schemNm);
		prvtId.setOthr(personIdentificationSEPA);
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
	 * @return DirectDebitTransactionInformationSDD object
	 */
	private static DirectDebitTransactionInformationSDD createTransaction(final PainTransaction painTransaction)
	{
		final DirectDebitTransactionInformationSDD transaction = new DirectDebitTransactionInformationSDD();

		// pmtId: EndtoEnd
		final PaymentIdentificationSEPA pmtId = new PaymentIdentificationSEPA();
		pmtId.setEndToEndId(painTransaction.getEndToEndId());
		transaction.setPmtId(pmtId);

		// Amount
		final ActiveOrHistoricCurrencyAndAmountSEPA amountSEPA = new ActiveOrHistoricCurrencyAndAmountSEPA();
		amountSEPA.setValue(painTransaction.getAmount());
		amountSEPA.setCcy(ActiveOrHistoricCurrencyCodeEUR.EUR);
		transaction.setInstdAmt(amountSEPA);

		// Debitor name
		final PartyIdentificationSEPA2 dbtr = new PartyIdentificationSEPA2();
		dbtr.setNm(painTransaction.getDbtrName());
		transaction.setDbtr(dbtr);

		// Debitor Bank Account

		// IBAN
		final CashAccountSEPA2 dbtrAcct = new CashAccountSEPA2();
		final AccountIdentificationSEPA dbtrAccId = new AccountIdentificationSEPA();
		dbtrAccId.setIBAN(painTransaction.getDbtrIban());
		dbtrAcct.setId(dbtrAccId);
		transaction.setDbtrAcct(dbtrAcct);

		// BIC
		final BranchAndFinancialInstitutionIdentificationSEPA3 dbtrBicInf = new BranchAndFinancialInstitutionIdentificationSEPA3();

		final FinancialInstitutionIdentificationSEPA3 dbtrBankInfo = new FinancialInstitutionIdentificationSEPA3();

		dbtrBankInfo.setBIC(painTransaction.getDbtrBic());

		dbtrBicInf.setFinInstnId(dbtrBankInfo);
		transaction.setDbtrAgt(dbtrBicInf);

		// mandate
		final DirectDebitTransactionSDD ddtxValue = new DirectDebitTransactionSDD();
		final MandateRelatedInformationSDD mandateInfo = new MandateRelatedInformationSDD();

		// Mandate Id
		mandateInfo.setMndtId(painTransaction.getMandateId());

		// date of signature
		mandateInfo.setDtOfSgntr(ParserUtils.dateToXmlGregorianNoOffset(painTransaction.getDtOfSgntr()));

		mandateInfo.setAmdmntInd(false);
		ddtxValue.setMndtRltdInf(mandateInfo);
		transaction.setDrctDbtTx(ddtxValue);

		// Debitor other name
		final PartyIdentificationSEPA1 ultDbtrNm = new PartyIdentificationSEPA1();
		ultDbtrNm.setNm(painTransaction.getUltDbtrName());
		transaction.setUltmtDbtr(ultDbtrNm);

		// Verbindungszweck = RechnungsNr.
		final RemittanceInformationSEPA1Choice rmtInf = new RemittanceInformationSEPA1Choice();
		rmtInf.setUstrd(painTransaction.getUstrdRemInf());
		transaction.setRmtInf(rmtInf);

		return transaction;
	}
}
