package task3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssemblyLine implements IAssemblyLine {
    private ILineStep firstStep;
    private ILineStep secondStep;
    private ILineStep thirdStep;

    public AssemblyLine(ILineStep ...steps) {
        if (!checkSteps(steps)) {
            System.out.println("Вы ввели недопустимое количество шагов линии. " +
                    "Будут установлены шаги линии по умолчанию.");
            firstStep = new EngineLineStep();
            secondStep = new ChassisLineStep();
            thirdStep = new BodyLineStep();
        } else {
            firstStep = steps[0];
            secondStep = steps[1];
            thirdStep = steps[2];
        }
    }

    /**
     * Служебный метод проверяет, поступило ли 3 сброчных линии.
     * @param steps Список сборочных линий.
     * @return true, если сборочных линий поступило 3, иначе false.
     */
    private static boolean checkSteps(ILineStep[] steps) {
        return steps.length == 3;
    }

    /**
     * Метод собирает продукт по частям.
     * @param product Продукт, который требуется собрать.
     * @return Собранный продукт.
     */
    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("Начинаем сборку продукта...\n");

        product.installFirstPart(firstStep.buildProductPart());
        product.installSecondPart(secondStep.buildProductPart());
        product.installThirdPart(thirdStep.buildProductPart());

        System.out.println("Сборка продукта окончена!\n");
        return product;
    }
}
