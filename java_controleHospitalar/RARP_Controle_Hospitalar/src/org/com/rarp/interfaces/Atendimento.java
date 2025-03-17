
package org.com.rarp.interfaces;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Atendimento complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Atendimento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://interfaces.rarp.com.org/}Movimentacao"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dataAtendimento" type="{http://interfaces.rarp.com.org/}localDate" minOccurs="0"/&gt;
 *         &lt;element name="horaIni" type="{http://interfaces.rarp.com.org/}localTime" minOccurs="0"/&gt;
 *         &lt;element name="horaFim" type="{http://interfaces.rarp.com.org/}localTime" minOccurs="0"/&gt;
 *         &lt;element name="detalheMedico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descricao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="receitaMedica" type="{http://interfaces.rarp.com.org/}ReceitaMedica" minOccurs="0"/&gt;
 *         &lt;element name="statusAtendimento" type="{http://interfaces.rarp.com.org/}statusAtendimento" minOccurs="0"/&gt;
 *         &lt;element name="entradaPaciente" type="{http://interfaces.rarp.com.org/}EntradaPaciente" minOccurs="0"/&gt;
 *         &lt;element name="sintomas" type="{http://interfaces.rarp.com.org/}Sintoma" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="styleClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Atendimento", propOrder = {
    "dataAtendimento",
    "horaIni",
    "horaFim",
    "detalheMedico",
    "descricao",
    "receitaMedica",
    "statusAtendimento",
    "entradaPaciente",
    "sintomas",
    "styleClass"
})
public class Atendimento
    extends Movimentacao
{

    protected LocalDate dataAtendimento;
    protected LocalTime horaIni;
    protected LocalTime horaFim;
    protected String detalheMedico;
    protected String descricao;
    protected ReceitaMedica receitaMedica;
    @XmlSchemaType(name = "string")
    protected StatusAtendimento statusAtendimento;
    protected EntradaPaciente entradaPaciente;
    protected List<Sintoma> sintomas;
    protected String styleClass;

    /**
     * Obtém o valor da propriedade dataAtendimento.
     * 
     * @return
     *     possible object is
     *     {@link LocalDate }
     *     
     */
    public LocalDate getDataAtendimento() {
        return dataAtendimento;
    }

    /**
     * Define o valor da propriedade dataAtendimento.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalDate }
     *     
     */
    public void setDataAtendimento(LocalDate value) {
        this.dataAtendimento = value;
    }

    /**
     * Obtém o valor da propriedade horaIni.
     * 
     * @return
     *     possible object is
     *     {@link LocalTime }
     *     
     */
    public LocalTime getHoraIni() {
        return horaIni;
    }

    /**
     * Define o valor da propriedade horaIni.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalTime }
     *     
     */
    public void setHoraIni(LocalTime value) {
        this.horaIni = value;
    }

    /**
     * Obtém o valor da propriedade horaFim.
     * 
     * @return
     *     possible object is
     *     {@link LocalTime }
     *     
     */
    public LocalTime getHoraFim() {
        return horaFim;
    }

    /**
     * Define o valor da propriedade horaFim.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalTime }
     *     
     */
    public void setHoraFim(LocalTime value) {
        this.horaFim = value;
    }

    /**
     * Obtém o valor da propriedade detalheMedico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetalheMedico() {
        return detalheMedico;
    }

    /**
     * Define o valor da propriedade detalheMedico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetalheMedico(String value) {
        this.detalheMedico = value;
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

    /**
     * Obtém o valor da propriedade receitaMedica.
     * 
     * @return
     *     possible object is
     *     {@link ReceitaMedica }
     *     
     */
    public ReceitaMedica getReceitaMedica() {
        return receitaMedica;
    }

    /**
     * Define o valor da propriedade receitaMedica.
     * 
     * @param value
     *     allowed object is
     *     {@link ReceitaMedica }
     *     
     */
    public void setReceitaMedica(ReceitaMedica value) {
        this.receitaMedica = value;
    }

    /**
     * Obtém o valor da propriedade statusAtendimento.
     * 
     * @return
     *     possible object is
     *     {@link StatusAtendimento }
     *     
     */
    public StatusAtendimento getStatusAtendimento() {
        return statusAtendimento;
    }

    /**
     * Define o valor da propriedade statusAtendimento.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusAtendimento }
     *     
     */
    public void setStatusAtendimento(StatusAtendimento value) {
        this.statusAtendimento = value;
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

    /**
     * Gets the value of the sintomas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sintomas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSintomas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Sintoma }
     * 
     * 
     */
    public List<Sintoma> getSintomas() {
        if (sintomas == null) {
            sintomas = new ArrayList<Sintoma>();
        }
        return this.sintomas;
    }

    /**
     * Obtém o valor da propriedade styleClass.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Define o valor da propriedade styleClass.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStyleClass(String value) {
        this.styleClass = value;
    }

}
