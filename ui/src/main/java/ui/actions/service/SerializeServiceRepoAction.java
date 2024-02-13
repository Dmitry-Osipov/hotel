package ui.actions.service;

import repository.service.ServiceRepository;
import service.ServiceService;
import ui.actions.IAction;
import utils.InputHandler;
import utils.exceptions.ErrorMessages;
import utils.file.FileAdditionResult;
import utils.file.serialize.SerializationUtils;

import java.io.IOException;

public class SerializeServiceRepoAction implements IAction {
    private final ServiceService serviceService;

    public SerializeServiceRepoAction(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Override
    public void execute() {
        try {
            String path = FileAdditionResult.getSerializeDirectory() + InputHandler.getFileNameFromUser();
            String choice = InputHandler.getUserOverwriteChoice(path + ".json");
            if (choice.equals("да")) {
                ServiceRepository repo = serviceService.getServiceRepository();
                SerializationUtils.serialize(repo, path);
                System.out.println("\n" + FileAdditionResult.SUCCESS.getMessage());
            } else {
                System.out.println("\n" + FileAdditionResult.FAILURE.getMessage());
            }
        } catch (IOException e) {
            System.out.println("\n" + ErrorMessages.FILE_ERROR.getMessage());
        }
    }
}
