
package de.deloma.tools.sepa.model.pain.pain0800302;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FinancialInstitutionIdentificationSEPA2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FinancialInstitutionIdentificationSEPA2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Othr" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.003.02}RestrictedFinancialIdentificationSEPA"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FinancialInstitutionIdentificationSEPA2", propOrder = {
    "othr"
})
public class FinancialInstitutionIdentificationSEPA2 {

    @XmlElement(name = "Othr", required = true)
    protected RestrictedFinancialIdentificationSEPA othr;

    /**
     * Gets the value of the othr property.
     * 
     * @return
     *     possible object is
     *     {@link RestrictedFinancialIdentificationSEPA }
     *     
     */
    public RestrictedFinancialIdentificationSEPA getOthr() {
        return othr;
    }

    /**
     * Sets the value of the othr property.
     * 
     * @param value
     *     allowed object is
     *     {@link RestrictedFinancialIdentificationSEPA }
     *     
     */
    public void setOthr(RestrictedFinancialIdentificationSEPA value) {
        this.othr = value;
    }

}
