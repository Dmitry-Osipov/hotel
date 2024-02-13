package ui.actions.service;

import essence.service.AbstractService;
import repository.service.ServiceRepository;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.FileAdditionResult;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;

public class DeserializeServiceRepoAction implements IAction {
    private final ServiceService serviceService;

    public DeserializeServiceRepoAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getSerializeDirectory() + InputHandler.getFileNameFromUser();
            ServiceRepository repo = SerializationUtils.deserialize(ServiceRepository.class, path);
            for (AbstractService service : repo.getServices()) {
                if (!serviceService.updateService(service)) {
                    serviceService.addService(service);
                    System.out.println("\nУслуга успешно добавлеа");
                } else {
                    System.out.println("\nДанные по услуге успешно обновлены");
                }
            }
            System.out.println("\nДанные десериализованы успешно");
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
