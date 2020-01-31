package softuni.project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.project.data.entities.Vehicle;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findAllByIsActiveTrueAndIsApprovedIsTrueOrderByTimeStampDesc();
    List<Vehicle> findAllByOrderByTimeStampDesc();
    List<Vehicle> findAllByUser_IdOrderByTimeStampDesc(String userId);
}
