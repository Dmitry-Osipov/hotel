package task_3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class CarPart implements IProductPart {
    private int serialNumber;
}
