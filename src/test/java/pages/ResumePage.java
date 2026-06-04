package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.Selenide.$$x;

public class ResumePage {

    @Step("Создать резюме")
    public void createResume(String speciality) {
        $x("//*[@data-qa='resume-profile-card-select-job']").click();
        sleep(1000);
        $x("//input[@data-qa='resume-profile-position-input']").setValue(speciality);

        sleep(1000);
        ElementsCollection searchContainer = $$x("//*[@data-qa='suggest-item-cell']").filter(visible);
        if (!searchContainer.isEmpty()) {
            searchContainer.get(0).click();
        }

        $x("//button[@data-qa='resume-profile-next-screen']").click();
        $x("//button[@data-qa='resume-profile-next-screen']").click();

        sleep(1000);
        SelenideElement university = $x("//input[@data-qa='profile-education-university-input']");
        if (university.is(visible)) {
            university.setValue("ИТМО");
        }
        SelenideElement educationYear = $x("//input[@data-qa='primary-education-form-year-input']");
        if (educationYear.is(visible)) {
            educationYear.setValue("2027");
        }

        $x("//button[@data-qa='resume-profile-next-screen']").click();
        $x("//button[@data-qa='resume-profile-next-screen']").click();
        $$x("//button[@data-qa='list-add']").get(1).click();
    }
}
