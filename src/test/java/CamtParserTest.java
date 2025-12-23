
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
 * Unit tests for {@link CamtParser}.
 *
 * Test files use test ibans from: https://ibanvalidieren.de/beispiele.html
 *
 * @author Azahar Hossain
 * @author Marco Janc
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
		final List<Object[]> data = new LinkedList<Object[]>();
		data.add(new Object[]
		{
			"/camt52/2020-01-08.xml", CAMTTYPE.CAMT52_001_02
		});

		data.add(new Object[]
		{
			"/camt52/2025-12-19.xml", CAMTTYPE.CAMT52_001_08
		});

		return data;
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

	public static InputStream getFile(final String filePath)
	{
		return CamtParserTest.class.getResourceAsStream(filePath);
	}

	@Test
	public void testGetCamtTypeFromStream()
	{

		InputStream is;
		try
		{
			is = CamtParserTest.getFile(CamtParserTest.TEST_FILE_URI);
			final CAMTTYPE actualType = CamtParser.getCamtTypeFromStream(is);

			Assert.assertEquals(CamtParserTest.type, actualType);

		}
		catch (final Exception e)
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
			final InputStream is = CamtParserTest.getFile(CamtParserTest.TEST_FILE_URI);
			xml = IOUtils.toString(is);
			final CAMTTYPE actualType = CamtParser.getCamtTypeFromXml(xml);
			Assert.assertEquals(CamtParserTest.type, actualType);
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
