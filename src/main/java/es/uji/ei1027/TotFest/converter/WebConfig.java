package es.uji.ei1027.TotFest.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final StringToDateConverter stringToDateConverter;

    public WebConfig(StringToDateConverter stringToDateConverter) {
        this.stringToDateConverter = stringToDateConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToDateConverter);
    }
}
