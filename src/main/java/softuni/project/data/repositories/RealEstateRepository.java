package softuni.project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.project.data.entities.RealEstate;

import java.util.List;
import java.util.Optional;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, String> {
    List<RealEstate> findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc();
    List<RealEstate> findAllByOrderByTimeStampDesc();
    List<RealEstate> findAllByUser_IdOrderByTimeStampDesc(String userId);
    Optional<RealEstate> findById(String id);
}
