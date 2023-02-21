package de.deloma.tools.sepa.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.deloma.tools.sepa.util.CamtParser;
import de.deloma.tools.sepa.util.CamtParser.CAMTTYPE;
/**
 *  Unit tests for {@link CamtParser}
 * @author Azahar Hossain
 *
 * TODO: create a suite to test multiple tests at once
 */
@RunWith(value = Parameterized.class)
public class CamtParserTest {

	private CamtParser parser;


	@Parameter(0)
	public static String TEST_FILE_URI;

	@Parameter(1)
	public static CAMTTYPE type;

	/*-- Test with multiple files at the same time -- */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ "H:\\Test\\Parser\\camt52\\prod\\DE12345678912345678901\\2023.02.14.xml", CAMTTYPE.CAMT52_001_08 } });
	}

	@Before
	public void setUp() throws Exception {

		parser = new CamtParser();
	}

	@Test
	public void testParse() {
//		camt52v2.testReadCamt52();
//		camt52v8.testParse();
	}

	@Test
	public void testGetCamtTypeFromStream() {
		
		InputStream is;
		try {
			is = new FileInputStream(TEST_FILE_URI);
			CAMTTYPE actualType = parser.getCamtTypeFromStream(is);
			
			assertEquals(type, actualType); 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

	@Test
	public void testGetCamtTypeFromXml() {
		String xml;
		try {
			xml = IOUtils.toString(new FileInputStream(TEST_FILE_URI));
			CAMTTYPE actualType = parser.getCamtTypeFromXml(xml);
			assertEquals(type, actualType); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
