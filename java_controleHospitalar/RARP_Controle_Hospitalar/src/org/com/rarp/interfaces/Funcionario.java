
package org.com.rarp.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de Funcionario complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Funcionario"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://interfaces.rarp.com.org/}PessoaFisica"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dtAdmissao" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="salarioContratual" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="CTPS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Funcionario", propOrder = {
    "dtAdmissao",
    "salarioContratual",
    "ctps"
})
@XmlSeeAlso({
    Medico.class
})
public class Funcionario
    extends PessoaFisica
{

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtAdmissao;
    protected double salarioContratual;
    @XmlElement(name = "CTPS")
    protected String ctps;

    /**
     * Obtém o valor da propriedade dtAdmissao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtAdmissao() {
        return dtAdmissao;
    }

    /**
     * Define o valor da propriedade dtAdmissao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtAdmissao(XMLGregorianCalendar value) {
        this.dtAdmissao = value;
    }

    /**
     * Obtém o valor da propriedade salarioContratual.
     * 
     */
    public double getSalarioContratual() {
        return salarioContratual;
    }

    /**
     * Define o valor da propriedade salarioContratual.
     * 
     */
    public void setSalarioContratual(double value) {
        this.salarioContratual = value;
    }

    /**
     * Obtém o valor da propriedade ctps.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCTPS() {
        return ctps;
    }

    /**
     * Define o valor da propriedade ctps.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCTPS(String value) {
        this.ctps = value;
    }

}
