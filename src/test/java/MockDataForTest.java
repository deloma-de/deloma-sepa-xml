package test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.deloma.tools.sepa.model.camt.camt5200102.AccountIdentification4Choice;
import de.deloma.tools.sepa.model.camt.camt5200102.AccountReport11;
import de.deloma.tools.sepa.model.camt.camt5200102.ActiveOrHistoricCurrencyAndAmount;
import de.deloma.tools.sepa.model.camt.camt5200102.AmountAndCurrencyExchange3;
import de.deloma.tools.sepa.model.camt.camt5200102.AmountAndCurrencyExchangeDetails3;
import de.deloma.tools.sepa.model.camt.camt5200102.BankTransactionCodeStructure4;
import de.deloma.tools.sepa.model.camt.camt5200102.CashAccount16;
import de.deloma.tools.sepa.model.camt.camt5200102.CreditDebitCode;
import de.deloma.tools.sepa.model.camt.camt5200102.DateAndDateTimeChoice;
import de.deloma.tools.sepa.model.camt.camt5200102.EntryDetails1;
import de.deloma.tools.sepa.model.camt.camt5200102.EntryStatus2Code;
import de.deloma.tools.sepa.model.camt.camt5200102.EntryTransaction2;
import de.deloma.tools.sepa.model.camt.camt5200102.PartyIdentification32;
import de.deloma.tools.sepa.model.camt.camt5200102.ProprietaryBankTransactionCodeStructure1;
import de.deloma.tools.sepa.model.camt.camt5200102.RemittanceInformation5;
import de.deloma.tools.sepa.model.camt.camt5200102.ReportEntry2;
import de.deloma.tools.sepa.model.camt.camt5200102.TransactionParty2;
import de.deloma.tools.sepa.model.camt.camt5200102.TransactionReferences2;
import de.deloma.tools.sepa.model.pain.pain0800302.AccountIdentificationSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.BranchAndFinancialInstitutionIdentificationSEPA3;
import de.deloma.tools.sepa.model.pain.pain0800302.CashAccountSEPA1;
import de.deloma.tools.sepa.model.pain.pain0800302.ChargeBearerTypeSEPACode;
import de.deloma.tools.sepa.model.pain.pain0800302.CustomerDirectDebitInitiationV02;
import de.deloma.tools.sepa.model.pain.pain0800302.DirectDebitTransactionInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.Document;
import de.deloma.tools.sepa.model.pain.pain0800302.FinancialInstitutionIdentificationSEPA3;
import de.deloma.tools.sepa.model.pain.pain0800302.GroupHeaderSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.IdentificationSchemeNameSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.LocalInstrumentSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.PartyIdentificationSEPA1;
import de.deloma.tools.sepa.model.pain.pain0800302.PartyIdentificationSEPA3;
import de.deloma.tools.sepa.model.pain.pain0800302.PartyIdentificationSEPA5;
import de.deloma.tools.sepa.model.pain.pain0800302.PartySEPA2;
import de.deloma.tools.sepa.model.pain.pain0800302.PaymentInstructionInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.PaymentMethod2Code;
import de.deloma.tools.sepa.model.pain.pain0800302.PaymentTypeInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.PersonIdentificationSEPA2;
import de.deloma.tools.sepa.model.pain.pain0800302.RestrictedPersonIdentificationSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.RestrictedPersonIdentificationSchemeNameSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.SequenceType1Code;
import de.deloma.tools.sepa.model.pain.pain0800302.ServiceLevelSEPA;
import de.deloma.tools.sepa.util.ParserUtils;

public class MockDataForTest {
	
	/**
	 * TODO make system param
	 */
	public static String TEST_FOLDER = "H:\\Test\\bank\\";
	
	
	private static String FILEPATH = "";
	private static String ACTUAL_MSGID = "";
	private static String EXPECTED_MSGID = "";

	private static TimeZone TIME_ZONE_LOCALE = TimeZone.getDefault();

	private static List<AccountReport11> ACCOUNT_REPORTS_11 = new ArrayList<AccountReport11>();
	
