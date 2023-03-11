package de.deloma.tools.sepa.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.model.pain.pain0800302.AccountIdentificationSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.ActiveOrHistoricCurrencyAndAmountSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.ActiveOrHistoricCurrencyCodeEUR;
import de.deloma.tools.sepa.model.pain.pain0800302.BranchAndFinancialInstitutionIdentificationSEPA3;
import de.deloma.tools.sepa.model.pain.pain0800302.CashAccountSEPA2;
import de.deloma.tools.sepa.model.pain.pain0800302.DirectDebitTransactionInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.DirectDebitTransactionSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.Document;
import de.deloma.tools.sepa.model.pain.pain0800302.FinancialInstitutionIdentificationSEPA3;
import de.deloma.tools.sepa.model.pain.pain0800302.MandateRelatedInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.PartyIdentificationSEPA1;
import de.deloma.tools.sepa.model.pain.pain0800302.PartyIdentificationSEPA2;
import de.deloma.tools.sepa.model.pain.pain0800302.PaymentIdentificationSEPA;
import de.deloma.tools.sepa.model.pain.pain0800302.PaymentInstructionInformationSDD;
import de.deloma.tools.sepa.model.pain.pain0800302.RemittanceInformationSEPA1Choice;
import de.deloma.tools.sepa.model.pain.pain0800302.SequenceType1Code;
import de.deloma.tools.sepa.pain.PainDocument00800302;
import de.deloma.tools.sepa.pain.PainDocumentType;
import de.deloma.tools.sepa.pain.wrapper.CollectorPaymentInfoPain0080302;
import de.deloma.tools.sepa.pain.wrapper.CreditorInfo;
import de.deloma.tools.sepa.pain.wrapper.GroupHeaderInfo;
import de.deloma.tools.sepa.pain.wrapper.SepaLocalInstrumentCode;
import de.deloma.tools.sepa.util.PainParser;
import de.deloma.tools.sepa.util.ParserUtils;

public class PainParserTest {

	static String filePath = MockDataForTest.TEST_FOLDER + "pain008\\test_pain.008.003.02.xml";
	static String xsdFilePath = MockDataForTest.TEST_FOLDER + "\\xsd\\pain.008.003.02.xsd";
	static String schemaLocation = "urn:iso:std:iso:20022:tech:xsd:pain.008.003.02 pain.008.003.02.xsd";
	public static TimeZone TIME_ZONE_LOCALE = TimeZone.getDefault();
	public static TimeZone TIME_ZONE_UTC = TimeZone.getTimeZone("UTC");

	public static void main(String[] args) {
		testCreatePain00800302();
	}

