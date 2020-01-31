package softuni.project.data.entities.enums;

public enum GearBoxType {
    Automatic("Автоматична"),
    NonAutomatic("Ръчна");

    private String name;
    GearBoxType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
