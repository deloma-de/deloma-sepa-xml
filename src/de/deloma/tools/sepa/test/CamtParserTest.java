package de.deloma.tools.sepa.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.deloma.tools.sepa.camt.CamtParser;
import de.deloma.tools.sepa.camt.CamtParser.CAMTTYPE;

/**
 * Unit tests for {@link CamtParser}
 *
 * @author Azahar Hossain
 *
 *         TODO: create a suite to test multiple tests at once
 */
@RunWith(value = Parameterized.class)
public class CamtParserTest
{

	private CamtParser parser;

	@Parameter(0)
	public static String TEST_FILE_URI;

	@Parameter(1)
	public static CAMTTYPE type;

	/*-- Test with multiple files at the same time -- */
	@Parameters
	public static Collection<Object[]> data()
	{
		return Arrays.asList(new Object[][]
		{
			{
				"H:\\Test\\Parser\\camt52\\prod\\DE12345678912345678901\\2023.02.14.xml", CAMTTYPE.CAMT52_001_08
			}
		});
	}

	@Before
	public void setUp() throws Exception
	{

		this.parser = new CamtParser();
	}

	@Test
	public void testParse()
	{
		// camt52v2.testReadCamt52();
		// camt52v8.testParse();
	}

	@Test
	public void testGetCamtTypeFromStream()
	{

		InputStream is;
		try
		{
			is = new FileInputStream(CamtParserTest.TEST_FILE_URI);
			final CAMTTYPE actualType = this.parser.getCamtTypeFromStream(is);

			Assert.assertEquals(CamtParserTest.type, actualType);

		}
		catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testGetCamtTypeFromXml()
	{
		String xml;
		try
		{
			xml = IOUtils.toString(new FileInputStream(CamtParserTest.TEST_FILE_URI));
			final CAMTTYPE actualType = this.parser.getCamtTypeFromXml(xml);
			Assert.assertEquals(CamtParserTest.type, actualType);
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