	/**
	 * Tests the method
	 * {@link PainParser#createDocumentRaw(String, Date, String, List)} by
	 * creating a sample xml file out of mock data
	 * 
	 * @throws PainParserException
	 */
	public static void testCreatePain00800302() {

		// Document document = populateDocumentDataPain00800302();

		Date creationDate = new Date();
		String msgIdSuffix = "test-msgId-";
		GroupHeaderInfo headerInfo = new GroupHeaderInfo(msgIdSuffix + new Date().toInstant().toString(), creationDate,
				"Initiator Name");

		try {

			List<DirectDebitTransactionInformationSDD> transactions = new ArrayList<>();

			// create direct debit transactions as per invoices
			DirectDebitTransactionInformationSDD transaction = createTransaction();
			transactions.add(transaction);

			String creditorName = "Creditor Name";
			String creditorIban = "DE87200500001234567890";
			String creditorBIC = "BANKDEFFXXX";
			String glauebigerId = "DE00ZZZ00099999999";

			CreditorInfo creditorInfo = new CreditorInfo(creditorName, creditorIban, creditorBIC, glauebigerId);

			List<PaymentInstructionInformationSDD> paymentInfoList = getPayInfos(msgIdSuffix, transactions,
					creditorInfo);

			// cal.set(2023, 0, 16);

			Document document = PainDocument00800302.createDocument(headerInfo, paymentInfoList);

			FileOutputStream os = new FileOutputStream(new File(filePath));
			String xml = PainParser.createDocumentXml(filePath, PainDocumentType.PAIN00800302, document);
			os.write(xml.getBytes());
			os.close();
			System.out.println(xml);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (PainParserException e) {
			e.printStackTrace();
		}

	}

	private static List<PaymentInstructionInformationSDD> getPayInfos(String msgIdSuffix,

			List<DirectDebitTransactionInformationSDD> transactions, CreditorInfo creditorInfo)
			throws PainParserException {

		List<PaymentInstructionInformationSDD> paymentInfoList = new ArrayList<>();

		// CORE + FRST
		String paymentInfoId = msgIdSuffix + "col-1-core-first";
		SepaLocalInstrumentCode sepaLocalInstrumentCode = SepaLocalInstrumentCode.CORE;
		SequenceType1Code sequenceTypeCode = SequenceType1Code.FRST;

		Calendar cal = Calendar.getInstance();

		cal.set(2023, 2 - 1, 3);
		Date colDate = cal.getTime();

		PaymentInstructionInformationSDD col1 = PainDocument00800302.createPayInstrInf(creditorInfo,
				new CollectorPaymentInfoPain0080302(paymentInfoId, sepaLocalInstrumentCode, sequenceTypeCode, colDate,
						transactions));

		// CORE + RECUR
		paymentInfoId = msgIdSuffix + "col-2-core-recur";
		sequenceTypeCode = SequenceType1Code.RCUR;

		// still the same creditor
		PaymentInstructionInformationSDD col2 = PainDocument00800302.createPayInstrInf(creditorInfo,
				new CollectorPaymentInfoPain0080302(paymentInfoId, sepaLocalInstrumentCode, sequenceTypeCode, colDate,
						transactions));

		// B2B + FRST
		paymentInfoId = msgIdSuffix + "col-3-b2b-first";
		sepaLocalInstrumentCode = SepaLocalInstrumentCode.B2B;
		sequenceTypeCode = SequenceType1Code.FRST;

		// still the same creditor
		PaymentInstructionInformationSDD col3a = PainDocument00800302.createPayInstrInf(creditorInfo,
				new CollectorPaymentInfoPain0080302(paymentInfoId, sepaLocalInstrumentCode, sequenceTypeCode, colDate,
						transactions));

		// B2B + FRST
		paymentInfoId = msgIdSuffix + "col-3-b2b-recur";
		sepaLocalInstrumentCode = SepaLocalInstrumentCode.B2B;
		sequenceTypeCode = SequenceType1Code.RCUR;

		PaymentInstructionInformationSDD col3b = PainDocument00800302.createPayInstrInf(creditorInfo,
				new CollectorPaymentInfoPain0080302(paymentInfoId, sepaLocalInstrumentCode, sequenceTypeCode, colDate,
						transactions));

		// B2B + FNAL
		paymentInfoId = msgIdSuffix + "col-3-b2b-fnal";
		sepaLocalInstrumentCode = sepaLocalInstrumentCode.B2B;
		sequenceTypeCode = SequenceType1Code.FNAL;

		PaymentInstructionInformationSDD col3c = PainDocument00800302.createPayInstrInf(creditorInfo,
				new CollectorPaymentInfoPain0080302(paymentInfoId, sepaLocalInstrumentCode, sequenceTypeCode, colDate,
						transactions));

		paymentInfoList.addAll(Arrays.asList(col1, col2, col3a, col3b, col3c));
		return paymentInfoList;

	}

	private static DirectDebitTransactionInformationSDD createTransaction() {
		DirectDebitTransactionInformationSDD transaction1 = new DirectDebitTransactionInformationSDD();

		// pmtId: EndtoEnd
		PaymentIdentificationSEPA pmtId = new PaymentIdentificationSEPA();
		pmtId.setEndToEndId("OriginatorID1235");
		transaction1.setPmtId(pmtId);

		// Amount
		ActiveOrHistoricCurrencyAndAmountSEPA amountSEPA = new ActiveOrHistoricCurrencyAndAmountSEPA();
		amountSEPA.setValue(new BigDecimal(112.72).setScale(2, RoundingMode.CEILING));
		amountSEPA.setCcy(ActiveOrHistoricCurrencyCodeEUR.EUR);
		transaction1.setInstdAmt(amountSEPA);

		// Debitor name
		PartyIdentificationSEPA2 dbtr = new PartyIdentificationSEPA2();
		dbtr.setNm("Debtor name");
		transaction1.setDbtr(dbtr);

		// IBAN
		CashAccountSEPA2 dbtrAcct = new CashAccountSEPA2();
		AccountIdentificationSEPA dbtrAccId = new AccountIdentificationSEPA();
		dbtrAccId.setIBAN("DE16200500001234567555");
		dbtrAcct.setId(dbtrAccId);
		transaction1.setDbtrAcct(dbtrAcct);

		// BIC
		BranchAndFinancialInstitutionIdentificationSEPA3 dbtrBicInf = new BranchAndFinancialInstitutionIdentificationSEPA3();

		FinancialInstitutionIdentificationSEPA3 dbtrBankInfo = new FinancialInstitutionIdentificationSEPA3();

		dbtrBankInfo.setBIC("SPUEDE2UXXX");

		dbtrBicInf.setFinInstnId(dbtrBankInfo);
		transaction1.setDbtrAgt(dbtrBicInf);

		DirectDebitTransactionSDD ddtxValue = new DirectDebitTransactionSDD();
		MandateRelatedInformationSDD mandateInfo = new MandateRelatedInformationSDD();

		// Mandate Id
		mandateInfo.setMndtId("Mandate-Id");

		// date of signature
		mandateInfo.setDtOfSgntr(ParserUtils.dateToXmlGregorian(Date.from(Instant.now()), TIME_ZONE_UTC));

		mandateInfo.setAmdmntInd(false);
		ddtxValue.setMndtRltdInf(mandateInfo);
		transaction1.setDrctDbtTx(ddtxValue);
		// Debitor other name
		PartyIdentificationSEPA1 ultDbtrNm = new PartyIdentificationSEPA1();

		ultDbtrNm.setNm("Ultimate Debtor Name");

		transaction1.setUltmtDbtr(ultDbtrNm);
		RemittanceInformationSEPA1Choice rmtInf = new RemittanceInformationSEPA1Choice();
		rmtInf.setUstrd("Unstructured Remittance Information"); // Verbindungszweck
		transaction1.setRmtInf(rmtInf);
		return transaction1;
	}

}
