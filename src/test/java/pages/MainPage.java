package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    @Step("Отображаются вакансии")
    public void checkHasVacancy() {
        $x("//*[@data-qa='vacancy-serp__vacancy']").shouldBe(visible);
    }

    @Step("Проверка отображения заголовка страницы поиска")
    public void checkSearchHeader(String title) {
        $x("//*[@data-qa='vacancies-search-header']").shouldHave(text(title));
    }

    @Step("Проверка успешности публикации резюме")
    public void checkSuccessPublicationResume() {
        $x("//*[@data-qa='suitable-vacancies-card-title']").shouldHave(text("Резюме опубликовано"));
    }

    @Step("Установка фильтра другие - аккредитация компании")
    public void clickOtherFilter() {
        $x("//input[@value='accredited_it']/ancestor::label").shouldBe(visible).click();
    }

    @Step("Переход по вакансии")
    public void clickToVacancy() {
        $x("(//div[contains(@class, 'vacancy-card')])[1]").click();
    }

    @Step("Установка фильтра формат работы гибрид")
    public void clickFormatJobFilter() {
        $x("//input[@value='HYBRID']/ancestor::label").click();
    }

    @Step("Установка фильтра часы работы")
    public void clickTimeFilter() {
        $x("//input[@value='HOURS_8']/ancestor::label").click();
    }

    @Step("Установка фильтра график работы")
    public void clickOpeningHoursFilter() {
        $x("//input[@value='FIVE_ON_TWO_OFF']/ancestor::label").click();
    }

    @Step("Установка фильтра тип занятости")
    public void clickTypeOfEmploymentFilter() {
        $x("//input[@value='PROJECT']/ancestor::label").click();
    }

    @Step("Установка фильтра опыт работы")
    public void clickExperienceFilter() {
        $x("//input[@value='between1And3']/ancestor::label").click();
    }

    @Step("Проверка типа занятости")
    public String checkTypeOfEmploymentFilter() {
        var employmentElement = $x("//*[@data-qa='vacancy-label-side-job']").shouldHave(text("Подработка"));
        return employmentElement.getText();
    }

    @Step("Проверка опыта работы")
    public String checkExperienceFilter() {
        var experienceElement = $x("//*[@data-qa='vacancy-serp__vacancy-work-experience-between1And3']").shouldHave(text("Опыт 1-3 года"));
        return experienceElement.getText();
    }

    @Step("Установка фильтра специализации")
    public void clickSpecializationFilter() {
        $x("//input[@value='44']/ancestor::label").click();
    }

    @Step("Установка фильтра специализации")
    public String checkSpecializationFilter() {
        var specializationElement = $x("//*[@data-qa='serp-item__title-text']").shouldHave(text("Инженер по качеству"));
        return specializationElement.getText();
    }
}
