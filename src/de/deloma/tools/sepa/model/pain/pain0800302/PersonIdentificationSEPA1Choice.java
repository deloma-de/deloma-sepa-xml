
package de.deloma.tools.sepa.model.pain.pain0800302;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PersonIdentificationSEPA1Choice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonIdentificationSEPA1Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="DtAndPlcOfBirth" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.003.02}DateAndPlaceOfBirth"/>
 *           &lt;element name="Othr" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.003.02}GenericPersonIdentification1"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonIdentificationSEPA1Choice", propOrder = {
    "dtAndPlcOfBirth",
    "othr"
})
public class PersonIdentificationSEPA1Choice {

    @XmlElement(name = "DtAndPlcOfBirth")
    protected DateAndPlaceOfBirth dtAndPlcOfBirth;
    @XmlElement(name = "Othr")
    protected GenericPersonIdentification1 othr;

    /**
     * Gets the value of the dtAndPlcOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link DateAndPlaceOfBirth }
     *     
     */
    public DateAndPlaceOfBirth getDtAndPlcOfBirth() {
        return dtAndPlcOfBirth;
    }

    /**
     * Sets the value of the dtAndPlcOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateAndPlaceOfBirth }
     *     
     */
    public void setDtAndPlcOfBirth(DateAndPlaceOfBirth value) {
        this.dtAndPlcOfBirth = value;
    }

    /**
     * Gets the value of the othr property.
     * 
     * @return
     *     possible object is
     *     {@link GenericPersonIdentification1 }
     *     
     */
    public GenericPersonIdentification1 getOthr() {
        return othr;
    }

    /**
     * Sets the value of the othr property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenericPersonIdentification1 }
     *     
     */
    public void setOthr(GenericPersonIdentification1 value) {
        this.othr = value;
    }

}
