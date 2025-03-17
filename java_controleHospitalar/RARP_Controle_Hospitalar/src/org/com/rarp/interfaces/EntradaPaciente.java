
package org.com.rarp.interfaces;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de EntradaPaciente complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="EntradaPaciente"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://interfaces.rarp.com.org/}Movimentacao"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="preTriagem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="medico" type="{http://interfaces.rarp.com.org/}Medico" minOccurs="0"/&gt;
 *         &lt;element name="paciente" type="{http://interfaces.rarp.com.org/}Paciente" minOccurs="0"/&gt;
 *         &lt;element name="atendimentos" type="{http://interfaces.rarp.com.org/}Atendimento" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="saidaPaciente" type="{http://interfaces.rarp.com.org/}SaidaPaciente" minOccurs="0"/&gt;
 *         &lt;element name="alta" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="emergencia" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntradaPaciente", propOrder = {
    "preTriagem",
    "medico",
    "paciente",
    "atendimentos",
    "saidaPaciente",
    "alta",
    "emergencia"
})
public class EntradaPaciente
    extends Movimentacao
{

    protected String preTriagem;
    protected Medico medico;
    protected Paciente paciente;
    protected List<Atendimento> atendimentos;
    protected SaidaPaciente saidaPaciente;
    protected boolean alta;
    protected boolean emergencia;

    /**
     * Obtém o valor da propriedade preTriagem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreTriagem() {
        return preTriagem;
    }

    /**
     * Define o valor da propriedade preTriagem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreTriagem(String value) {
        this.preTriagem = value;
    }

    /**
     * Obtém o valor da propriedade medico.
     * 
     * @return
     *     possible object is
     *     {@link Medico }
     *     
     */
    public Medico getMedico() {
        return medico;
    }

    /**
     * Define o valor da propriedade medico.
     * 
     * @param value
     *     allowed object is
     *     {@link Medico }
     *     
     */
    public void setMedico(Medico value) {
        this.medico = value;
    }

    /**
     * Obtém o valor da propriedade paciente.
     * 
     * @return
     *     possible object is
     *     {@link Paciente }
     *     
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Define o valor da propriedade paciente.
     * 
     * @param value
     *     allowed object is
     *     {@link Paciente }
     *     
     */
    public void setPaciente(Paciente value) {
        this.paciente = value;
    }

    /**
     * Gets the value of the atendimentos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the atendimentos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAtendimentos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Atendimento }
     * 
     * 
     */
    public List<Atendimento> getAtendimentos() {
        if (atendimentos == null) {
            atendimentos = new ArrayList<Atendimento>();
        }
        return this.atendimentos;
    }

    /**
     * Obtém o valor da propriedade saidaPaciente.
     * 
     * @return
     *     possible object is
     *     {@link SaidaPaciente }
     *     
     */
    public SaidaPaciente getSaidaPaciente() {
        return saidaPaciente;
    }

    /**
     * Define o valor da propriedade saidaPaciente.
     * 
     * @param value
     *     allowed object is
     *     {@link SaidaPaciente }
     *     
     */
    public void setSaidaPaciente(SaidaPaciente value) {
        this.saidaPaciente = value;
    }

    /**
     * Obtém o valor da propriedade alta.
     * 
     */
    public boolean isAlta() {
        return alta;
    }

    /**
     * Define o valor da propriedade alta.
     * 
     */
    public void setAlta(boolean value) {
        this.alta = value;
    }

    /**
     * Obtém o valor da propriedade emergencia.
     * 
     */
    public boolean isEmergencia() {
        return emergencia;
    }

    /**
     * Define o valor da propriedade emergencia.
     * 
     */
    public void setEmergencia(boolean value) {
        this.emergencia = value;
    }

}
