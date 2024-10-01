package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.ModulesEntity;
import org.SchoolApp.Datas.Repository.ModulesRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModulesSeeder {

    private final ModulesRepository modulesRepository;

    public ModulesSeeder(ModulesRepository modulesRepository) {
        this.modulesRepository = modulesRepository;
    }

    @PostConstruct
    public void seedModules() {
        if (modulesRepository.count() == 0) { // Only seed if no modules exist
            createModule("Web Basics", "Introduction to HTML, CSS, and JavaScript", 30);
            createModule("Advanced JavaScript", "Deep dive into ES6 and asynchronous programming", 45);
        }
    }

    private void createModule(String nom, String description, int dureeAcquisition) {
        ModulesEntity module = new ModulesEntity();
        module.setNom(nom);
        module.setDescription(description);
        module.setDuree_acquisition(dureeAcquisition);
        modulesRepository.save(module);
    }
}
