package com.monkila_tech.config;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.monkila_tech.mokopay_backend.models.ERole;
import com.monkila_tech.mokopay_backend.models.Role;
import com.monkila_tech.mokopay_backend.models.User;
import com.monkila_tech.mokopay_backend.repository.RoleRepository;
import com.monkila_tech.mokopay_backend.repository.TransactionRepository;
import com.monkila_tech.mokopay_backend.repository.UserRepository;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Override
    public void run(String... args) {


        if (activeProfile.equals("dev") || activeProfile.equals("test")) {
            userRepository.deleteAll();
            System.out.println("User supprimé avec success");

            insererRoleSiNonExistant(ERole.ROLE_ADMIN);
            insererRoleSiNonExistant(ERole.ROLE_BUYER);
            insererRoleSiNonExistant(ERole.ROLE_SELLER);
            insererRoleSiNonExistant(ERole.ROLE_USER);

            insererAdminParDefaut();
        }
        
    }

    private void insererRoleSiNonExistant(ERole nom) {
        Optional<Role> roleExistant = roleRepository.findByName(nom);
        if (roleExistant.isEmpty()) {
            Role role = new Role();
            role.setName(nom);
            roleRepository.save(role);
            System.out.println("Role inséré : " + nom);
        } else {
            System.err.println("Role déjà existant : " + nom);
        }
    }

    // private void insererTransactionStatusSiNonExistant(TransactionStatus nom) {
    //     List<Transaction> statusExistant = transactionRepository.findByStatus(nom);
    //     if (.isEmpty()) {
    //         Role role = new Role();
    //         role.setName(nom);
    //         roleRepository.save(role);
    //         System.out.println("Role inséré : " + nom);
    //     } else {
    //         System.err.println("Role déjà existant : " + nom);
    //     }
    // }

    private void insererAdminParDefaut() {
        String username = "monkila";
        String email = "jnkiwa25@gmail.com";

        if (userRepository.findByUsername(username).isEmpty()) {
            User admin = new User();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode("405522")); 
            admin.setStatus(true);
            admin.setPhone("+243816717846");

            Role roleAdmin = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN non trouvé"));

            admin.getRoles().add(roleAdmin);
            userRepository.save(admin);

            System.out.println("Utilisateur admin créé avec succès !");
        } else {
            System.err.println("Admin déjà existant.");
        }
    }
}

