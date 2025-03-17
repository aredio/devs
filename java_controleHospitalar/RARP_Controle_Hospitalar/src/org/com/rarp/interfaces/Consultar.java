
package org.com.rarp.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de consultar complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="consultar"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requisicao" type="{http://interfaces.rarp.com.org/}Requisicao" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultar", propOrder = {
    "requisicao"
})
public class Consultar {

    protected Requisicao requisicao;

    /**
     * Obt�m o valor da propriedade requisicao.
     * 
     * @return
     *     possible object is
     *     {@link Requisicao }
     *     
     */
    public Requisicao getRequisicao() {
        return requisicao;
    }

    /**
     * Define o valor da propriedade requisicao.
     * 
     * @param value
     *     allowed object is
     *     {@link Requisicao }
     *     
     */
    public void setRequisicao(Requisicao value) {
        this.requisicao = value;
    }

}
