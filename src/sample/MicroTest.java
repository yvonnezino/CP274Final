//package CP274Final.src.sample;
package sample;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import java.sql.Connection;
import java.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MicroTest {
    DbTools newconnection = new DbTools();
    Controller control = new Controller();

    public MicroTest() throws Exception {
    }


    @Test
    // Test to see if county can be retired from UI that gives county and state name
    void testCountyByStateAndName() throws Exception {
        County first = newconnection
                .getCountyByNameAndState("Autauga", "Alabama");
        String name = first.name;
        name.equals("Autuaga");
    }

    @Test
    //Test to see if county infected number can be retrieved
    void testCases() throws Exception {
        County first = newconnection
                .getAllCounties()
                .get(5);
        int cases = first.getAll("confirmed").size();
        assertEquals(100,cases);
    }

    @Test
        //Test to see if county dead number can be retrieved
    void testDeaths() throws Exception {
        County first = newconnection
                .getAllCounties()
                .get(5);
        int cases = first.getAll("deaths").size();
        assertEquals(100,cases);
    }


    @Test
    // Test to see if connection can be achieved between program and database
    void testCon() throws Exception {
        Connection con = newconnection
                .getConnection();
        assertNotNull(con);
    }

    @Test
    // Test to see if countys are added to County ArrayList from database
    void testCounty() throws Exception {
        County first = newconnection
                .getAllCounties()
                .get(1309) ;
        String name = first.name;
        name.equals("Saginaw");
        }

        //Testing that the get getCountyByNameAndState in DbTools gets different data for
        //counties with the same name in different states
        @Test
        void testDuplicateCounties() throws Exception {
        County sameNameOne= newconnection.getCountyByNameAndState("Cook","Illinois");
        County sameNameTwo= newconnection.getCountyByNameAndState("Cook","Georgia");
        assertNotSame(sameNameOne,sameNameTwo);
        }


}
