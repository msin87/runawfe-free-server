package ru.runa.wfe.var;

public enum WfVariableType {
    TIME("Время"),
    GROUP("Группа"),
    DATE("Дата"),
    DATE_TIME("Дата со временем"),
    DOUBLE("Дробное число"),
    EXECUTOR("Исполнитель"),
    MAP("Карта"),
    ACTOR("Пользователь"),
    USER_TYPE("Пользовательский тип данных"),
    LIST("Список"),
    PROCESS_ID("Ссылка на процесс"),
    STRING("Строка"),
    TEXT("Текст"),
    FORMATTED_TEXT("Форматированный текст"),
    FILE("Файл"),
    BOOLEAN("Флаг (логическое выражение)"),
    LONG("Целое число"),
    BIG_DECIMAL("Число повышенной точности");
    String stringValue;

    WfVariableType(String string) {
        stringValue = string;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public static WfVariableType fromString(String string) {
        switch (string) {
            case "Время":
                return TIME;
            case "Группа":
                return GROUP;
            case "Дата":
                return DATE;
            case "Дата со временем":
                return DATE_TIME;
            case "Дробное число":
                return DOUBLE;
            case "Исполнитель":
                return EXECUTOR;
            case "Пользовательский тип данных":
                return USER_TYPE;
            case "Список":
                return LIST;
            case "Ссылка на процесс":
                return PROCESS_ID;
            case "Строка":
                return STRING;
            case "Текст":
                return TEXT;
            case "Форматированный текст":
                return FORMATTED_TEXT;
            case "Файл":
                return FILE;
            case "Флаг (логическое выражение)":
                return BOOLEAN;
            case "Целое число":
                return LONG;
            case "Число повышенной точности":
                return BIG_DECIMAL;
            default:
                throw new IllegalStateException("Unexpected value: " + string);
        }
    }
}