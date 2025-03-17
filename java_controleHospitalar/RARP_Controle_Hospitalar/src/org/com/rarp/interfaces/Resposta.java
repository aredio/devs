
package org.com.rarp.interfaces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Resposta complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Resposta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="requisicao" type="{http://interfaces.rarp.com.org/}Requisicao" minOccurs="0"/&gt;
 *         &lt;element name="pessoaFisica" type="{http://interfaces.rarp.com.org/}PessoaFisica" minOccurs="0"/&gt;
 *         &lt;element name="historico" type="{http://interfaces.rarp.com.org/}Historico" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Resposta", propOrder = {
    "requisicao",
    "pessoaFisica",
    "historico"
})
public class Resposta {

    protected Requisicao requisicao;
    protected PessoaFisica pessoaFisica;
    protected Historico historico;

    /**
     * Obtém o valor da propriedade requisicao.
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

    /**
     * Obtém o valor da propriedade historico.
     * 
     * @return
     *     possible object is
     *     {@link Historico }
     *     
     */
    public Historico getHistorico() {
        return historico;
    }

    /**
     * Define o valor da propriedade historico.
     * 
     * @param value
     *     allowed object is
     *     {@link Historico }
     *     
     */
    public void setHistorico(Historico value) {
        this.historico = value;
    }

}
