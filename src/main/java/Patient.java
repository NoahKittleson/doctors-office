import java.util.List;
import org.sql2o.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Patient {
  private int id;
  private String name;
  private String birthdate;
  private int doctorid;

  public Patient (String name, String birthdate, int doctorid) {
    this.name = name;
    this.birthdate = birthdate;
    this.doctorid = doctorid;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getBirthdate() {
    return birthdate;
  }

  public int getDoctorId() {
    return doctorid;
  }

  @Override
  public boolean equals(Object otherPatient) {
    if (!(otherPatient instanceof Patient)) {
      return false;
    } else {
      Patient newPatient = (Patient) otherPatient;
      return this.getName().equals(newPatient.getName()) &&
             this.getId() == newPatient.getId() &&
             this.getBirthdate().equals(newPatient.getBirthdate()) &&
             this.getDoctorId() == newPatient.getDoctorId();
    }
  }

  public static List<Patient> all() {
    String sql = "SELECT id, name, birthdate, doctorid FROM patients";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Patient.class);
    }
  }

  public void save() {
    Timestamp timestamp = new Timestamp(0,0,0,0,0,0,0);
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date parsedDate = dateFormat.parse(this.getBirthdate());
      timestamp = new java.sql.Timestamp(parsedDate.getTime());
    }
    catch(Exception e) {

    }

    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patients(name, birthdate, doctorid) VALUES (:name, :birthdate, :doctorid)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("birthdate", timestamp)  //this is going to fail because we need to conver to timestamp
        .addParameter("doctorid", this.doctorid)
        .executeUpdate()
        .getKey();
    }
  }

  public static Patient find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients where id=:id";
      Patient category = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Patient.class);
      return category;
    }
  }
}
