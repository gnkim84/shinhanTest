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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>fieldType complex type�� ���� Java Ŭ�����Դϴ�.
 * 
 * <p>���� ��Ű�� ������ �� Ŭ������ ���ԵǴ� �ʿ��� �������� �����մϴ�.
 * 
 * <pre>
 * &lt;complexType name="fieldType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sampleValues" type="{http://tableau.com/api}distinctValueType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dataType" type="{http://tableau.com/api}dataTypeType" minOccurs="0"/>
 *         &lt;element name="fieldRole" type="{http://tableau.com/api}fieldRoleType" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fieldType", propOrder = {
    "sampleValues",
    "dataType",
    "fieldRole",
    "name"
})
public class FieldType {

    protected List<DistinctValueType> sampleValues;
    @XmlSchemaType(name = "string")
    protected DataTypeType dataType;
    @XmlSchemaType(name = "string")
    protected FieldRoleType fieldRole;
    protected String name;

    /**
     * Gets the value of the sampleValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sampleValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSampleValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DistinctValueType }
     * 
     * 
     */
    public List<DistinctValueType> getSampleValues() {
        if (sampleValues == null) {
            sampleValues = new ArrayList<DistinctValueType>();
        }
        return this.sampleValues;
    }

    /**
     * dataType �Ӽ��� ���� �����ɴϴ�.
     * 
     * @return
     *     possible object is
     *     {@link DataTypeType }
     *     
     */
    public DataTypeType getDataType() {
        return dataType;
    }

    /**
     * dataType �Ӽ��� ���� �����մϴ�.
     * 
     * @param value
     *     allowed object is
     *     {@link DataTypeType }
     *     
     */
    public void setDataType(DataTypeType value) {
        this.dataType = value;
    }

    /**
     * fieldRole �Ӽ��� ���� �����ɴϴ�.
     * 
     * @return
     *     possible object is
     *     {@link FieldRoleType }
     *     
     */
    public FieldRoleType getFieldRole() {
        return fieldRole;
    }

    /**
     * fieldRole �Ӽ��� ���� �����մϴ�.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldRoleType }
     *     
     */
    public void setFieldRole(FieldRoleType value) {
        this.fieldRole = value;
    }

    /**
     * name �Ӽ��� ���� �����ɴϴ�.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * name �Ӽ��� ���� �����մϴ�.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
