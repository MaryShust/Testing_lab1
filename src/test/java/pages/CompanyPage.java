package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.sleep;

public class CompanyPage {
    @Step("Получение названия компании")
    public String getCompanyName() {
        sleep(5000);
        var titleElement = $x(
                "//*[@data-qa='company-header-title-name']"
        );
        titleElement.shouldBe(visible);
        String text = titleElement.getText();
        if (text == null || text.isBlank()) {
            text = titleElement.innerText();
        }
        return (text != null ? text : "").trim();
    }



    @Step("Получение вакансий компании")
    public void getVacancy() {
        sleep(5000);
        var titleElement = $x(
                "//*[@data-qa='employer-page-tabs-desktop-go-VACANCIES']"
        );
        titleElement.shouldBe(visible).click();
    }
}
