
package org.com.rarp.interfaces;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de statusAtendimento.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="statusAtendimento"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="emAberto"/&gt;
 *     &lt;enumeration value="emAndamento"/&gt;
 *     &lt;enumeration value="realizado"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "statusAtendimento")
@XmlEnum
public enum StatusAtendimento {

    @XmlEnumValue("emAberto")
    EM_ABERTO("emAberto"),
    @XmlEnumValue("emAndamento")
    EM_ANDAMENTO("emAndamento"),
    @XmlEnumValue("realizado")
    REALIZADO("realizado");
    private final String value;

    StatusAtendimento(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StatusAtendimento fromValue(String v) {
        for (StatusAtendimento c: StatusAtendimento.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
