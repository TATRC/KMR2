
package gov.hhs.fha.nhinc.common.addrbook;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.hhs.fha.nhinc.common.addrbook package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetContactDetailsResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "GetContactDetailsResponse");
    private final static QName _GetContactDetailsRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "GetContactDetailsRequest");
    private final static QName _GetAllAddrRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "GetAllAddrRequest");
    private final static QName _GetSummariesResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "GetSummariesResponse");
    private final static QName _SearchAddrRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "SearchAddrRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.hhs.fha.nhinc.common.addrbook
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetContactDetailsResponseType }
     * 
     */
    public GetContactDetailsResponseType createGetContactDetailsResponseType() {
        return new GetContactDetailsResponseType();
    }

    /**
     * Create an instance of {@link ContactDetails }
     * 
     */
    public ContactDetails createContactDetails() {
        return new ContactDetails();
    }

    /**
     * Create an instance of {@link SearchAddrRequestType }
     * 
     */
    public SearchAddrRequestType createSearchAddrRequestType() {
        return new SearchAddrRequestType();
    }

    /**
     * Create an instance of {@link GetAllAddrRequestType }
     * 
     */
    public GetAllAddrRequestType createGetAllAddrRequestType() {
        return new GetAllAddrRequestType();
    }

    /**
     * Create an instance of {@link GetSummariesResponseType }
     * 
     */
    public GetSummariesResponseType createGetSummariesResponseType() {
        return new GetSummariesResponseType();
    }

    /**
     * Create an instance of {@link GetContactDetailsRequestType }
     * 
     */
    public GetContactDetailsRequestType createGetContactDetailsRequestType() {
        return new GetContactDetailsRequestType();
    }

    /**
     * Create an instance of {@link ServiceError }
     * 
     */
    public ServiceError createServiceError() {
        return new ServiceError();
    }

    /**
     * Create an instance of {@link ContactSummary }
     * 
     */
    public ContactSummary createContactSummary() {
        return new ContactSummary();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContactDetailsResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "GetContactDetailsResponse")
    public JAXBElement<GetContactDetailsResponseType> createGetContactDetailsResponse(GetContactDetailsResponseType value) {
        return new JAXBElement<GetContactDetailsResponseType>(_GetContactDetailsResponse_QNAME, GetContactDetailsResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContactDetailsRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "GetContactDetailsRequest")
    public JAXBElement<GetContactDetailsRequestType> createGetContactDetailsRequest(GetContactDetailsRequestType value) {
        return new JAXBElement<GetContactDetailsRequestType>(_GetContactDetailsRequest_QNAME, GetContactDetailsRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllAddrRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "GetAllAddrRequest")
    public JAXBElement<GetAllAddrRequestType> createGetAllAddrRequest(GetAllAddrRequestType value) {
        return new JAXBElement<GetAllAddrRequestType>(_GetAllAddrRequest_QNAME, GetAllAddrRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSummariesResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "GetSummariesResponse")
    public JAXBElement<GetSummariesResponseType> createGetSummariesResponse(GetSummariesResponseType value) {
        return new JAXBElement<GetSummariesResponseType>(_GetSummariesResponse_QNAME, GetSummariesResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchAddrRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "SearchAddrRequest")
    public JAXBElement<SearchAddrRequestType> createSearchAddrRequest(SearchAddrRequestType value) {
        return new JAXBElement<SearchAddrRequestType>(_SearchAddrRequest_QNAME, SearchAddrRequestType.class, null, value);
    }

}
