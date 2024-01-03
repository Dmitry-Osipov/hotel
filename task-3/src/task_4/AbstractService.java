package task_4;

import lombok.Getter;

@Getter
public abstract class AbstractService {
    private final int id;
    private int price;
    private final int MIN_PRICE = 1500;

    public AbstractService(int id, int price) {
        this.id = id;
        setPrice(price);
    }

    public void setPrice(int price) {
        if (checkPrice(price)) this.price = price;
        else this.price = MIN_PRICE;
    }

    private boolean checkPrice(int price) { return this.price < price && MIN_PRICE < price; }
}
