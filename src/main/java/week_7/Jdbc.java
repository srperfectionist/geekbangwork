package week_7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shirui
 * @date 2022/2/27
 */
@Controller
public class Jdbc {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/insert")
    public void insert(){
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        String sql = "insert into order (id, userid) values (?,?)";
        for(int i = 1;i <= 10000000; i++){
            batchArgs.add(new Object[]{i,"测试"});
        }
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
