package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class StartPage {

    @Step("Открыта главная страница")
    public void openPage() {
        open("/");
        $x("//body").shouldBe(visible);
    }

    @Step("Закрыть cookie-баннер если есть")
    public void closeCookieBanner() {
        SelenideElement cookieBtn = $x("//button[@data-qa='cookies-policy-informer-accept']");
        if(cookieBtn.is(visible)) {
            cookieBtn.click();
        }
    }

    @Step("Закрыть popup авторизации если есть")
    public void closeAuthPopup() {
        sleep(1000);
        if ($x("//button[@data-qa='signup-modal-close']").is(visible)) {
            $x("//button[@data-qa='signup-modal-close']").shouldBe(visible).click();
        }
    }

    @Step("Поиск по фразе {0}")
    public void searchVacanciesByText(String vacancy) {
        sleep(1000);
        $x("//*[@data-qa='search-input']").setValue(vacancy).pressEnter();
    }

    @Step("Открыть фильтры")
    public void openFilter() {
        sleep(1000);
        $x("//*[@data-qa='advanced-search']").click();
    }

    @Step("Настройка фильтров")
    public void setFilter(String vacancy, int price, String excluded) {
        sleep(1000);
        $x("//*[@data-qa='vacancysearch__keywords-input']").setValue(vacancy);
        $x("//*[@data-qa='vacancysearch__keywords-excluded-input']").setValue(excluded);
        $x("//*[@data-qa='advanced-search-salary']").setValue(String.valueOf(price));
        $x("//*[@data-qa='advanced-search-submit-button']").click();
    }

    @Step("Страница содержит промотекст для соискателя")
    public String getPromoTitle() {
        return $x("//*[@data-qa='title-container']").getText();
    }

    @Step("Отображаются вакансии для города")
    public String getWorkInHeaderTitle() {
        return $x("//*[@data-qa='main-page-anonymous-header']").getText();
    }

    @Step("Отображаются компаний для города")
    public String getWorkInCompanyTitle() {
        return $x("//a[@data-qa='index__work-in-company-header']").getText();
    }

    @Step("Отображаются профессий для города")
    public String getWorkInProfessionTitle() {
        return $x("//*[@data-qa='index__work-in-profession-header']").getText();
    }

    @Step("Кликнуть на блок вакансии дня")
    public String clickBlocVacancyOfDay() {
        sleep(2000);
        var element = $x("(//*[@data-qa='vacancy-item-desktop'])");
        String title = element.getText();
        element.click();
        return title;
    }

    @Step("Кликнуть на вакансию дня")
    public void clickVacancyOfDay() {
        sleep(3000);
        var linkElement = $x("//*[@data-qa='vacancy_of_the_day_title']").shouldBe(visible);
        ((JavascriptExecutor) getWebDriver())
            .executeScript("arguments[0].scrollIntoView({block: 'center'});", linkElement);
        sleep(500);
        linkElement.click();
    }

    @Step("Получить URL вакансии дня")
    public String getVacancyOfDayUrl() {
        sleep(2000);
        var linkElement = $x("//a[.//*[@data-qa='vacancy_of_the_day_title']]").shouldBe(visible);
        return linkElement.getAttribute("href");
    }

    @Step("Кликнуть на первую компанию в списке")
    public String clickCompanyOfDay() {
        sleep(2000);
        var element = $x("(//*[@data-qa='company-item-desktop'])");
        String title = element.getText();
        element.click();
        return title;
    }

    @Step("Кликнуть на первую вакансию в поиске")
    public String clickFirstCompanyOfDay() {
        sleep(2000);
        var element = $x("(//*[@data-qa='company-of-the-day-name'])[1]");
        String title = element.getText();
        element.click();
        return title;
    }

    @Step("Открыть работа из дома")
    public void clickWorkFromHome() {
        sleep(1000);
        $x("//*[@data-qa='remote-item-desktop']").click();
    }

    @Step("Открыть фильтры")
    public String clickWorkInCompany() {
        sleep(1000);
        var element = $x("//*[@id='7172']");
        element.shouldHave(text("Лента, федеральная розничная сеть")).click();
        return element.getText();
    }
}
