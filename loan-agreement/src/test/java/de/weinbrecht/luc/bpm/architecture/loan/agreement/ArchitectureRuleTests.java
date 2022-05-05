package de.weinbrecht.luc.bpm.architecture.loan.agreement;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "de.weinbrecht.luc.bpm.architecture.loan.agreement")
class ArchitectureRuleTests {
    private final static String DOMAIN = "..domain..";
    private final static String USE_CASE = "..usecase..";
    private final static String ADAPTER = "..adapter..";

    @ArchTest
    static final ArchRule domain_use_case_should_not_import_adapters =
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().accessClassesThat().resideInAPackage(ADAPTER);

    @ArchTest
    static final ArchRule use_case_should_not_import_adapters =
            noClasses()
                    .that().resideInAPackage(USE_CASE)
                    .should().accessClassesThat().resideInAPackage(ADAPTER);
}
