package softuni.project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.project.data.entities.Clothes;

import java.util.List;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, String> {

    // TODO: 5.12.2019 г. For all repos the same query
    List<Clothes> findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc();
    List<Clothes> findAllByOrderByTimeStampDesc();
    List<Clothes> findAllByUser_IdOrderByTimeStampDesc(String userId);


}
