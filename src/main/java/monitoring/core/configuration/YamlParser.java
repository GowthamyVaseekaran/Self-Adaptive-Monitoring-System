package monitoring.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Read Yaml file.
 */

@Component
@EnableConfigurationProperties
@ConfigurationProperties("user")
public class YamlParser {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" + this.getEmail() + "}";
    }
}
