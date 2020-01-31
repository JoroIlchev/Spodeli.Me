package softuni.project.services.interfaces;

import softuni.project.services.models.VehicleServiceModel;

import java.util.List;

public interface VehicleService {

    VehicleServiceModel saveVehicle(VehicleServiceModel model, String username);

    VehicleServiceModel findById(String id);

    List<VehicleServiceModel> extractAll();

    List<VehicleServiceModel> extractAllByUserId(String userId);

    void editOffer(VehicleServiceModel model, String id);

    void deleteById(String id);

    List<VehicleServiceModel> extractAllForModerator();

    void changeApproveStatus(String id, String isActive);
}
