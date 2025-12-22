package test;

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

import de.deloma.tools.sepa.exception.PainParserException;
import de.deloma.tools.sepa.pain.PainDocumentType;
import de.deloma.tools.sepa.pain.PainParser;
import de.deloma.tools.sepa.pain.wrapper.CollectorPaymentInfoPain;
import de.deloma.tools.sepa.pain.wrapper.CreditorInfo;
import de.deloma.tools.sepa.pain.wrapper.GroupHeaderInfo;
import de.deloma.tools.sepa.pain.wrapper.PainTransaction;
import de.deloma.tools.sepa.pain.wrapper.SepaLocalInstrumentCode;
import de.deloma.tools.sepa.pain.wrapper.SequenceTypeCode;

/**
 * Pain file creation test
 *
 * @author Azahar Hossain
 * @author Marco Janc
 */
public class PainParserTest
{
	public static String TEST_FOLDER = "H:\\Test\\bank\\";

	public static void main(final String[] args)
	{
		final PainDocumentType type = PainDocumentType.PAIN00800108;

		final String filePath = PainParserTest.TEST_FOLDER + "pain008\\test-" + type.getName() + ".xml";

		PainParserTest.testCreatePainXml(type, filePath);
	}

	/**
	 * Tests the method
	 * {@link PainParser#createDocumentRaw(String, Date, String, List)} by
	 * creating a sample xml file out of mock data
	 *
	 * @throws PainParserException
	 */
	public static void testCreatePainXml(final PainDocumentType type, final String filePath)
	{

		// Document document = populateDocumentDataPain00800302();

		final Date creationDate = new Date();
		final String msgIdSuffix = "test-msgId-";

		final GroupHeaderInfo headerInfo = new GroupHeaderInfo(msgIdSuffix + new Date().toInstant().toString(),
			creationDate, "Initiator Name");

		try
		{
			final List<PainTransaction> transactions = new ArrayList<>();

			// create direct debit transactions as per invoices
			final PainTransaction transaction = PainParserTest.createTransaction();
			transactions.add(transaction);

			final String creditorName = "Creditor Name";
			final String creditorIban = "DE87200500001234567890";
			final String creditorBIC = "BANKDEFFXXX";
			final String glauebigerId = "DE00ZZZ00099999999";

			final CreditorInfo creditorInfo = new CreditorInfo(creditorName, creditorIban, creditorBIC, glauebigerId);

			final List<CollectorPaymentInfoPain> paymentInfoList = PainParserTest.getPayInfos(msgIdSuffix,
				transactions, creditorInfo);

			// cal.set(2023, 0, 16);

			final String xml = PainParser.createDocumentXml(type, headerInfo, paymentInfoList);

			final FileOutputStream os = new FileOutputStream(new File(filePath));
			os.write(xml.getBytes());
			os.close();
			System.out.println(xml);

		}
		catch (final IOException | PainParserException e)
		{
			e.printStackTrace();
		}

	}

	private static List<CollectorPaymentInfoPain> getPayInfos(final String msgIdSuffix,
		final List<PainTransaction> transactions, final CreditorInfo creditorInfo) throws PainParserException
	{
		// CORE + FRST
		String paymentInfoId = msgIdSuffix + "col-1-core-first";
		SepaLocalInstrumentCode sepaLocalInstrumentCode = SepaLocalInstrumentCode.CORE;
		SequenceTypeCode sequenceTypeCode = SequenceTypeCode.FRST;

		final Calendar cal = Calendar.getInstance();

		cal.set(2023, 2 - 1, 3);
		final Date colDate = cal.getTime();

		final CollectorPaymentInfoPain col1 = new CollectorPaymentInfoPain(creditorInfo, paymentInfoId,
			sepaLocalInstrumentCode, sequenceTypeCode, colDate, transactions);

		// CORE + RECUR
		paymentInfoId = msgIdSuffix + "col-2-core-recur";
		sequenceTypeCode = SequenceTypeCode.RCUR;

		// still the same creditor
		final CollectorPaymentInfoPain col2 = new CollectorPaymentInfoPain(creditorInfo, paymentInfoId,
			sepaLocalInstrumentCode, sequenceTypeCode, colDate, transactions);

		// B2B + FRST
		paymentInfoId = msgIdSuffix + "col-3-b2b-first";
		sepaLocalInstrumentCode = SepaLocalInstrumentCode.B2B;
		sequenceTypeCode = SequenceTypeCode.FRST;

		// still the same creditor
		final CollectorPaymentInfoPain col3a = new CollectorPaymentInfoPain(creditorInfo, paymentInfoId,
			sepaLocalInstrumentCode, sequenceTypeCode, colDate, transactions);

		// B2B + FRST
		paymentInfoId = msgIdSuffix + "col-3-b2b-recur";
		sepaLocalInstrumentCode = SepaLocalInstrumentCode.B2B;
		sequenceTypeCode = SequenceTypeCode.RCUR;

		final CollectorPaymentInfoPain col3b = new CollectorPaymentInfoPain(creditorInfo, paymentInfoId,
			sepaLocalInstrumentCode, sequenceTypeCode, colDate, transactions);

		// B2B + FNAL
		paymentInfoId = msgIdSuffix + "col-3-b2b-fnal";
		sepaLocalInstrumentCode = SepaLocalInstrumentCode.B2B;
		sequenceTypeCode = SequenceTypeCode.FNAL;

		final CollectorPaymentInfoPain col3c = new CollectorPaymentInfoPain(creditorInfo, paymentInfoId,
			sepaLocalInstrumentCode, sequenceTypeCode, colDate, transactions);

		return new ArrayList<>(Arrays.asList(col1, col2, col3a, col3b, col3c));
	}

	private static PainTransaction createTransaction()
	{
		final String endToEndId = "OriginatorID1235";
		final BigDecimal amount = new BigDecimal(112.72).setScale(2, RoundingMode.CEILING);
		final String dbtrName = "Debtor name";
		final String dbtrIban = "DE16200500001234567555";
		final String dbtrBic = "SPUEDE2UXXX";
		final String mandateId = "Mandate-Id";
		final Date dtOfSgntr = Date.from(Instant.now());
		final String ultDbtrNm = "Ultimate Debtor Name";
		final String ustrdRemInf = "Unstructured Remittance Information";

		return new PainTransaction(endToEndId, amount, dbtrName, dbtrIban, dbtrBic, mandateId, dtOfSgntr, ultDbtrNm,
			ustrdRemInf);
	}

}
