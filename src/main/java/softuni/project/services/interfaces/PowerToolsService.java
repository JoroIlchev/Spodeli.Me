package softuni.project.services.interfaces;

import softuni.project.services.models.PowerToolsServiceModel;

import java.util.List;

public interface PowerToolsService {
    PowerToolsServiceModel savePowerTools(PowerToolsServiceModel model, String username);

    PowerToolsServiceModel findById(String id);

    List<PowerToolsServiceModel> extractAll();

    List<PowerToolsServiceModel> extractAllByUserId(String userId);

    void editOffer(PowerToolsServiceModel powerToolsServiceModel, String id);

    void deleteById(String id);

    List<PowerToolsServiceModel> extractAllForModerator();

    void changeApproveStatus(String id, String isActive);
}
