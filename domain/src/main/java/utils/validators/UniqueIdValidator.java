package utils.validators;

import essence.Identifiable;

import java.util.List;

/**
 * Класс предназначен для валидации сущности внутри её сервиса по ID сущности.
 */
public final class UniqueIdValidator {
    private UniqueIdValidator() {
    }

    /**
     * Метод проводит валидацию, существует ли хотя бы 1 сущность внутри списка с совпадающим ID.
     * @param list Список сущностей.
     * @param id ID сущности.
     * @return true, если есть хотя бы одно совпадение
     * @param <T> Дженерик, наследуется от интерфейса Identifiable. Таким образом, он подходит только для тех сущностей,
     * что имеют ID.
     */
    public static <T extends Identifiable> boolean validateUniqueId(List<T> list, int id) {
        return list.stream().noneMatch(essence -> essence.getId() == id);
    }
}
