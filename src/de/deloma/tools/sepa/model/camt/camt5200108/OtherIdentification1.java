//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.02.20 at 04:39:57 PM CET 
//


package de.deloma.tools.sepa.model.camt.camt5200108;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OtherIdentification1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OtherIdentification1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.08}Max35Text"/>
 *         &lt;element name="Sfx" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.08}Max16Text" minOccurs="0"/>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.08}IdentificationSource3Choice"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OtherIdentification1", propOrder = {
    "id",
    "sfx",
    "tp"
})
public class OtherIdentification1 {

    @XmlElement(name = "Id", required = true)
    protected String id;
    @XmlElement(name = "Sfx")
    protected String sfx;
    @XmlElement(name = "Tp", required = true)
    protected IdentificationSource3Choice tp;

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
     * Gets the value of the sfx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSfx() {
        return sfx;
    }

    /**
     * Sets the value of the sfx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSfx(String value) {
        this.sfx = value;
    }

    /**
     * Gets the value of the tp property.
     * 
     * @return
     *     possible object is
     *     {@link IdentificationSource3Choice }
     *     
     */
    public IdentificationSource3Choice getTp() {
        return tp;
    }

    /**
     * Sets the value of the tp property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificationSource3Choice }
     *     
     */
    public void setTp(IdentificationSource3Choice value) {
        this.tp = value;
    }

}