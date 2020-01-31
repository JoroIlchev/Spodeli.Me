package softuni.project.services.interfaces;

import softuni.project.services.models.ClothesServiceModel;

import java.util.List;

public interface ClothesService {
    ClothesServiceModel saveClothes(ClothesServiceModel model, String username);
    List<ClothesServiceModel> extractAll();
    ClothesServiceModel findById(String id);
    List<ClothesServiceModel> extractAllByUserId(String userId);
    void editOffer(ClothesServiceModel clothesServiceModel, String id);
    void deleteById(String id);
    List<ClothesServiceModel> extractAllForModerator();
    void changeApproveStatus(String id, String isActive);

}
