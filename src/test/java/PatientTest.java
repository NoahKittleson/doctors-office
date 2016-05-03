import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.assertj.core.api.Assertions.assertThat;


public class PatientTest {
  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctors_office_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteDoctorsQuery = "DELETE FROM doctors *;";
      String deletePatientsQuery = "DELETE FROM patients *;";
      con.createQuery(deleteDoctorsQuery).executeUpdate();
      con.createQuery(deletePatientsQuery).executeUpdate();
    }
  }

  @Test
  public void saveAPatientObject() {
    Patient newPatient  = new Patient ("Tony", "2016-01-01", 1);
    newPatient.save();
    Patient testPatient = Patient.find(newPatient.getId());
    assertEquals(testPatient.getBirthdate(), newPatient.getBirthdate());
  }
}
