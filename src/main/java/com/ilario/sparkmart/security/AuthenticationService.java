package com.ilario.sparkmart.security;

import com.ilario.sparkmart.exceptions.addresses.AddressNotFoundException;
import com.ilario.sparkmart.exceptions.roles.RoleNotFoundException;
import com.ilario.sparkmart.exceptions.users.GenderNotFoundException;
import com.ilario.sparkmart.exceptions.users.UserEmailAlreadyInUseException;
import com.ilario.sparkmart.models.Address;
import com.ilario.sparkmart.models.User;
import com.ilario.sparkmart.models.Wishlist;
import com.ilario.sparkmart.repositories.IAddressRepository;
import com.ilario.sparkmart.repositories.IUserRepository;
import com.ilario.sparkmart.repositories.IWishlistRepository;
import com.ilario.sparkmart.security.misc.AuthenticationRequest;
import com.ilario.sparkmart.security.misc.AuthenticationResponse;
import com.ilario.sparkmart.security.misc.RegisterRequest;
import com.ilario.sparkmart.security.misc.enums.Gender;
import com.ilario.sparkmart.security.misc.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUserRepository userRepository;
    private final IAddressRepository addressRepository;
    private final IWishlistRepository wishlistRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) {
        var existingAddress = addressRepository.findAddressByStreetAddressAndCity("", "");
        try {
            var existingUser = userRepository.findByEmail(request.email());
            if (existingUser.isPresent()) {
                throw  new UserEmailAlreadyInUseException("ERROR: Email already in use!");
            }
            if(existingAddress.isEmpty()) {
                var newAddress = Address.builder()
                        .streetAddress("")
                        .country("")
                        .city("")
                        .postalCode("")
                        .province("")
                        .build();
                addressRepository.save(newAddress);
            }
            var addressOptional = addressRepository.findAddressByStreetAddressAndCity("", "");
            if(addressOptional.isEmpty()) {
                throw new AddressNotFoundException("ERROR: Default address not found!");
            }
            var address = addressOptional.get();
            var wishlist = new Wishlist();
            var user = User.builder()
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .phoneNumber(request.phoneNumber())
                    .wishlist(wishlist)
                    .address(address)
                    .isDisabled(false)
                    .build();
            switch (request.role().toUpperCase()) {
                case "CUSTOMER" -> user.setRole(Role.CUSTOMER);
                case "EMPLOYEE" -> user.setRole(Role.EMPLOYEE);
                default -> throw new RoleNotFoundException("ERROR: Role not found.");
            }
            switch (request.gender().toUpperCase()) {
                case "MALE" -> user.setGender(Gender.MALE);
                case "FEMALE" -> user.setGender(Gender.FEMALE);
                default -> throw new GenderNotFoundException("ERROR: Gender not found.");
            }
            wishlist.setUser(user);
            address.getUsers().add(user);
            userRepository.save(user);
            wishlistRepository.save(wishlist);
            var jwtToken = jwtService.generateToken(user);
            return new AuthenticationResponse(jwtToken);
        } catch (UserEmailAlreadyInUseException | RoleNotFoundException | AddressNotFoundException | GenderNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = userRepository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
