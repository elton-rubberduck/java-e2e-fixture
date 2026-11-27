package demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  private final DataSource dataSource;

  public UserController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @GetMapping("/users")
  public String find(@RequestParam String name) throws Exception {
    return lookup(name);
  }

  protected String lookup(String userName) throws Exception {
    String query =
        "SELECT email FROM users WHERE name LIKE '" + userName + "%'";
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(query);
      return rs.next() ? rs.getString(1) : "";
    }
  }

  @GetMapping("/users/export")
  public String export(@RequestParam String format) throws Exception {
    Process p = Runtime.getRuntime().exec("sh -c 'export-users --format " + format + "'");
    return new String(p.getInputStream().readAllBytes());
  }
}
