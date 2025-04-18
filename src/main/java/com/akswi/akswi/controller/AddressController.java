package com.akswi.akswi.controller;

import com.akswi.akswi.entity.Address;
import com.akswi.akswi.entity.User;
import com.akswi.akswi.service.AddressService;
import com.akswi.akswi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;

    @PostMapping
    public Address addAddress(@RequestParam Long userId, @RequestBody Address address) {
        // Retrieve the user by userId and link the address

        User user = userService.getUserById(userId);
        address.setUser(user);
        return addressService.addAddress(address);
    }

    @PutMapping("/{addressId}")
    public Address updateAddress(@PathVariable Long addressId, @RequestBody Address updatedAddress) {
        return addressService.updateAddress(addressId, updatedAddress);
    }

    @DeleteMapping("/{addressId}")
    public void deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
    }
}
