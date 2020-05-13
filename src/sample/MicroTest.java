//package CP274Final.src.sample;
package sample;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static sample.DataRetriever.update;

public class MicroTest {
    DbTools newconnection = new DbTools();
    Controller control = new Controller();

    public MicroTest() throws Exception {

    }

    @Test
    void dataRetrievalTest() throws Exception {
        System.out.println(update("Los Angeles", "California", "deaths"));
    }

    @Test
    void testCountyByStateAndName() throws Exception {
        County first = newconnection
                .getCountyByNameAndState("Autauga", "Alabama");
        String name = first.name;
        name.equals("Autuaga");
    }

    @Test
    void testCases() throws Exception {
        County first = newconnection
                .getAllCounties()
                .get(5);
        int cases = first.getAll("confirmed").size();
        assertEquals(100,cases);
    }

    @Test
    void testDeaths() throws Exception {
        County first = newconnection
                .getAllCounties()
                .get(5);
        int cases = first.getAll("deaths").size();
        assertEquals(100,cases);
    }


    @Test
    void testCon() throws Exception {
        Connection con = newconnection
                .getConnection();
        assertNotNull(con);
    }

    @Test
    void testCounty() throws Exception {
        County first = newconnection
                .getAllCounties()
                .get(1309) ;
        String name = first.name;
        name.equals("Saginaw");
        }


}
