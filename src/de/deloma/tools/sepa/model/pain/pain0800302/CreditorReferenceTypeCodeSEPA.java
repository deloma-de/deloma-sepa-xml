
package de.deloma.tools.sepa.model.pain.pain0800302;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreditorReferenceTypeCodeSEPA complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreditorReferenceTypeCodeSEPA">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Cd" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.003.02}DocumentType3CodeSEPA"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreditorReferenceTypeCodeSEPA", propOrder = {
    "cd"
})
public class CreditorReferenceTypeCodeSEPA {

    @XmlElement(name = "Cd", required = true)
    @XmlSchemaType(name = "string")
    protected DocumentType3CodeSEPA cd;

    /**
     * Gets the value of the cd property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentType3CodeSEPA }
     *     
     */
    public DocumentType3CodeSEPA getCd() {
        return cd;
    }

    /**
     * Sets the value of the cd property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentType3CodeSEPA }
     *     
     */
    public void setCd(DocumentType3CodeSEPA value) {
        this.cd = value;
    }

}
