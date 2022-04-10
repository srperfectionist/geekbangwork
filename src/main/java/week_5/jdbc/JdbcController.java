package week_5.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author shirui
 * @date 2022/2/13
 */
@RestController
public class JdbcController {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/query")
    public List<Map<String, Object>> query(@PathVariable("id") int id) {
        String sql = "select * from user where id = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }

    @GetMapping("/addUser")
    public String addUser() {
        String sql = "insert into mybatis.user(id,name,pwd) values (?, ?, ?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, "1");
                preparedStatement.setString(2, "test");
                preparedStatement.setString(3, "123456");
                return preparedStatement;
            }
        });
        return "addUser-ok";
    }

    @GetMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id") int id) {
        String sql = "update user set name= ?,pwd= ? where id= ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, "test");
                preparedStatement.setString(2, "123456");
                preparedStatement.setString(3, "1");
                return preparedStatement;
            }
        });
        return "updateUser-ok";
    }

    @GetMapping("/deleteUser/{id}")
    public String delUser(@PathVariable("id") int id) {
        String sql = "delete from user where id=?";
        jdbcTemplate.update(sql, id);
        return "delUser-ok";
    }
}
