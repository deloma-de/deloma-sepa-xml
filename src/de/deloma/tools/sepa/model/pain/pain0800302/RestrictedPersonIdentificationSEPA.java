
package de.deloma.tools.sepa.model.pain.pain0800302;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RestrictedPersonIdentificationSEPA complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RestrictedPersonIdentificationSEPA">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.003.02}RestrictedPersonIdentifierSEPA"/>
 *         &lt;element name="SchmeNm" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.003.02}RestrictedPersonIdentificationSchemeNameSEPA"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RestrictedPersonIdentificationSEPA", propOrder = {
    "id",
    "schmeNm"
})
public class RestrictedPersonIdentificationSEPA {

    @XmlElement(name = "Id", required = true)
    protected String id;
    @XmlElement(name = "SchmeNm", required = true)
    protected RestrictedPersonIdentificationSchemeNameSEPA schmeNm;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the schmeNm property.
     * 
     * @return
     *     possible object is
     *     {@link RestrictedPersonIdentificationSchemeNameSEPA }
     *     
     */
    public RestrictedPersonIdentificationSchemeNameSEPA getSchmeNm() {
        return schmeNm;
    }

    /**
     * Sets the value of the schmeNm property.
     * 
     * @param value
     *     allowed object is
     *     {@link RestrictedPersonIdentificationSchemeNameSEPA }
     *     
     */
    public void setSchmeNm(RestrictedPersonIdentificationSchemeNameSEPA value) {
        this.schmeNm = value;
    }

}
