package de.deloma.tools.sepa.pain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import de.deloma.tools.sepa.model.pain.pain0800302.ServiceLevelSEPA;
import de.deloma.tools.sepa.pain.wrapper.CollectorPaymentInfoPain0080302;
import de.deloma.tools.sepa.pain.wrapper.CreditorInfo;
import de.deloma.tools.sepa.pain.wrapper.GroupHeaderInfo;
import de.deloma.tools.sepa.util.PainParser;
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
	 * creates a single transaction for the given parameter informations
	 * 
	 * @param endToEndId
	 * @param amount
	 * @param dbtrName
	 * @param dbtrIban
	 * @param dbtrBic
	 * @param mandateId
	 * @param dtOfSgntr
	 * @param ultDbtrName
	 * @param ustrdRemInf
	 * @return DirectDebitTransactionInformationSDD object
	 */
	public static DirectDebitTransactionInformationSDD createTransaction(String endToEndId, BigDecimal amount,
			String dbtrName, String dbtrIban, String dbtrBic, String mandateId, Date dtOfSgntr, String ultDbtrName,
			String ustrdRemInf) {
		final DirectDebitTransactionInformationSDD transaction = new DirectDebitTransactionInformationSDD();

		// pmtId: EndtoEnd
		final PaymentIdentificationSEPA pmtId = new PaymentIdentificationSEPA();
		pmtId.setEndToEndId(endToEndId);
		transaction.setPmtId(pmtId);

		// Amount
		final ActiveOrHistoricCurrencyAndAmountSEPA amountSEPA = new ActiveOrHistoricCurrencyAndAmountSEPA();
		amountSEPA.setValue(amount);
		amountSEPA.setCcy(ActiveOrHistoricCurrencyCodeEUR.EUR);
		transaction.setInstdAmt(amountSEPA);

		// Debitor name
		final PartyIdentificationSEPA2 dbtr = new PartyIdentificationSEPA2();
		dbtr.setNm(dbtrName);
		transaction.setDbtr(dbtr);

		// Retrieve Bank Account by ShopBusiness id;

		// IBAN
		final CashAccountSEPA2 dbtrAcct = new CashAccountSEPA2();
		final AccountIdentificationSEPA dbtrAccId = new AccountIdentificationSEPA();
		dbtrAccId.setIBAN(dbtrIban);
		dbtrAcct.setId(dbtrAccId);
		transaction.setDbtrAcct(dbtrAcct);

		// BIC
		final BranchAndFinancialInstitutionIdentificationSEPA3 dbtrBicInf = new BranchAndFinancialInstitutionIdentificationSEPA3();

		final FinancialInstitutionIdentificationSEPA3 dbtrBankInfo = new FinancialInstitutionIdentificationSEPA3();

		dbtrBankInfo.setBIC(dbtrBic);

		dbtrBicInf.setFinInstnId(dbtrBankInfo);
		transaction.setDbtrAgt(dbtrBicInf);

		final DirectDebitTransactionSDD ddtxValue = new DirectDebitTransactionSDD();
		final MandateRelatedInformationSDD mandateInfo = new MandateRelatedInformationSDD();

		// Mandate Id
		mandateInfo.setMndtId(mandateId);

		// date of signature
		mandateInfo.setDtOfSgntr(ParserUtils.dateToXmlGregorianNoOffset(dtOfSgntr));

		mandateInfo.setAmdmntInd(false);
		ddtxValue.setMndtRltdInf(mandateInfo);
		transaction.setDrctDbtTx(ddtxValue);
		// Debitor other name
		final PartyIdentificationSEPA1 ultDbtrNm = new PartyIdentificationSEPA1();

		ultDbtrNm.setNm(ultDbtrName);

		transaction.setUltmtDbtr(ultDbtrNm);
		final RemittanceInformationSEPA1Choice rmtInf = new RemittanceInformationSEPA1Choice();
		// Verbindungszweck = RechnungsNr.
		rmtInf.setUstrd(ustrdRemInf);
		transaction.setRmtInf(rmtInf);
		return transaction;
	}

	/**
	 * Creates a Document with document / page level info (e.g., msgId,
	 * creationDateTime and name of the initiator) as well as the collector
	 * level (a list of PaymentInstructionInformationSDD)
	 * 
	 * @param msgId
	 * @param creationDateTime
	 * @param initiatorName
	 * @param paymentInfoList
	 * @return
	 * @throws PainParserException
	 * 
	 * @see {@link PainParser#createPaymentInstructionInfoSDD()},<br>
	 *      {@link PainParserTest#testCreatePain00800302()}
	 */
	public static Document createDocument(GroupHeaderInfo groupHeaderInfo,
			List<PaymentInstructionInformationSDD> paymentInfoList) throws PainParserException {

		Objects.requireNonNull(groupHeaderInfo, "groupHeaderInfo must not  be null");
		Objects.requireNonNull(paymentInfoList, "paymentInfoList must not  be null");

		final CustomerDirectDebitInitiationV02 ddIntiation = new CustomerDirectDebitInitiationV02();

		/*---------Group header: document info  --------*/
		final GroupHeaderSDD grpHdr = new GroupHeaderSDD();

		String msgId = groupHeaderInfo.getMsgId();

		if (msgId.length() > 35 | msgId.length() < 1)
			throw new PainParserException(ParserExceptionType.GENERAL, "Invalid msgId length!");

		grpHdr.setMsgId(groupHeaderInfo.getMsgId());

		// Number of total transactions in all paymentInfos
		int numTxs = 0;
		numTxs = paymentInfoList.stream().collect(Collectors.summingInt(p -> Integer.parseInt(p.getNbOfTxs())));

		if (numTxs < 0)
			throw new PainParserException(ParserExceptionType.GENERAL, "Invalid number of transactions!");

		grpHdr.setNbOfTxs(String.valueOf(numTxs));

		grpHdr.setCreDtTm(groupHeaderInfo.getCreationDateTime());

		// There are many other fields for initiating party identification, but
		// only name is recommended.

		String initiatorName = groupHeaderInfo.getInitiator();

		if (initiatorName.length() > 70 | msgId.length() < 1)
			throw new PainParserException(ParserExceptionType.GENERAL, "Invalid groupHeaderInfo.initiator length!");

		final PartyIdentificationSEPA1 identificationSEPA1 = new PartyIdentificationSEPA1();

		identificationSEPA1.setNm(initiatorName);
		grpHdr.setInitgPty(identificationSEPA1);

		ddIntiation.setGrpHdr(grpHdr);

		/*---------Payment Info--------*/

		ddIntiation.getPmtInf().addAll(paymentInfoList);
		
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
	 * @param creditorInfo
	 *            : <code> To pass creditor name, iban, bic etc info </code>
	 * @param collectorPaymentInfoPain
	 *            :
	 *            <code> To pass paymentinformations like paymentInfoid, collection date, Sepa codes (e.g, B2B, CORE, RCUR etc), as well as transactions </code>
	 * @return
	 * @throws PainParserException
	 */
	public static PaymentInstructionInformationSDD createPayInstrInf(CreditorInfo creditorInfo,
			CollectorPaymentInfoPain0080302 collectorPaymentInfoPain) throws PainParserException {

		Objects.requireNonNull(creditorInfo, "creditorInfo must not be null");
		Objects.requireNonNull(collectorPaymentInfoPain, "collectorPaymentInfo must not be null");
		
		creditorInfo.checkProperties();
		collectorPaymentInfoPain.checkProperties();

		final PaymentInstructionInformationSDD paymentInfo = new PaymentInstructionInformationSDD();

		// -- Creditor info
		// tenantid ? = Referenz zur eindeutigen Identifizierung des Sammlers
		paymentInfo.setPmtInfId(collectorPaymentInfoPain.getPaymentInfoId());
		// constant DD
		paymentInfo.setPmtMtd(PaymentMethod2Code.DD);

		// Numer of transactions
		paymentInfo.setNbOfTxs(String.valueOf(collectorPaymentInfoPain.getTransactions().size()));

		// Total Amount
		paymentInfo.setCtrlSum(collectorPaymentInfoPain.getTotalAmount());

		final PaymentTypeInformationSDD paymentTypeInfo = new PaymentTypeInformationSDD();

		// --- SEPA ---
		final ServiceLevelSEPA serviceLevel = new ServiceLevelSEPA();
		serviceLevel.setCd("SEPA");
		paymentTypeInfo.setSvcLvl(serviceLevel);

		final LocalInstrumentSEPA localInstrumentSEPA = new LocalInstrumentSEPA();
		//
		localInstrumentSEPA.setCd(collectorPaymentInfoPain.getSepaLocalInstrumentCode().getValue());
		paymentTypeInfo.setLclInstrm(localInstrumentSEPA);

		paymentTypeInfo.setSeqTp(collectorPaymentInfoPain.getSequenceTypeCode());
		paymentInfo.setPmtTpInf(paymentTypeInfo);

		/*
		 * final Date colDate = new Date(2022 - 1900, Calendar.SEPTEMBER, 5);
		 */
		// collection date
		paymentInfo.setReqdColltnDt(collectorPaymentInfoPain.getCollectionDate());
		// -- Creditor Bank account info

		// Creditor info
		final PartyIdentificationSEPA5 creditor = new PartyIdentificationSEPA5();
		creditor.setNm(creditorInfo.getName());
		paymentInfo.setCdtr(creditor);

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

		// Constant charge bearer: SLEV
		paymentInfo.setChrgBr(ChargeBearerTypeSEPACode.SLEV);

		// Gl�ubiger Id
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

		paymentInfo.getDrctDbtTxInf().addAll(collectorPaymentInfoPain.getTransactions());

		return paymentInfo;

	}
}