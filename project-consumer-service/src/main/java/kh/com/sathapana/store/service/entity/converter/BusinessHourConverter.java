package kh.com.sathapana.store.service.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kh.com.sathapana.store.service.entity.BusinessHour;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class BusinessHourConverter implements AttributeConverter<List<BusinessHour>, String> {

    private final String SEPARATOR = "@";

    final ObjectMapper objectMapper;

    BusinessHourConverter() { this.objectMapper = new ObjectMapper(); }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(List<BusinessHour> businessHours) {
        return objectMapper.writeValueAsString(businessHours);
    }

    @SneakyThrows
    @Override
    public List<BusinessHour> convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, new TypeReference<List<BusinessHour>>() {});
    }
}
