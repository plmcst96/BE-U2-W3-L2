package cristinapalmisani.BEU2W3L1.services;

import cristinapalmisani.BEU2W3L1.entities.Device;
import cristinapalmisani.BEU2W3L1.entities.User;
import cristinapalmisani.BEU2W3L1.exception.BadRequestException;
import cristinapalmisani.BEU2W3L1.exception.NotFoundException;
import cristinapalmisani.BEU2W3L1.payloads.device.DeviceRequestDTO;
import cristinapalmisani.BEU2W3L1.repositories.DeviceRepositoryDAO;
import cristinapalmisani.BEU2W3L1.repositories.UserRepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepositoryDAO deviceRepositoryDAO;
    @Autowired
    private UserRepositoryDAO userRepositoryDAO;


    private boolean isValidCategory(String category) {
        List<String> allowedCategories = Arrays.asList("smartphone", "tablet", "laptop", "smartwatch");
        return allowedCategories.contains(category);
    }
    public Device save(DeviceRequestDTO body){
        if (isValidCategory(body.typeDevice())){
            Device device = new Device();
            device.setTypeDevice(body.typeDevice());
            device.setState("available");
            return deviceRepositoryDAO.save(device);
        } else {
            throw  new BadRequestException("Device category '" + body.typeDevice() + "' is not allowed!");
        }
    }

    public Page<Device> getDevices(int page, int size, String orderBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return deviceRepositoryDAO.findAll(pageable);
    }

    public Device findById(UUID id){
        return deviceRepositoryDAO.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public void findByIdAnDelete(UUID id){
        Device device = this.findById(id);
        deviceRepositoryDAO.delete(device);
    }

    public Device findByIdAndUpdate(UUID id, DeviceRequestDTO body){
        Device device = this.findById(id);
        validateUpdateTypeDevice(device, body.typeDevice());
        validateUpdateState(device, body.state());
        return deviceRepositoryDAO.save(device);
    }
    private void validateUpdateState(Device device, String newState) {
        List<String> allowedStates = Arrays.asList("available", "assigned", "maintenance", "disused");
        if (!allowedStates.contains(newState)) {
            throw new BadRequestException("Device state '" + newState + "' is not allowed");
        }
        device.setState(newState);
    }

    private void validateUpdateTypeDevice(Device device, String newCategory) {
        List<String> allowedCategories = Arrays.asList("smartphone", "tablet", "laptop", "smartwatch");
        if (!allowedCategories.contains(newCategory)) {
            throw new BadRequestException("Device category '" + newCategory + "' is not allowed");
        }
        device.setTypeDevice(newCategory);
    }

    public Page<Device> getDeviceByUserId(UUID id, int page, int size, String orderBy){
        User user = userRepositoryDAO.findById(id).orElseThrow(()-> new NotFoundException(id));
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return  deviceRepositoryDAO.getDeviceByUser(user, pageable);
    }

    public Device assignUser(UUID deviceId, UUID userId){
        User user = userRepositoryDAO.findById(userId).orElseThrow(()-> new NotFoundException(userId));
        Device device = this.findById(deviceId);
        device.setState("assigned");
        device.setUser(user);
        return deviceRepositoryDAO.save(device);
    }

    public Device putInMaintenance(UUID deviceId) {
        Device device = deviceRepositoryDAO.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
        device.setState("maintenance");
        return deviceRepositoryDAO.save(device);
    }

    public Device putInDisused(UUID deviceId) {
        Device device = deviceRepositoryDAO.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
        device.setState("disused");
        if (device.getUser() != null) {
            device.setUser(null);
        }
        return deviceRepositoryDAO.save(device);
    }

    public Device setAvailable(UUID deviceId) {
        Device device = deviceRepositoryDAO.findById(deviceId).orElseThrow(() -> new NotFoundException(deviceId));
        device.setState("available");
        if (device.getUser() != null) {
            device.setUser(null);
        }
        return deviceRepositoryDAO.save(device);
    }

   public Page<Device> getDeviceByUserAndType(String typeDevice, UUID userId, int page, int size, String orderBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        User user = userRepositoryDAO.findById(userId).orElseThrow(()-> new NotFoundException(userId));
        Device device = new Device();
        device.setTypeDevice(device.getTypeDevice());
        device.setUser(user);
        return deviceRepositoryDAO.getByTypeDeviceAndUser(typeDevice, user, pageable);

   }
}
