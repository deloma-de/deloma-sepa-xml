/**
 * 
 */
package de.deloma.tools.sepa.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.deloma.tools.sepa.model.camt.camt5200108.AccountReport25;
import de.deloma.tools.sepa.model.camt.camt5200108.BankToCustomerAccountReportV08;
import de.deloma.tools.sepa.model.camt.camt5200108.CashBalance8;
import de.deloma.tools.sepa.model.camt.camt5200108.Document;
import de.deloma.tools.sepa.util.CamtParser;
import de.deloma.tools.sepa.util.CamtParser.CAMTTYPE;

/**
 * Unit test for {@link CamtParser}, for the
 * {@link de.deloma.tools.sepa.model.camt.camt5200108.Document} type
 * 
 * @author Azahar Hossain
 *
 */
@RunWith(value = Parameterized.class)
public class Camt5200108Test implements Serializable {

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
	public static String EXPECTED_BANKACCOUNT_GLÄUBIGER_ID;

	/*-- Test with multiple files at the same time -- */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "H:\\Test\\Parser\\camt52\\prod\\DE12345678912345678901\\2023.02.14.xml", CAMTTYPE.CAMT52_001_08,
						
					"camt52_20230216173506__ONLINEBA", "DE12345678912345678901", "SPKRDE33XXX", "DE888888888" 
						
				}, });
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.camtparser = new CamtParser(type);
	}

	/**
	 * Test method for
	 * {@link de.deloma.tools.sepa.util.CamtParser#parse(java.io.InputStream)}.
	 */
	@Test
	public void testParse() {
		try {

			InputStream is = new FileInputStream(TEST_FILE_URI);
			actualDocument = Document.class.cast(camtparser.<Document>parse(is));

			// Document is readable and return an Object
			assertTrue(actualDocument != null);

			// Document.rpt is readable and return an Object
			assertTrue(actualDocument.getBkToCstmrAcctRpt() != null);

			// At list one report exists
			assertTrue(actualDocument.getBkToCstmrAcctRpt().getRpt().size() >= 1);

			// Get the first report
			if (actualDocument.getBkToCstmrAcctRpt().getRpt().get(0) != null)
				ACTUAL_FIRST_REPORT = actualDocument.getBkToCstmrAcctRpt().getRpt().get(0);

			// GroupHeader exists
			assertTrue(actualDocument.getBkToCstmrAcctRpt().getGrpHdr() != null);

			// GroupHeader exists
			assertEquals(EXPECTED_MSGID, actualDocument.getBkToCstmrAcctRpt().getGrpHdr().getMsgId());

			// ------ Document and File Header info ends ---//

			/*-- Report part --*/

			// Report Account must match
			this.showSuccessIfAccountInfoIsValid();

			this.showSuccessIfBalanceIsValid();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*---------- Helper methods ---*/

	private  void showSuccessIfAccountInfoIsValid() {

		// IBAN
		assertEquals(EXPECTED_BANKACCOUNT_IBAN, ACTUAL_FIRST_REPORT.getAcct().getId().getIBAN());
		// BIC
		assertEquals(EXPECTED_BANKACCOUNT_BIC, ACTUAL_FIRST_REPORT.getAcct().getSvcr().getFinInstnId().getBICFI());
		// Gläubiger-Id
		assertEquals(EXPECTED_BANKACCOUNT_GLÄUBIGER_ID,
				ACTUAL_FIRST_REPORT.getAcct().getSvcr().getFinInstnId().getOthr().getId());
	}

	private  void showSuccessIfBalanceIsValid() {
		
		List<CashBalance8> balances  = ACTUAL_FIRST_REPORT.getBal(); 
		assertTrue(balances !=null && balances.size() ==2 );
		
		

	}

}
