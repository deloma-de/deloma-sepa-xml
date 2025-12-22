package de.deloma.tools.sepa.pain;

/**
 *
 * @author Marco Janc
 */
public enum PainDocumentType
{
	PAIN00800302("pain.008.003.02", "urn:iso:std:iso:20022:tech:xsd:pain.008.003.02 pain.008.003.02.xsd",

		de.deloma.tools.sepa.model.pain.pain0800302.ObjectFactory.class),

	PAIN00800102("pain.008.001.02", "urn:iso:std:iso:20022:tech:xsd:pain.008.001.02 pain.008.001.02.xsd",
		de.deloma.tools.sepa.model.pain.pain0800102.ObjectFactory.class),

	PAIN00800108("pain.008.001.08", "urn:iso:std:iso:20022:tech:xsd:pain.008.001.08 pain.008.001.08.xsd",
		de.deloma.tools.sepa.model.pain.pain0800108.ObjectFactory.class);

	/**
	 * name of the type
	 */
	private String name;

	/**
	 * xml schema location
	 */
	private final String schemaLocation;

	/**
	 * XML factory class
	 */
	private Class<?> factoryClass;

	private PainDocumentType(final String name, final String schemaLocation, final Class<?> factoryClass)
	{
		this.name = name;
		this.schemaLocation = schemaLocation;
		this.factoryClass = factoryClass;
	}

	public String getName()
	{
		return this.name;
	}

	public Class<?> getFactoryClass()
	{
		return this.factoryClass;
	}

	public String getSchemaLocation()
	{
		return this.schemaLocation;
	}

}