	private static Document populateDocumentDataPain00800302() {

		Document document = new Document();

		CustomerDirectDebitInitiationV02 directDebitInitiationV02 = new CustomerDirectDebitInitiationV02();

		/* Group Header */
		GroupHeaderSDD groupHeaderSDD = new GroupHeaderSDD();
		groupHeaderSDD.setMsgId("Msg-ID");
		// CredtTm
		groupHeaderSDD.setCreDtTm(
				ParserUtils.dateToXMLGregorianCalendar(new Date(2010, 12, 21, 9, 30, 47), TIME_ZONE_LOCALE));
		groupHeaderSDD.setCtrlSum(null);
		PartyIdentificationSEPA1 partyIdentificationSEPA1 = new PartyIdentificationSEPA1();
		partyIdentificationSEPA1.setNm("Initiator Name");
		groupHeaderSDD.setInitgPty(partyIdentificationSEPA1);
		directDebitInitiationV02.setGrpHdr(groupHeaderSDD);

		/* PaymentInf */
		PaymentInstructionInformationSDD pmtInf = new PaymentInstructionInformationSDD();

		pmtInf.setPmtInfId("Payment-ID");
		pmtInf.setPmtMtd(PaymentMethod2Code.DD);
		pmtInf.setNbOfTxs("2");
		BigDecimal cntrlSum = new BigDecimal(6655.86);
		cntrlSum = cntrlSum.setScale(2, RoundingMode.CEILING);
		pmtInf.setCtrlSum(cntrlSum);

		PaymentTypeInformationSDD paymentTypeInformationSDD = new PaymentTypeInformationSDD();
		ServiceLevelSEPA serviceLevelSEPA = new ServiceLevelSEPA();
		serviceLevelSEPA.setCd("SEPA");
		paymentTypeInformationSDD.setSvcLvl(serviceLevelSEPA);
		LocalInstrumentSEPA instrumentSEPA = new LocalInstrumentSEPA();

		/**
		 * normal direct debit : LOCAL_INSTRUMENT_CORE_DIRECT_DEBIT = 'CORE';
		 * urgent direct debit : LOCAL_INSTRUMENT_CORE_DIRECT_DEBIT_D_1 =
		 * 'COR1'; business direct debit : LOCAL_INSTRUMENT_BUSINESS_2_BUSINESS
		 * = 'B2B';
		 */

		instrumentSEPA.setCd("CORE");

		paymentTypeInformationSDD.setLclInstrm(instrumentSEPA);
		paymentTypeInformationSDD.setSeqTp(SequenceType1Code.FRST);

		pmtInf.setReqdColltnDt(ParserUtils.dateToXMLGregorianCalendar(new Date(2010, 12 - 1, 03), TIME_ZONE_LOCALE));

		// Creditor info
		PartyIdentificationSEPA5 creditor = new PartyIdentificationSEPA5();
		creditor.setNm("Creditor Name");
		pmtInf.setCdtr(creditor);

		/*
		 * Creditor Bank Info
		 */
		// IBAN or Bank id
		CashAccountSEPA1 creditorBankinfoIBAN = new CashAccountSEPA1();
		AccountIdentificationSEPA id = new AccountIdentificationSEPA();
		id.setIBAN("DE87200500001234567890");
		creditorBankinfoIBAN.setId(id);
		pmtInf.setCdtrAcct(creditorBankinfoIBAN);

		// BIC or Bank branch
		BranchAndFinancialInstitutionIdentificationSEPA3 crdtrBankAndBranch = new BranchAndFinancialInstitutionIdentificationSEPA3();
		FinancialInstitutionIdentificationSEPA3 crdtrBankId = new FinancialInstitutionIdentificationSEPA3();
		crdtrBankId.setBIC("BANKDEFFXXX");
		crdtrBankAndBranch.setFinInstnId(crdtrBankId);
		pmtInf.setCdtrAgt(crdtrBankAndBranch);

		// Constant charge bearer: SLEV
		pmtInf.setChrgBr(ChargeBearerTypeSEPACode.SLEV);

		// Gläubiger Id
		PartyIdentificationSEPA3 cdtrSchmeId = new PartyIdentificationSEPA3();
		PartySEPA2 schemeId = new PartySEPA2();
		PersonIdentificationSEPA2 prvtId = new PersonIdentificationSEPA2();
		RestrictedPersonIdentificationSEPA personIdentificationSEPA = new RestrictedPersonIdentificationSEPA();
		personIdentificationSEPA.setId("DE00ZZZ00099999999");

		RestrictedPersonIdentificationSchemeNameSEPA schemNm = new RestrictedPersonIdentificationSchemeNameSEPA();
		schemNm.setPrtry(IdentificationSchemeNameSEPA.SEPA);
		personIdentificationSEPA.setSchmeNm(schemNm);

		prvtId.setOthr(personIdentificationSEPA);
		schemeId.setPrvtId(prvtId);
		cdtrSchmeId.setId(schemeId);
		pmtInf.setCdtrSchmeId(cdtrSchmeId);

		// set transactions:
		DirectDebitTransactionInformationSDD transaction1 = new DirectDebitTransactionInformationSDD();

		pmtInf.getDrctDbtTxInves().add(transaction1);

		pmtInf.setPmtTpInf(paymentTypeInformationSDD);
		directDebitInitiationV02.getPmtInves().add(pmtInf);

		document.setCstmrDrctDbtInitn(directDebitInitiationV02);

		return document;
	}
	
