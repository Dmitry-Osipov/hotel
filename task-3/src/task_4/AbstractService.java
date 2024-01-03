package task_4;

public interface AbstractService {
    void setStatus(ServiceStatusTypes status);
    ServiceStatusTypes getStatus();
}
