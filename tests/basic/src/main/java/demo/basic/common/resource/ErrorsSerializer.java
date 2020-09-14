package demo.basic.common.resource;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator generator, SerializerProvider serializerProvider)
            throws IOException {

        generator.writeStartArray();
        errors.getFieldErrors().stream().forEach(e -> {
            try {
                generator.writeStartObject();
                generator.writeStringField("field", e.getField());
                generator.writeStringField("objectName", e.getObjectName());
                generator.writeStringField("code", e.getCode());
                generator.writeStringField("defaultMessage", e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();
                if (rejectedValue != null) {
                    generator.writeStringField("rejectedValue", rejectedValue.toString());
                }
                generator.writeEndObject();
            } catch (IOException e1) {
                logger.warn("Failed to write error message", e1);
            }
        });

        errors.getGlobalErrors().forEach(e -> {
            try {
                generator.writeStartObject();
                generator.writeStringField("objectName", e.getObjectName());
                generator.writeStringField("code", e.getCode());
                generator.writeStringField("defaultMessage", e.getDefaultMessage());
                generator.writeEndObject();
            } catch (IOException e1) {
                logger.warn("Failed to write error message", e1);
            }
        });

        generator.writeEndArray();
    }
}

