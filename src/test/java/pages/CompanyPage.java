package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.sleep;

public class CompanyPage {
    @Step("Получение названия компании")
    public String getCompanyName() {
        sleep(3000);
        var titleElement = $x(
                "//h1[@data-qa='company-header-title-name']"
        );

        return titleElement.getText();
    }
}
