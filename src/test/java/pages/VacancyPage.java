package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class VacancyPage {

    @Step("Получение текста об аккредитации компании")
    public String getAccreditationText() {
        sleep(8000);

        var accreditationElement = $x(
            "//span[contains(text(), 'аккредитован')]"
        );

        return accreditationElement.shouldBe(visible).getText();
    }

    @Step("Получение текста об аккредитации компании")
    public String getFormatJob() {
        sleep(8000);

        var formatJobElement = $x(
                "//*[@data-qa='work-formats-text']"
        );

        return formatJobElement.shouldBe(visible).getText();
    }


}