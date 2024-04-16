package ru.otus.orm;

import ru.otus.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String className;
    private final Constructor constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) throws NoSuchMethodException, NoSuchFieldException {
        this.className = clazz.getSimpleName();
        this.allFields = Arrays.asList(clazz.getDeclaredFields());
        allFields.forEach(f -> f.setAccessible(true));
        this.constructor = clazz.getConstructor();
        this.idField = Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException("Id field not found"));
        List<Field> allFieldsCopy = new ArrayList<>(allFields);
        allFieldsCopy.remove(idField);
        this.fieldsWithoutId = new ArrayList<>(allFieldsCopy);
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
