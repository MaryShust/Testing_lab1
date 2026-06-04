package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

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
    public void checkPromoTitle(String expectedTitle) {
        $x("//*[@data-qa='title-container']").shouldHave(text(expectedTitle));
    }

    @Step("Отображаются вакансии для города {0}")
    public void checkWorkInCompanyTitle(
            String title,
            String companyHeader,
            String professionHeader
    ) {
        $x("//*[@data-qa='main-page-anonymous-header']").shouldHave(text(title));
        $x("//a[@data-qa='index__work-in-company-header']").shouldHave(text(companyHeader));
        $x("//*[@data-qa='index__work-in-profession-header']").shouldHave(text(professionHeader));
    }
}
