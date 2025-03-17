
package org.com.rarp.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Requisicao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Requisicao"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="serie" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="numeracao" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="usuario" type="{http://interfaces.rarp.com.org/}Usuario" minOccurs="0"/&gt;
 *         &lt;element name="pessoaFisica" type="{http://interfaces.rarp.com.org/}PessoaFisica" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Requisicao", propOrder = {
    "serie",
    "numeracao",
    "usuario",
    "pessoaFisica"
})
public class Requisicao {

    protected long serie;
    protected long numeracao;
    protected Usuario usuario;
    protected PessoaFisica pessoaFisica;

    /**
     * Obtém o valor da propriedade serie.
     * 
     */
    public long getSerie() {
        return serie;
    }

    /**
     * Define o valor da propriedade serie.
     * 
     */
    public void setSerie(long value) {
        this.serie = value;
    }

    /**
     * Obtém o valor da propriedade numeracao.
     * 
     */
    public long getNumeracao() {
        return numeracao;
    }

    /**
     * Define o valor da propriedade numeracao.
     * 
     */
    public void setNumeracao(long value) {
        this.numeracao = value;
    }

    /**
     * Obtém o valor da propriedade usuario.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Define o valor da propriedade usuario.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuario(Usuario value) {
        this.usuario = value;
    }

    /**
     * Obtém o valor da propriedade pessoaFisica.
     * 
     * @return
     *     possible object is
     *     {@link PessoaFisica }
     *     
     */
    public PessoaFisica getPessoaFisica() {
        return pessoaFisica;
    }

    /**
     * Define o valor da propriedade pessoaFisica.
     * 
     * @param value
     *     allowed object is
     *     {@link PessoaFisica }
     *     
     */
    public void setPessoaFisica(PessoaFisica value) {
        this.pessoaFisica = value;
    }

}
