package de.deloma.tools.sepa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.deloma.tools.sepa.model.camt.camt5200102.AccountReport11;
import de.deloma.tools.sepa.model.camt.camt5200102.Document;
import de.deloma.tools.sepa.model.camt.camt5200102.GroupHeader42;
import de.deloma.tools.sepa.util.CamtParser;
import de.deloma.tools.sepa.util.CamtParser.CAMTTYPE;

/**
 * TODO: Instead of checking properties and functionality manually, all method
 * should be tested using JUnit test
 * 
 * In case in need, add parameterized Test 
 * @author Azahar Hossain (c) 
 * @since 2022
 *
 */
@RunWith(value= Parameterized.class)
public class Camt5200102Test implements Serializable {
	
	private static final long serialVersionUID = -6489214670802406150L;

	private CamtParser camtParser52;
	
	@Parameter(0)
	public static String TEST_FILE_URI; 
	
	@Parameter(1)
	public static boolean isFileCreated; 
	
	@Parameter(2)
	public static String expectedMsgId;
	
	@Parameter(3)
	public static int numReports;
	
	@Parameters
	public static Collection<Object[]>data(){
		return Arrays.asList(new Object[][]{
			{"H:\\Test\\Parser\\camt52\\2020.01.08.xml", true, "camt52_20200108173025__ONLINEBA",0}
		});
	}
	

    @Before
    public void setUp() throws Exception {
    	camtParser52 = new CamtParser(CAMTTYPE.CAMT52_001_02);
    }

	/**
	 * 
	 */
    @Test
	public void testReadCamt52() {
		try {


			InputStream fis = new FileInputStream(TEST_FILE_URI);
			
			Document document052 = (Document) camtParser52.parse(fis);

			// actual
			String actualMsgId = document052.getBkToCstmrAcctRpt().getGrpHdr().getMsgId();

			GroupHeader42 header = document052.getBkToCstmrAcctRpt().getGrpHdr();
			
			Date date = header.getCreDtTm().toGregorianCalendar().getTime();

			System.out.println(date);

			List<AccountReport11> accountreports = document052.getBkToCstmrAcctRpt().getRpt();

			
			//TODO: complete following check:  most important properties here 
			
			
			// msgId
			
			assertEquals(expectedMsgId, actualMsgId);
			
			// num of reports must not be zero 
			assertNotEquals(numReports,accountreports.size()); 
			

			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
