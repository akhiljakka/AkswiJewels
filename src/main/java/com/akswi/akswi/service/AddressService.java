package com.akswi.akswi.service;

import com.akswi.akswi.entity.Address;
import com.akswi.akswi.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address updateAddress(Long addressId, Address updatedAddress) {
        return addressRepository.findById(addressId)
                .map(address -> {
                    address.setStreet(updatedAddress.getStreet());
                    address.setCity(updatedAddress.getCity());
                    address.setState(updatedAddress.getState());
                    address.setZipCode(updatedAddress.getZipCode());
                    address.setCountry(updatedAddress.getCountry());
                    return addressRepository.save(address);
                })
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }
}
