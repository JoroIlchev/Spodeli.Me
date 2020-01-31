package softuni.project.data.entities.enums;

public enum Region {
    Blagoevgrad("Благоевград"),
    Burgas("Бургас"),
    Varna("Варна"),
    Veliko_Tarnovo("Велико Търново"),
    Vidin("Видин"),
    Vratsa("Враца"),
    Gabrovo("Габрово"),
    Dobrich("Добрич"),
    Kardzali("Кърджали"),
    Kustendil("Кюстендил"),
    Lovech("Ловеч"),
    Montana("Монтана"),
    Pazardjik("Пазарджик"),
    Pernik("Перник"),
    Pleven("Плевен"),
    Plovdiv("Пловдив"),
    Razgrad("Разград"),
    Ruse("Русе"),
    Silistra("Силистра"),
    Sliven("Сливен"),
    Smolian("Смолян"),
    Sofia("София"),
    Stara_Zagora("Стара Загора"),
    Tyrgovishte("Търговище"),
    Haskovo("Хасково"),
    Shumen("Шумен"),
    Yambol("Ямбол");


    private String name;
    Region(String name) {
       this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
