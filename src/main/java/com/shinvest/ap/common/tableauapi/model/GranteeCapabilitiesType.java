//
// �� ������ JAXB(JavaTM Architecture for XML Binding) ���� ���� 2.2.8-b130911.1802 ������ ���� �����Ǿ����ϴ�. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>�� �����Ͻʽÿ�. 
// �� ������ �����ϸ� �ҽ� ��Ű���� ���������� �� ���� ������ �սǵ˴ϴ�. 
// ���� ��¥: 2020.02.03 �ð� 05:34:04 PM KST 
//


package com.shinvest.ap.common.tableauapi.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>granteeCapabilitiesType complex type�� ���� Java Ŭ�����Դϴ�.
 * 
 * <p>���� ��Ű�� ������ �� Ŭ������ ���ԵǴ� �ʿ��� �������� �����մϴ�.
 * 
 * <pre>
 * &lt;complexType name="granteeCapabilitiesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="group" type="{http://tableau.com/api}groupType"/>
 *           &lt;element name="user" type="{http://tableau.com/api}userType"/>
 *         &lt;/choice>
 *         &lt;element name="capabilities">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="capability" type="{http://tableau.com/api}capabilityType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "granteeCapabilitiesType", propOrder = {
    "group",
    "user",
    "capabilities"
})
public class GranteeCapabilitiesType {

    protected GroupType group;
    protected UserType user;
    @XmlElement(required = true)
    protected GranteeCapabilitiesType.Capabilities capabilities;

    /**
     * group �Ӽ��� ���� �����ɴϴ�.
     * 
     * @return
     *     possible object is
     *     {@link GroupType }
     *     
     */
    public GroupType getGroup() {
        return group;
    }

    /**
     * group �Ӽ��� ���� �����մϴ�.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupType }
     *     
     */
    public void setGroup(GroupType value) {
        this.group = value;
    }

    /**
     * user �Ӽ��� ���� �����ɴϴ�.
     * 
     * @return
     *     possible object is
     *     {@link UserType }
     *     
     */
    public UserType getUser() {
        return user;
    }

    /**
     * user �Ӽ��� ���� �����մϴ�.
     * 
     * @param value
     *     allowed object is
     *     {@link UserType }
     *     
     */
    public void setUser(UserType value) {
        this.user = value;
    }

    /**
     * capabilities �Ӽ��� ���� �����ɴϴ�.
     * 
     * @return
     *     possible object is
     *     {@link GranteeCapabilitiesType.Capabilities }
     *     
     */
    public GranteeCapabilitiesType.Capabilities getCapabilities() {
        return capabilities;
    }

    /**
     * capabilities �Ӽ��� ���� �����մϴ�.
     * 
     * @param value
     *     allowed object is
     *     {@link GranteeCapabilitiesType.Capabilities }
     *     
     */
    public void setCapabilities(GranteeCapabilitiesType.Capabilities value) {
        this.capabilities = value;
    }


    /**
     * <p>anonymous complex type�� ���� Java Ŭ�����Դϴ�.
     * 
     * <p>���� ��Ű�� ������ �� Ŭ������ ���ԵǴ� �ʿ��� �������� �����մϴ�.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="capability" type="{http://tableau.com/api}capabilityType" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "capability"
    })
    public static class Capabilities {

        @XmlElement(required = true)
        protected List<CapabilityType> capability;

        /**
         * Gets the value of the capability property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the capability property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCapability().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CapabilityType }
         * 
         * 
         */
        public List<CapabilityType> getCapability() {
            if (capability == null) {
                capability = new ArrayList<CapabilityType>();
            }
            return this.capability;
        }

    }

}
