package task_2;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class Flower {
    private final Integer cost;
    private final Color color;

    public Flower(Integer cost, Color color) {
        this.cost = cost;
        this.color = color;
    }
}
