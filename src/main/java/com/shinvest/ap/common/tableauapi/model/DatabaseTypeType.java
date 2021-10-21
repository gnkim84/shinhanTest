//
// �� ������ JAXB(JavaTM Architecture for XML Binding) ���� ���� 2.2.8-b130911.1802 ������ ���� �����Ǿ����ϴ�. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>�� �����Ͻʽÿ�. 
// �� ������ �����ϸ� �ҽ� ��Ű���� ���������� �� ���� ������ �սǵ˴ϴ�. 
// ���� ��¥: 2020.02.03 �ð� 05:34:04 PM KST 
//


package com.shinvest.ap.common.tableauapi.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>databaseTypeType�� ���� Java Ŭ�����Դϴ�.
 * 
 * <p>���� ��Ű�� ������ �� Ŭ������ ���ԵǴ� �ʿ��� �������� �����մϴ�.
 * <p>
 * <pre>
 * &lt;simpleType name="databaseTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DatabaseServer"/>
 *     &lt;enumeration value="File"/>
 *     &lt;enumeration value="WebDataConnector"/>
 *     &lt;enumeration value="CloudFile"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "databaseTypeType")
@XmlEnum
public enum DatabaseTypeType {

    @XmlEnumValue("DatabaseServer")
    DATABASE_SERVER("DatabaseServer"),
    @XmlEnumValue("File")
    FILE("File"),
    @XmlEnumValue("WebDataConnector")
    WEB_DATA_CONNECTOR("WebDataConnector"),
    @XmlEnumValue("CloudFile")
    CLOUD_FILE("CloudFile");
    private final String value;

    DatabaseTypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DatabaseTypeType fromValue(String v) {
        for (DatabaseTypeType c: DatabaseTypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
