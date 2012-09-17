
package gov.hhs.fha.nhinc.common.addrbook;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSummariesResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetSummariesResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="summaryObjects" type="{urn:gov:hhs:fha:nhinc:common:addrbook}contactSummary" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="errorList" type="{urn:gov:hhs:fha:nhinc:common:addrbook}serviceError" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSummariesResponseType", propOrder = {
    "summaryObjects",
    "errorList"
})
public class GetSummariesResponseType {

    @XmlElement(nillable = true)
    protected List<ContactSummary> summaryObjects;
    @XmlElement(nillable = true)
    protected List<ServiceError> errorList;

    /**
     * Gets the value of the summaryObjects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the summaryObjects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSummaryObjects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContactSummary }
     * 
     * 
     */
    public List<ContactSummary> getSummaryObjects() {
        if (summaryObjects == null) {
            summaryObjects = new ArrayList<ContactSummary>();
        }
        return this.summaryObjects;
    }

    /**
     * Gets the value of the errorList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errorList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrorList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceError }
     * 
     * 
     */
    public List<ServiceError> getErrorList() {
        if (errorList == null) {
            errorList = new ArrayList<ServiceError>();
        }
        return this.errorList;
    }

}
