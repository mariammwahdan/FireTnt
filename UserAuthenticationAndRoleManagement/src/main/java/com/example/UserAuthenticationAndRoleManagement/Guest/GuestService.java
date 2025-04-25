package com.example.UserAuthenticationAndRoleManagement.Guest;

import com.example.UserAuthenticationAndRoleManagement.User.DTO.CreateUserDTO;
import com.example.UserAuthenticationAndRoleManagement.User.DTO.UpdateUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GuestService {
    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public List<Guest> fetchAll() {
        return guestRepository.findAll();
    }

    public Guest fetchById(Long id) {
        return guestRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
    }

    public Guest create(CreateUserDTO dto) {
        if (guestRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        Guest guest = new Guest();
        guest.setEmail(dto.getEmail());
        guest.setPassword(dto.getPassword());
        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setPhoneNumber(dto.getPhoneNumber());
        return guestRepository.save(guest);
    }

    public Guest update(Long id, UpdateUserDTO dto) {
        Guest guest = fetchById(id);
        guest.setEmail(dto.getEmail());
        guest.setPassword(dto.getPassword());
        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setPhoneNumber(dto.getPhoneNumber());
        guest.setBanned(dto.isBanned());
        return guestRepository.save(guest);
    }

    public void delete(Long id) {
        guestRepository.deleteById(id);
    }
}
