
package org.com.rarp.interfaces;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Historico complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Historico"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pessoaJuridica" type="{http://interfaces.rarp.com.org/}pessoaJuridica" minOccurs="0"/&gt;
 *         &lt;element name="entradaPacientes" type="{http://interfaces.rarp.com.org/}EntradaPaciente" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Historico", propOrder = {
    "tipo",
    "pessoaJuridica",
    "entradaPacientes",
    "descricao"
})
public class Historico {

    protected String tipo;
    protected PessoaJuridica pessoaJuridica;
    protected List<EntradaPaciente> entradaPacientes;
    protected String descricao;

    /**
     * Obtém o valor da propriedade tipo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o valor da propriedade tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Obtém o valor da propriedade pessoaJuridica.
     * 
     * @return
     *     possible object is
     *     {@link PessoaJuridica }
     *     
     */
    public PessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    /**
     * Define o valor da propriedade pessoaJuridica.
     * 
     * @param value
     *     allowed object is
     *     {@link PessoaJuridica }
     *     
     */
    public void setPessoaJuridica(PessoaJuridica value) {
        this.pessoaJuridica = value;
    }

    /**
     * Gets the value of the entradaPacientes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entradaPacientes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntradaPacientes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntradaPaciente }
     * 
     * 
     */
    public List<EntradaPaciente> getEntradaPacientes() {
        if (entradaPacientes == null) {
            entradaPacientes = new ArrayList<EntradaPaciente>();
        }
        return this.entradaPacientes;
    }

    /**
     * Obtém o valor da propriedade descricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define o valor da propriedade descricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricao(String value) {
        this.descricao = value;
    }

}
