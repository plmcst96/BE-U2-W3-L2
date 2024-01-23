package cristinapalmisani.BEU2W3L1.repositories;

import cristinapalmisani.BEU2W3L1.entities.Device;
import cristinapalmisani.BEU2W3L1.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceRepositoryDAO extends JpaRepository<Device, UUID> {
    Page<Device> getDeviceByUser(User user, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.typeDevice = :typeDevice")
    Page<Device> getByTypeDevice(String typeDevice, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.state = 'maintenance'")
    Page<Device> getDevicesInMaintenance(Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.typeDevice = :typeDevice AND d.user = :user")
    Page<Device> getByTypeDeviceAndUser(String typeDevice, User user, Pageable pageable);

    @Query("SELECT d FROM Device d WHERE d.state = :state")
    Page<Device> getByState(String state, Pageable pageable);
}
