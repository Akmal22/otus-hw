package ru.otus.dao.orm;

import ru.otus.dao.meta.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) throws NoSuchMethodException, NoSuchFieldException {
        this.name = clazz.getSimpleName();
        this.constructor = clazz.getConstructor();
        this.idField = Arrays.stream(clazz.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException("Id field not found"));
        idField.setAccessible(true);
        this.allFields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
        allFields.forEach(f -> f.setAccessible(true));
        List<Field> allFieldsCopy = new ArrayList<>(allFields);
        allFieldsCopy.remove(idField);
        this.fieldsWithoutId = new ArrayList<>(allFieldsCopy);
    }

    @Override
    public String getName() {
        return name;
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
