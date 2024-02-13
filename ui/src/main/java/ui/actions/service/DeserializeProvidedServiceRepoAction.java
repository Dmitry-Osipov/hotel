package ui.actions.service;

import essence.provided.ProvidedService;
import repository.service.ProvidedServicesRepository;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.FileAdditionResult;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;

public class DeserializeProvidedServiceRepoAction implements IAction {
    private final ServiceService serviceService;

    public DeserializeProvidedServiceRepoAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getSerializeDirectory() + InputHandler.getFileNameFromUser();
            ProvidedServicesRepository repo = SerializationUtils.deserialize(ProvidedServicesRepository.class, path);
            for (ProvidedService providedService : repo.getProvidedServices()) {
                if (!serviceService.updateProvidedService(providedService)) {
                    serviceService.addProvidedService(providedService);
                    System.out.println("\nПроведённая услуга успешно добавлена");
                } else {
                    System.out.println("\nДанные по проведённой услуге успешно обновлены");
                }
            }
            System.out.println("\nДанные десериализованы успешно");
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
