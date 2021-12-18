package kh.com.sathapana.store.service.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import kh.com.sathapana.store.model.LanguageDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class LanguageSerializer extends StdSerializer<LanguageDTO> {

    @Autowired
    private Translator translator;

    public LanguageSerializer() {
        this(LanguageDTO.class);
    }

    protected LanguageSerializer(Class<LanguageDTO> t) {
        super(t);
    }

    @Override
    public void serialize(LanguageDTO language, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        String languageTag = translator.getLocale().toLanguageTag();
        if (StringUtils.isNotEmpty(languageTag) && languageTag.equals("en")) {
            jsonGenerator.writeString(language.getEn());
        } else
            jsonGenerator.writeString(language.getKm());
    }

}
