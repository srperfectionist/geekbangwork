package week_5.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shirui
 * @date 2022/2/13
 */
@Configuration
@ConditionalOnClass(Student.class)
@EnableConfigurationProperties(StudentConfig.class)
public class StudentAutoConfiguration {

    StudentConfig studentConfig;

    @Autowired
    public void setStudentConfig(StudentConfig studentConfig) {
        this.studentConfig = studentConfig;
    }

    @Bean
    public Student init(){
        String id = studentConfig.getId();
        String name = studentConfig.getName();

        return Student.builder().id(id).name(name).build();
    }
}
