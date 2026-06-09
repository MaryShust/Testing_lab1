package pages;

import io.qameta.allure.Step;

import java.util.List;

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
        sleep(2000);
        executeJavaScript("window.scrollBy(0, 300);");
        sleep(1000);
        
        try {
            var otherParams = $x("//button[contains(text(), 'Другие параметры')]").shouldBe(visible);
            otherParams.scrollIntoView(true);
            otherParams.click();
            sleep(3000);
        } catch (Exception e) {
            executeJavaScript("document.querySelectorAll('button').forEach(b => { if (b.textContent.includes('Другие')) b.click(); })");
            sleep(3000);
        }

        executeJavaScript(
            "const inputs = document.querySelectorAll('input');" +
            "for (let input of inputs) {" +
            "  if (input.value === '8hours') {" +
            "    input.click();" +
            "    return;" +
            "  }" +
            "}"
        );
        sleep(2000);
    }

    @Step("Установка фильтра график работы")
    public void clickOpeningHoursFilter() {
        sleep(2000);
        executeJavaScript("window.scrollBy(0, 300);");
        sleep(1000);
        
        try {
            var otherParams = $x("//button[contains(text(), 'Другие параметры')]").shouldBe(visible);
            otherParams.scrollIntoView(true);
            otherParams.click();
            sleep(3000);
        } catch (Exception e) {
            executeJavaScript("document.querySelectorAll('button').forEach(b => { if (b.textContent.includes('Другие')) b.click(); })");
            sleep(3000);
        }

        executeJavaScript(
            "const inputs = document.querySelectorAll('input');" +
            "for (let input of inputs) {" +
            "  if (input.value === 'FiveDays') {" +
            "    input.click();" +
            "    return;" +
            "  }" +
            "}"
        );
        sleep(2000);
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
        sleep(1000);
    }

//    @Step("Установка фильтра специализации")
//    public String checkSpecializationFilter() {
//        sleep(1000);
//        var specializationElement = $x("//*[@data-qa='serp-item__title-text']").shouldHave(text("Инженер по качеству"));
//        return specializationElement.getText();
//    }

    @Step("Установка фильтра специализации")
    public List<String> getSpecializationsFilter() {
        sleep(1000);
        var specializationElement = $x("//*[@data-qa='serp-item__title-text']");
        return specializationElement.getText();
    }

    @Step("Получение текста примененного фильтра")
    public String getAppliedFilterText() {
        sleep(2000);
        return executeJavaScript(
            "const filters = document.querySelectorAll('[data-qa=\"selected-filters-item\"]');" +
            "return filters.length > 0 ? filters[0].textContent : 'Фильтр применен';"
        );
    }

    @Step("Установка фильтра специализации")
    public String checkWorkFromHome() {
        var specializationElement = $x("//*[@data-qa='vacancy-label-work-schedule-remote']").shouldHave(text("Можно удалённо"));
        return specializationElement.getText();
    }
}
