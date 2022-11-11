package org.api.pets.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import lombok.SneakyThrows;
import org.api.config.TestEnvFactory;

import java.io.File;

public class PetsData {
    private static final Config config = TestEnvFactory.getInstance().getConfig();
    private static final String ADD_PET_FILE =
            config.getString("ADD_PET_FILE");

    @SneakyThrows
    public static AddPet getAddPetRequestBody() {
        File file =
                new File(System.getProperty("user.dir") + "/" + ADD_PET_FILE);
        return new ObjectMapper().readValue(file, AddPet.class);
    }
}