	/**
	 * @param accId
	 * @return
	 */
	private static ReportEntry2 createEntry(AccountIdentification4Choice accId) {
		ReportEntry2 entry = new ReportEntry2();
		ActiveOrHistoricCurrencyAndAmount currenyAndAmount = new ActiveOrHistoricCurrencyAndAmount(); // Amt
		currenyAndAmount.setCcy("EUR");
		currenyAndAmount.setValue(new BigDecimal(1.10));
		entry.setAmt(currenyAndAmount);

		entry.setCdtDbtInd(CreditDebitCode.DBIT);
		entry.setSts(EntryStatus2Code.BOOK);
		// Booking date
		DateAndDateTimeChoice bookingDate = new DateAndDateTimeChoice();
		bookingDate.setDt(ParserUtils.dateToXMLGregorianCalendar(new Date(2013, 12, 30), TIME_ZONE_LOCALE));
		entry.setBookgDt(bookingDate);
		// Value date
		DateAndDateTimeChoice valueDate = new DateAndDateTimeChoice();
		valueDate.setDt(ParserUtils.dateToXMLGregorianCalendar(new Date(2013, 12, 30), TIME_ZONE_LOCALE));
		entry.setValDt(valueDate);
		entry.setAcctSvcrRef("2013122812211780000");

		/* EntryDetails: Transactions */
		EntryDetails1 ntryDetails = new EntryDetails1();
		EntryTransaction2 txDetails1 = createTransaction(accId);

		/*--------END OF TRANSACTION DETAILS 1------*/

		// EntryTransaction2 txDetails2 = new EntryTransaction2(); // TxDtls 2
		ntryDetails.getTxDtls().add(txDetails1);

		entry.getNtryDtls().add(ntryDetails);
		return entry;
	}

	/**
	 * @param accId
	 * @return
	 * 
	 * @author Marco Janc
	 */
	private static EntryTransaction2 createTransaction(AccountIdentification4Choice accId) {
		// Tx 1:
		EntryTransaction2 txDetails1 = new EntryTransaction2(); // TxDtls 1

		// refs
		TransactionReferences2 refs = new TransactionReferences2();
		refs.setMsgId("STZV-Msg28122013-11:29");
		refs.setEndToEndId("STZV-EtE28122013-11:29-1");
		txDetails1.setRefs(refs);

		// AmtDtls
		AmountAndCurrencyExchange3 amtDetails = new AmountAndCurrencyExchange3();
		AmountAndCurrencyExchangeDetails3 txAmt = new AmountAndCurrencyExchangeDetails3();
		ActiveOrHistoricCurrencyAndAmount tx1CurrenyAndAmount = new ActiveOrHistoricCurrencyAndAmount(); // Amt
		tx1CurrenyAndAmount.setCcy("EUR");
		tx1CurrenyAndAmount.setValue(new BigDecimal(0.60));
		txAmt.setAmt(tx1CurrenyAndAmount);
		amtDetails.setTxAmt(txAmt);
		txDetails1.setAmtDtls(amtDetails);
		// BkTxCd
		BankTransactionCodeStructure4 bkTxCdStruct = new BankTransactionCodeStructure4();
		ProprietaryBankTransactionCodeStructure1 propTry = new ProprietaryBankTransactionCodeStructure1();
		propTry.setCd("NMSC+201");
		propTry.setIssr("ZKA");
		bkTxCdStruct.setPrtry(propTry);
		txDetails1.setBkTxCd(bkTxCdStruct);

		// RltdPties
		TransactionParty2 txParties = new TransactionParty2();
		// Dbtr
		PartyIdentification32 dbtrInfo = new PartyIdentification32();
		dbtrInfo.setNm("Testkonto Nummer 2");
		txParties.setDbtr(dbtrInfo); // Dbtr
		CashAccount16 dbtrAcct = new CashAccount16();
		accId.setIBAN("DE14740618130000033626");
		dbtrAcct.setId(accId);
		txParties.setDbtrAcct(dbtrAcct); // DbtrAcct
		dbtrInfo.setNm("keine Information vorhanden"); // UltmtDbtr
		txParties.setUltmtDbtr(dbtrInfo);

		// Cdtr
		PartyIdentification32 cdtrInfo = new PartyIdentification32();
		cdtrInfo.setNm("Testkonto Nummer 1");
		txParties.setDbtr(cdtrInfo); // Dbtr
		CashAccount16 CdtrAcct = new CashAccount16();
		accId.setIBAN("DE58740618130100033626");
		dbtrAcct.setId(accId); // Creditor account IBAN = same as Owner
		txParties.setCdtrAcct(CdtrAcct); // CdtrAcct
		cdtrInfo.setNm("test konto 1"); // UltmtCdtr
		txParties.setUltmtCdtr(cdtrInfo);

		txDetails1.setRltdPties(txParties);
		RemittanceInformation5 rmtInf = new RemittanceInformation5();
		rmtInf.getUstrds().add("Sammler aus Testknto 2 Zweite Ueberweisung TAN:704515");
		txDetails1.setRmtInf(rmtInf);
		return txDetails1;
	}
	
	
}
