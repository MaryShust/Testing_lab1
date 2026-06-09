package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class VacancyPage {

    @Step("Получение названия вакансии")
    public String getVacancyTitle() {
        sleep(5000);
        var titleElement = $x("//h1[@data-qa='vacancy-title']");
        titleElement.shouldBe(visible);
        String text = titleElement.getText();
        if (text == null || text.isBlank()) {
            text = titleElement.innerText();
        }
        return (text != null ? text : "").trim();
    }

    @Step("Получение текста об аккредитации компании")
    public String getAccreditationText() {
        sleep(8000);

        var accreditationElement = $x(
            "//span[contains(text(), 'аккредитован')]"
        );

        return accreditationElement.shouldBe(visible).getText();
    }

    @Step("Получение текста об аккредитации компании")
    public String getCompanyName() {
//        sleep(80000);
        sleep(5000);


        var titleElement = $x(
                "//[@data-qa='employer__search-saved']"
        );
        System.out.println("LOL titleElement=" + titleElement.getText());

//        var titleElement = $x(
//                "//*[@data-qa='vacancy-company-name']"
//        );
//
//        var atitleElement = $x(
//                "//*[@data-qa='company-header-title-form']"
//        );
//
//        System.out.println("LOL atitleElement=" + atitleElement.getText());
//        titleElement.shouldBe(visible);
//        String text = titleElement.getText();
//        if (text == null || text.isBlank()) {
//            text = titleElement.innerText();
//        }
//        return (text != null ? text : "").trim();
        return "";
    }
    @Step("Получение текста о формате работы")
    public String getFormatJob() {
        sleep(5000);

        var formatJobElement = $x(
            "//span[contains(text(), 'Гибрид') or contains(text(), 'Удалённо') or contains(text(), 'Офис')]"
        );

        return formatJobElement.shouldBe(visible).getText();
    }

    @Step("Получение текста о рабочих часах")
    public String getWorkingHours() {
        sleep(5000);

        var hoursElement = $x(
            "//span[contains(text(), '8 часов') or contains(text(), 'часов в день')]"
        );

        return hoursElement.shouldBe(visible).getText();
    }

    @Step("Получение текста о графике работы")
    public String getWorkSchedule() {
        sleep(5000);

        var scheduleElement = $x(
            "//span[contains(text(), '5/2') or contains(text(), 'Полный день')]"
        );

        return scheduleElement.shouldBe(visible).getText();
    }
}