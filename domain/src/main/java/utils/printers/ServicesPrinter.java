package utils.printers;

import essence.service.AbstractService;
import utils.exceptions.ErrorMessages;
import utils.exceptions.NoEntityException;

import java.util.List;

/**
 * Класс является финальным и предоставляет статический метод для вывода списка услуг в консоль или сообщения об
 * отсутствии услуг.
 */
public final class ServicesPrinter {
    private ServicesPrinter() {
    }

    /**
     * Метод выводит в консоль список услуг или сообщение об отсутствии услуг.
     * @param services Список услуг.
     * @throws NoEntityException Ошибка связана с отсутствием услуг.
     */
    public static void printServices(List<AbstractService> services) throws NoEntityException {
        if (services.isEmpty()) {
            throw new NoEntityException(ErrorMessages.NO_SERVICES.getMessage());
        } else {
            for (int i = 0; i < services.size(); i++) {
                System.out.println(i+1 + ". " + services.get(i));
            }
        }
    }
}
