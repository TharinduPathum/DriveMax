package lk.ijse.javafx.drivemax.bo.util;

import java.lang.reflect.Field;

public class EntityDTOConverter {

    // Convert source object to target type
    public static <S, T> T convert(S source, Class<T> targetClass) {
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();

            for (Field sourceField : source.getClass().getDeclaredFields()) {
                sourceField.setAccessible(true);
                try {
                    Field targetField = targetClass.getDeclaredField(sourceField.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, sourceField.get(source));
                } catch (NoSuchFieldException ignored) {
                    // Field doesn't exist in target, skip
                }
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Error converting between types", e);
        }
    }
}
