package service;

public interface AbstractService {
    /**
     * Метод устанавливает новый статус исполнения услуги.
     * @param status Новый статус.
     */
    void setStatus(ServiceStatusTypes status);

    /**
     * Метод получает статус исполнения услуги.
     * @return Статус.
     */
    ServiceStatusTypes getStatus();
}
