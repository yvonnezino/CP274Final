//package CP274Final.src.sample;
package sample;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;



public class MicroTest {
    DbTools newconnection = new DbTools();
    Controller control = new Controller();

    public MicroTest() throws Exception {
    }


    @Test
    // Test to see if county can be received from database by name and state, used in UI
    void testCountyByStateAndName() throws Exception {
        County first = newconnection
                .getCountyByNameAndState("Autauga", "Alabama");
        String name = first.name;
        name.equals("Autuaga");
    }

    @Test
    // Test to see if infected case data can be taken out of database by county
    void testCases() throws Exception {
        County first = newconnection
                .getAllCounties()
                .get(5);
        int cases = first.getAll("confirmed").get(first.getAll("confirmed").size()-1);
        assertEquals(58,cases);
    }

    @Test
        // Test to see if deaths  data can be taken out of database by county
    void testDeaths() throws Exception {
        County first = newconnection
                .getAllCounties()
                .get(5);
        int cases = first.getAll("deaths").get(first.getAll("deaths").size()-1);
        assertEquals(3,cases);
    }


    @Test
    // Tests to see if connection between program and database can be achieved
    void testCon() throws Exception {
        Connection con = newconnection
                .getConnection();
        assertNotNull(con);
    }

    @Test
    // Test to see if county list is made in getAllCounties method
    void testCounty() throws Exception {
        County first = newconnection
                .getAllCounties()
                .get(1309) ;
        String name = first.name;
        name.equals("Saginaw");
        }


}
