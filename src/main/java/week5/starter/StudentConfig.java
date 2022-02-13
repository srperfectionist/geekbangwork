package week5.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shirui
 * @date 2022/2/13
 */
@Component
@ConfigurationProperties(prefix = "student")
@Getter
@Setter
public class StudentConfig {

    private String id;
    private String name;
}
