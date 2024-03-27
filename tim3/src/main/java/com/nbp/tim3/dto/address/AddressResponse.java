package com.nbp.tim3.dto.address;

import com.nbp.tim3.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    int id;
    String street;
    String municipality;
    String mapCoordinates;

    public AddressResponse(Address address) {
        street = address.getStreet();
        municipality = address.getMunicipality();
        mapCoordinates = address.getMapCoordinates();
        id = address.getId();
    }
}
