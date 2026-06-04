package pages;

import io.qameta.allure.Step;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class HeaderPartPage {

    @Step("Открыть экран профиля")
    public void openProfile() {
        $x("//*[@data-qa='mainmenu_profileAndResumes']").click();
    }

    @Step("Клик по ссылке 'Работодателям'")
    public void clickEmployerLink() {
        $x("//*[@data-qa='userTypeSegmentedEmployer']/ancestor::label").click();
    }

    @Step("Выбираем город {0}")
    public void changeRegion(String city) {
        $x("//*[@data-qa='geoSwitcher-button']").click();
        $(byText(city)).click();
    }

    @Step("Открыть экран авторизации")
    public void openAuth() {
        $x("//a[@data-qa='login']").click();
    }

    @Step("Выход")
    public void logout() {
        openMenu();
        $x("//button[@data-qa='mainmenu_logoffUser']").click();
    }

    @Step("Открыть экран создания резюме")
    public void openCreateResume() {
        $x("//a[@data-qa='mainmenu_createResume']").click();
    }

    @Step("Открыть меню")
    private void openMenu() {
        $x("//*[@data-qa='mainmenu_applicantProfileDesktopDrop']").click();
    }
}
