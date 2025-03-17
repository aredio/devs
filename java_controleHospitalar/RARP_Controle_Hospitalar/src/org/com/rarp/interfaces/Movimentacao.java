
package org.com.rarp.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de Movimentacao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Movimentacao"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="dtMovimentacao" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="hrMovimentacao" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Movimentacao", propOrder = {
    "codigo",
    "dtMovimentacao",
    "hrMovimentacao"
})
@XmlSeeAlso({
    EntradaPaciente.class,
    Atendimento.class,
    SaidaPaciente.class
})
public class Movimentacao {

    protected int codigo;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtMovimentacao;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar hrMovimentacao;

    /**
     * Obtém o valor da propriedade codigo.
     * 
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Define o valor da propriedade codigo.
     * 
     */
    public void setCodigo(int value) {
        this.codigo = value;
    }

    /**
     * Obtém o valor da propriedade dtMovimentacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtMovimentacao() {
        return dtMovimentacao;
    }

    /**
     * Define o valor da propriedade dtMovimentacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtMovimentacao(XMLGregorianCalendar value) {
        this.dtMovimentacao = value;
    }

    /**
     * Obtém o valor da propriedade hrMovimentacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHrMovimentacao() {
        return hrMovimentacao;
    }

    /**
     * Define o valor da propriedade hrMovimentacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHrMovimentacao(XMLGregorianCalendar value) {
        this.hrMovimentacao = value;
    }

}
