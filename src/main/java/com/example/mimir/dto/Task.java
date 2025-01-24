package com.example.mimir.dto;

import com.google.gson.Gson;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.Map;

final public class Task {

    private String subject;
    private String question;
    private String language;
    private List<String> words;
    private Map<String, String> translatedWords;

    @Converter(autoApply = true)
    public static class TaskJsonConverter implements AttributeConverter<Task, String> {

        private final static Gson GSON = new Gson();

        @Override
        public String convertToDatabaseColumn(Task mjo) {
            return GSON.toJson(mjo);
        }

        @Override
        public Task convertToEntityAttribute(String dbData) {
            return GSON.fromJson(dbData, Task.class);
        }
    }
}
