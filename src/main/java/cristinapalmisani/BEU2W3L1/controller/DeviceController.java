package cristinapalmisani.BEU2W3L1.controller;

import cristinapalmisani.BEU2W3L1.entities.Device;
import cristinapalmisani.BEU2W3L1.exception.BadRequestException;
import cristinapalmisani.BEU2W3L1.exception.NotFoundException;
import cristinapalmisani.BEU2W3L1.payloads.device.DeviceRequestDTO;
import cristinapalmisani.BEU2W3L1.payloads.device.DeviceResponseDTO;
import cristinapalmisani.BEU2W3L1.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Device> getDevices(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   @RequestParam(defaultValue = "id") String orderBy){
        return deviceService.getDevices(page, size, orderBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public DeviceResponseDTO createdDevice(@RequestBody @Validated DeviceRequestDTO body, BindingResult validation){
        if (validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Device device = deviceService.save(body);
            return new DeviceResponseDTO(device.getId());
        }

    }
    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable UUID id) {
        try {
            return deviceService.findById(id);
        } catch (Exception e) {
            throw new NotFoundException(id);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated DeviceRequestDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            return deviceService.findByIdAndUpdate(id, body);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID id) {
        try {
            deviceService.findByIdAnDelete(id);
        } catch (Exception e) {
            throw new NotFoundException(id);
        }
    }

    @GetMapping("/{deviceId}/assign/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device assignUser(@PathVariable UUID deviceId, @PathVariable UUID userId) {
        return deviceService.assignUser(deviceId, userId);
    }

    @GetMapping("{id}/putinmaintenance")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device putInMaintenance(@PathVariable UUID id) {
        return deviceService.putInMaintenance(id);
    }

    @GetMapping("{id}/setavailable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device putInAvailable(@PathVariable UUID id) {
        return deviceService.setAvailable(id);
    }

    @GetMapping("{id}/setdisused")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Device putInDisused(@PathVariable UUID id) {
        return deviceService.putInDisused(id);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Device> getDeviceByUser(@PathVariable UUID userId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @RequestParam(defaultValue = "id") String orderBy){
        return deviceService.getDeviceByUserId(userId, page, size, orderBy);
    }


}
