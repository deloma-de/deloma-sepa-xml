
package de.deloma.tools.sepa.model.pain.pain0800302;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChargeBearerTypeSEPACode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ChargeBearerTypeSEPACode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SLEV"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ChargeBearerTypeSEPACode")
@XmlEnum
public enum ChargeBearerTypeSEPACode {

    SLEV;

    public String value() {
        return name();
    }

    public static ChargeBearerTypeSEPACode fromValue(String v) {
        return valueOf(v);
    }

}
