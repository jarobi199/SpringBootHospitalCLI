package io.hospital;

import io.hospital.authentication.SessionContext;
import io.hospital.interfaces.IRoleMenu;
import io.hospital.menu.AuthenticationMenu;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class HospitalApplication implements CommandLineRunner {

    @Autowired
    private AuthenticationMenu authenticationMenu;

     static void main(String[] args) {
        new SpringApplicationBuilder(HospitalApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    public void run(String @NonNull ... args) {
        System.out.println("=============================================================");
        System.out.println("   Welcome To The Hospital Patient Management Application!");
        System.out.println("=============================================================");
        System.out.println();

        authenticationMenu.show();
        IRoleMenu menu = SessionContext.getCurrentUser().getMenu();
        menu.show();
    }
}
