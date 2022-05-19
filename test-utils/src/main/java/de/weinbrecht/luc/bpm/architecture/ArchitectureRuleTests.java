package de.weinbrecht.luc.bpm.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public interface ArchitectureRuleTests {
    String DOMAIN = "..domain..";
    String USE_CASE = "..usecase..";
    String ADAPTER = "..adapter..";

    String basePackage();

    @Test
    default void domain_use_case_should_not_import_adapters() {
        JavaClasses classes = new ClassFileImporter().importPackages(basePackage());

        ArchRule rule = noClasses()
                .that().resideInAPackage(DOMAIN)
                .should().accessClassesThat().resideInAPackage(ADAPTER);

        rule.check(classes);
    }

    @Test
    default void use_case_should_not_import_adapters() {
        JavaClasses classes = new ClassFileImporter().importPackages(basePackage());

        ArchRule rule = noClasses()
                .that().resideInAPackage(USE_CASE)
                .should().accessClassesThat().resideInAPackage(ADAPTER);

        rule.check(classes);
    }
}
