package demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    try (Connection connection = dataSource.getConnection();
        PreparedStatement statement =
            connection.prepareStatement("SELECT email FROM users WHERE name = ?")) {
      statement.setString(1, userName);
      ResultSet rs = statement.executeQuery();
      return rs.next() ? rs.getString(1) : "";
    }
  }
}
