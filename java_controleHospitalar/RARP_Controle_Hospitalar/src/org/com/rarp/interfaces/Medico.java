
package org.com.rarp.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Medico complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Medico"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://interfaces.rarp.com.org/}Funcionario"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codigoMedico" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="CRM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Medico", propOrder = {
    "codigoMedico",
    "crm"
})
public class Medico
    extends Funcionario
{

    protected int codigoMedico;
    @XmlElement(name = "CRM")
    protected String crm;

    /**
     * Obtém o valor da propriedade codigoMedico.
     * 
     */
    public int getCodigoMedico() {
        return codigoMedico;
    }

    /**
     * Define o valor da propriedade codigoMedico.
     * 
     */
    public void setCodigoMedico(int value) {
        this.codigoMedico = value;
    }

    /**
     * Obtém o valor da propriedade crm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCRM() {
        return crm;
    }

    /**
     * Define o valor da propriedade crm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCRM(String value) {
        this.crm = value;
    }

}
