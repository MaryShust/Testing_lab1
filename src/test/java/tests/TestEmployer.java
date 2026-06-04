package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("Проверка функционала работодателя")
public class TestEmployer extends TestBase {

    @Test
    @Tag("UI")
    @DisplayName("Проверка промотекста для работодателя")
    public void checkTitleForEmployers() {
        headerPartPage.clickEmployerLink();
        startEmployerPage.checkPromoTitle();
    }

    @Test
    @Tag("UI")
    @DisplayName("Поиск резюме по профессии")
    public void searchVacanciesByProfession() {
        String search = "Тестировщик";
        headerPartPage.clickEmployerLink();
        startEmployerPage.searchResumeByText(search);
        startEmployerPage.checkSearchHeader("Подходящие кандидаты");
    }

    @Test
    @Tag("UI")
    @DisplayName("Поиск резюме по профессии не из пула захардкоженных профессий")
    public void searchVacanciesByAlternativeProfession() {
        String search = "Quality Assurance";
        headerPartPage.clickEmployerLink();
        startEmployerPage.searchResumeByText(search);
        startEmployerPage.checkSearchHeader("Подходящие кандидаты");
    }

    @Test
    @Tag("UI")
    @DisplayName("Поиск резюме по несуществующей профессии, но после поиска существующей")
    public void searchNotExistentVacanciesByProfessionAfterCorrectProfession() {
        String search1 = "Тестировщик";
        String search2 = "12345";
        headerPartPage.clickEmployerLink();
        startEmployerPage.searchResumeByText(search1);
        startEmployerPage.checkSearchHeader("Подходящие кандидаты");
        startEmployerPage.searchResumeByText(search2);
        startEmployerPage.checkFailSearchHeader();
    }

    @Test
    @Tag("UI")
    @DisplayName("Поиск резюме по несуществующей профессии")
    public void searchNotExistentVacanciesByProfession() {
        String search = "Тестировщик12345";
        headerPartPage.clickEmployerLink();
        startEmployerPage.searchResumeByText(search);
        startEmployerPage.checkFailSearchHeader();
    }
}