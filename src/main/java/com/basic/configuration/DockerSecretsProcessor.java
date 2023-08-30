package com.basic.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class DockerSecretsProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Resource resource = new FileSystemResource("/run/secrets/db-password");
        if (resource.exists() && System.getProperty("MYSQL_PASSWORD") == null) {
            try {
                String dbPassword = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
                System.setProperty("MYSQL_PASSWORD", dbPassword);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}