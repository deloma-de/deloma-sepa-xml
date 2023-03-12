/**
 *
 */
package de.deloma.tools.sepa.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.deloma.tools.sepa.camt.CamtParser;
import de.deloma.tools.sepa.camt.CamtParser.CAMTTYPE;
import de.deloma.tools.sepa.model.camt.camt5200108.AccountReport25;
import de.deloma.tools.sepa.model.camt.camt5200108.CashBalance8;
import de.deloma.tools.sepa.model.camt.camt5200108.Document;

/**
 * Unit test for {@link CamtParser}, for the
 * {@link de.deloma.tools.sepa.model.camt.camt5200108.Document} type
 *
 * @author Azahar Hossain
 *
 */
@RunWith(value = Parameterized.class)
public class Camt5200108Test implements Serializable
{

	private static final long serialVersionUID = 2106089224395768067L;

	private CamtParser camtparser;

	private Document actualDocument;

	/**
	 * To test the first actual report properties
	 */
	private static AccountReport25 ACTUAL_FIRST_REPORT;

	/*---- Expected properties in test ---*/

	@Parameter(0)
	public static String TEST_FILE_URI;

	@Parameter(1)
	public static CAMTTYPE type;

	@Parameter(2)
	public static String EXPECTED_MSGID;

	@Parameter(3)
	public static String EXPECTED_BANKACCOUNT_IBAN;

	@Parameter(4)
	public static String EXPECTED_BANKACCOUNT_BIC;

	@Parameter(5)
	public static String EXPECTED_BANKACCOUNT_GLAEUBIGER_ID;

	/*-- Test with multiple files at the same time -- */
	@Parameters
	public static Collection<Object[]> data()
	{
		return Arrays.asList(new Object[][]
		{
			{
				MockDataForTest.TEST_FOLDER + "camt52\\prod\\DE12345678912345678901\\2023.02.14.xml", CAMTTYPE.CAMT52_001_08,

				"camt52_20230216173506__ONLINEBA", "DE12345678912345678901", "SPKRDE33XXX", "DE888888888"

			},
		});
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.camtparser = new CamtParser(Camt5200108Test.type);
	}

	/**
	 * Test method for
	 * {@link de.deloma.tools.sepa.util.CamtParser#parse(java.io.InputStream)}.
	 */
	@Test
	public void testParse()
	{
		try
		{

			final InputStream is = new FileInputStream(Camt5200108Test.TEST_FILE_URI);
			this.actualDocument = Document.class.cast(this.camtparser.<Document> parse(is));

			// Document is readable and return an Object
			Assert.assertTrue(this.actualDocument != null);

			// Document.rpt is readable and return an Object
			Assert.assertTrue(this.actualDocument.getBkToCstmrAcctRpt() != null);

			// At list one report exists
			Assert.assertTrue(this.actualDocument.getBkToCstmrAcctRpt().getRpt().size() >= 1);

			// Get the first report
			if (this.actualDocument.getBkToCstmrAcctRpt().getRpt().get(0) != null)
				Camt5200108Test.ACTUAL_FIRST_REPORT = this.actualDocument.getBkToCstmrAcctRpt().getRpt().get(0);

			// GroupHeader exists
			Assert.assertTrue(this.actualDocument.getBkToCstmrAcctRpt().getGrpHdr() != null);

			// GroupHeader exists
			Assert.assertEquals(Camt5200108Test.EXPECTED_MSGID, this.actualDocument.getBkToCstmrAcctRpt().getGrpHdr().getMsgId());

			// ------ Document and File Header info ends ---//

			/*-- Report part --*/

			// Report Account must match
			this.showSuccessIfAccountInfoIsValid();

			this.showSuccessIfBalanceIsValid();

		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

	}

	/*---------- Helper methods ---*/

	private void showSuccessIfAccountInfoIsValid()
	{

		// IBAN
		Assert.assertEquals(Camt5200108Test.EXPECTED_BANKACCOUNT_IBAN, Camt5200108Test.ACTUAL_FIRST_REPORT.getAcct().getId().getIBAN());
		// BIC
		Assert.assertEquals(Camt5200108Test.EXPECTED_BANKACCOUNT_BIC, Camt5200108Test.ACTUAL_FIRST_REPORT.getAcct().getSvcr().getFinInstnId().getBICFI());
		// Glaeubiger-Id
		Assert.assertEquals(Camt5200108Test.EXPECTED_BANKACCOUNT_GLAEUBIGER_ID,
			Camt5200108Test.ACTUAL_FIRST_REPORT.getAcct().getSvcr().getFinInstnId().getOthr().getId());
	}

	private void showSuccessIfBalanceIsValid()
	{

		final List<CashBalance8> balances = Camt5200108Test.ACTUAL_FIRST_REPORT.getBal();
		Assert.assertTrue(balances != null && balances.size() == 2);

	}

}
