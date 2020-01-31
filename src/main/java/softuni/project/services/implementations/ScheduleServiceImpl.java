package softuni.project.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import softuni.project.data.entities.Clothes;
import softuni.project.data.entities.PowerTools;
import softuni.project.data.entities.RealEstate;
import softuni.project.data.entities.Vehicle;
import softuni.project.data.repositories.ClothesRepository;
import softuni.project.data.repositories.PowerToolsRepository;
import softuni.project.data.repositories.RealEstateRepository;
import softuni.project.data.repositories.VehicleRepository;
import softuni.project.services.interfaces.ScheduleService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ClothesRepository clothesRepository;
    private final PowerToolsRepository powerToolsRepository;
    private final RealEstateRepository realEstateRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public ScheduleServiceImpl(ClothesRepository clothesRepository, PowerToolsRepository powerToolsRepository,
                               RealEstateRepository realEstateRepository, VehicleRepository vehicleRepository) {
        this.clothesRepository = clothesRepository;
        this.powerToolsRepository = powerToolsRepository;
        this.realEstateRepository = realEstateRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void checkAvailableOffers() {
        LocalDate localDate = LocalDate.now();
        System.out.println("Its work!");
        List<Clothes> clothes = clothesRepository.findAll().stream()
                .map(c -> {
                    if (c.getEndsOn().isBefore(localDate)){
                        c.setIsActive(false);
                    }
                    return c;
                }).collect(Collectors.toList());
        clothesRepository.saveAll(clothes);

        List<PowerTools> powerTools = powerToolsRepository.findAll().stream()
                .map(c -> {
                    if (c.getEndsOn().isBefore(localDate)){
                        c.setIsActive(false);
                    }
                    return c;
                }).collect(Collectors.toList());
        powerToolsRepository.saveAll(powerTools);

        List<RealEstate> realEstates = realEstateRepository.findAll().stream()
                .map(c -> {
                    if (c.getEndsOn().isBefore(localDate)){
                        c.setIsActive(false);
                    }
                    return c;
                }).collect(Collectors.toList());
        realEstateRepository.saveAll(realEstates);

        List<Vehicle> vehicles = vehicleRepository.findAll().stream()
                .map(c -> {
                    if (c.getEndsOn().isBefore(localDate)){
                        c.setIsActive(false);
                    }
                    return c;
                }).collect(Collectors.toList());
        vehicleRepository.saveAll(vehicles);


        System.out.println("It works");
    }
}
