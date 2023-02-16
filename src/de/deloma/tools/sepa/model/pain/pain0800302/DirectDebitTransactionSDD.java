
package de.deloma.tools.sepa.model.pain.pain0800302;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirectDebitTransactionSDD complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectDebitTransactionSDD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MndtRltdInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.003.02}MandateRelatedInformationSDD"/>
 *         &lt;element name="CdtrSchmeId" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.003.02}PartyIdentificationSEPA3" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectDebitTransactionSDD", propOrder = {
    "mndtRltdInf",
    "cdtrSchmeId"
})
public class DirectDebitTransactionSDD {

    @XmlElement(name = "MndtRltdInf", required = true)
    protected MandateRelatedInformationSDD mndtRltdInf;
    @XmlElement(name = "CdtrSchmeId")
    protected PartyIdentificationSEPA3 cdtrSchmeId;

    /**
     * Gets the value of the mndtRltdInf property.
     * 
     * @return
     *     possible object is
     *     {@link MandateRelatedInformationSDD }
     *     
     */
    public MandateRelatedInformationSDD getMndtRltdInf() {
        return mndtRltdInf;
    }

    /**
     * Sets the value of the mndtRltdInf property.
     * 
     * @param value
     *     allowed object is
     *     {@link MandateRelatedInformationSDD }
     *     
     */
    public void setMndtRltdInf(MandateRelatedInformationSDD value) {
        this.mndtRltdInf = value;
    }

    /**
     * Gets the value of the cdtrSchmeId property.
     * 
     * @return
     *     possible object is
     *     {@link PartyIdentificationSEPA3 }
     *     
     */
    public PartyIdentificationSEPA3 getCdtrSchmeId() {
        return cdtrSchmeId;
    }

    /**
     * Sets the value of the cdtrSchmeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIdentificationSEPA3 }
     *     
     */
    public void setCdtrSchmeId(PartyIdentificationSEPA3 value) {
        this.cdtrSchmeId = value;
    }

}
