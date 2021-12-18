package kh.com.sathapana.store.service.configuration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
@AllArgsConstructor
public class Translator {

    private final ResourceBundleMessageSource messageSource;
    private static List<String> SUPPORTED_LANGUAGE;
    static {
        SUPPORTED_LANGUAGE = Arrays.asList("km", "en");
    }
    public String translate(String msgCode, String... params) {
        Locale locale = getLocale();
        return messageSource.getMessage(msgCode, params, locale);
    }

    public Locale getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        if(!SUPPORTED_LANGUAGE.contains(locale.getLanguage())) {
            locale = new Locale("km", "KM");
        }
        return locale;
    }

    public Locale getLocaleWithCountry(){
        Locale locale = LocaleContextHolder.getLocale();
        String languageTag = locale.toLanguageTag();
        if(!SUPPORTED_LANGUAGE.contains(languageTag)) {
            locale = new Locale("KM", "KM");
        } else {
            locale = languageTag.equalsIgnoreCase("en") ?  new Locale("en", "US") :
                    new Locale("km", "KM");
        }
        return locale;
    }

}
