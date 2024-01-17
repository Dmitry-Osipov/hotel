package ui;

public record MenuItem(String title, IAction action, Menu nextMenu) {

    public void doAction() {
        if (action != null) {
            action.execute();
        }
    }
}
