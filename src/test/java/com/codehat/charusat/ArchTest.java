package com.codehat.charusat;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.codehat.charusat");

        noClasses()
            .that()
            .resideInAnyPackage("com.codehat.charusat.service..")
            .or()
            .resideInAnyPackage("com.codehat.charusat.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.codehat.charusat.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
