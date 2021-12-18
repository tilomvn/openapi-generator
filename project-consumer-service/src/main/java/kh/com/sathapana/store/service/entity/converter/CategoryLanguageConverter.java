package kh.com.sathapana.store.service.entity.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kh.com.sathapana.store.service.entity.category.Language;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CategoryLanguageConverter implements AttributeConverter<Language, String> {
    final ObjectMapper objectMapper;

    public CategoryLanguageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Language language) {
        return objectMapper.writeValueAsString(language);
    }

    @SneakyThrows
    @Override
    public Language convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, Language.class);
    }
}
