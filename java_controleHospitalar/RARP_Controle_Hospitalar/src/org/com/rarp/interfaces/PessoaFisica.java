
package org.com.rarp.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de PessoaFisica complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="PessoaFisica"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://interfaces.rarp.com.org/}Pessoa"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cpf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="rg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sexo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="possuiNecessidades" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="certidaoNascimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SUS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PessoaFisica", propOrder = {
    "cpf",
    "rg",
    "sexo",
    "possuiNecessidades",
    "certidaoNascimento",
    "sus"
})
@XmlSeeAlso({
    Funcionario.class,
    Paciente.class
})
public class PessoaFisica
    extends Pessoa
{

    protected String cpf;
    protected String rg;
    protected String sexo;
    protected boolean possuiNecessidades;
    protected String certidaoNascimento;
    @XmlElement(name = "SUS")
    protected String sus;

    /**
     * Obtém o valor da propriedade cpf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o valor da propriedade cpf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpf(String value) {
        this.cpf = value;
    }

    /**
     * Obtém o valor da propriedade rg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRg() {
        return rg;
    }

    /**
     * Define o valor da propriedade rg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRg(String value) {
        this.rg = value;
    }

    /**
     * Obtém o valor da propriedade sexo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * Define o valor da propriedade sexo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSexo(String value) {
        this.sexo = value;
    }

    /**
     * Obtém o valor da propriedade possuiNecessidades.
     * 
     */
    public boolean isPossuiNecessidades() {
        return possuiNecessidades;
    }

    /**
     * Define o valor da propriedade possuiNecessidades.
     * 
     */
    public void setPossuiNecessidades(boolean value) {
        this.possuiNecessidades = value;
    }

    /**
     * Obtém o valor da propriedade certidaoNascimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertidaoNascimento() {
        return certidaoNascimento;
    }

    /**
     * Define o valor da propriedade certidaoNascimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertidaoNascimento(String value) {
        this.certidaoNascimento = value;
    }

    /**
     * Obtém o valor da propriedade sus.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUS() {
        return sus;
    }

    /**
     * Define o valor da propriedade sus.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUS(String value) {
        this.sus = value;
    }

}
