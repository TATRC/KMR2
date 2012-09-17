package gov.hhs.fha.nhinc.addrbookmgr;

import gov.hhs.fha.nhinc.common.addrbook.ContactSummary;
import gov.hhs.fha.nhinc.common.addrbook.GetSummariesResponseType;
import gov.hhs.fha.nhinc.common.addrbook.SearchAddrRequestType;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AddrBookTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AddrBookTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AddrBookTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testSearchAddr()
    {
//        SearchAddrRequestType searchAddr = new SearchAddrRequestType();
//        searchAddr.setUserId("30354");
//        searchAddr.setSearch("*");
//        AddressBookImpl impl = new AddressBookImpl();
//        GetSummariesResponseType summaries = impl.searchAddr(searchAddr);
//        List<ContactSummary> contacts = summaries.getSummaryObjects();
//        ContactSummary contact = contacts.get(0);
//        System.out.println(contact.getName());
        assertTrue( true );
    }
}
