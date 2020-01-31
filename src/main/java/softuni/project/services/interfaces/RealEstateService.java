package softuni.project.services.interfaces;

import softuni.project.services.models.RealEstateServiceModel;

import java.util.List;

public interface RealEstateService {
    List<RealEstateServiceModel> extractAll();
    RealEstateServiceModel findById(String id);
    RealEstateServiceModel saveRealEstate(RealEstateServiceModel model, String username);

    List<RealEstateServiceModel> extractAllByUserId(String userId);

    void editOffer(RealEstateServiceModel realEstateServiceModel, String id);

    void deleteById(String id);

    List<RealEstateServiceModel> extractAllForModerator();

    void changeApproveStatus(String id, String isActive);
}
