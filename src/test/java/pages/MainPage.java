package pages;

import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

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

    @Step("Проверка отображения заголовка страницы поиска")
    public void clickOtherFilter() {
        $x("//input[@value='accredited_it']/ancestor::label").click();
    }

    @Step("Переход по вакансии")
    public void clickToVacancy() {
        $x("(//div[contains(@class, 'vacancy-card')])[1]").click();
    }
}
