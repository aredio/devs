
package org.com.rarp.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de SaidaPaciente complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="SaidaPaciente"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://interfaces.rarp.com.org/}Movimentacao"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="estadoPaciente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="entradaPaciente" type="{http://interfaces.rarp.com.org/}EntradaPaciente" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SaidaPaciente", propOrder = {
    "estadoPaciente",
    "entradaPaciente"
})
public class SaidaPaciente
    extends Movimentacao
{

    protected String estadoPaciente;
    protected EntradaPaciente entradaPaciente;

    /**
     * Obtém o valor da propriedade estadoPaciente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoPaciente() {
        return estadoPaciente;
    }

    /**
     * Define o valor da propriedade estadoPaciente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoPaciente(String value) {
        this.estadoPaciente = value;
    }

    /**
     * Obtém o valor da propriedade entradaPaciente.
     * 
     * @return
     *     possible object is
     *     {@link EntradaPaciente }
     *     
     */
    public EntradaPaciente getEntradaPaciente() {
        return entradaPaciente;
    }

    /**
     * Define o valor da propriedade entradaPaciente.
     * 
     * @param value
     *     allowed object is
     *     {@link EntradaPaciente }
     *     
     */
    public void setEntradaPaciente(EntradaPaciente value) {
        this.entradaPaciente = value;
    }

}
