package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class StartEmployerPage {

    @Step("Страница содержит промотекст для работодателя")
    public void checkPromoTitle() {
        SelenideElement promo1 = $x("//*[@data-qa='employer-index-subtitle']");
        SelenideElement promo2 = $x("//a[@data-qa='employer-index-publish-vacancy']");

        if (promo1.is(visible)) {
            promo1.shouldHave(text(
                    """
                            Находите сотрудников среди тех, кто хочет у вас работать.
                            hh.ru — сервис № 1 по поиску сотрудников в России
                            """
            ));
        } else if (promo2.is(visible)) {
            promo2.shouldHave(text("Разместить вакансию"));
        }
    }

    @Step("Поиск резюме по фразе {0}")
    public void searchResumeByText(String text) {
        sleep(1000);
        $x("//input[@data-qa='resume-search_professional-role-input']").setValue(text);
        sleep(1000);
        ElementsCollection searchContainer = $$x("//*[@data-qa='cell-text-content']").filter(visible);
        if (!searchContainer.isEmpty()) {
            searchContainer.get(0).click();
        }
        $x("//*[@data-qa='onboarding-search-submit']").click();
    }

    @Step("Проверка отображения заголовка страницы поиска")
    public void checkSearchHeader(String title) {
        SelenideElement catalogHeader = $$x("//h4[@data-qa='title']").get(3);
        catalogHeader.shouldHave(text(title));
    }

    @Step("Проверка отображения заголовка страницы поиска при проблемах с результатом")
    public void checkFailSearchHeader() {
        SelenideElement failHeader = $x("//h3[@data-qa='title']");
        SelenideElement failHelper = $x("//*[@data-qa='form-helper-error']");
        sleep(1000);
        if (failHelper.is(visible)) {
            failHelper.shouldHave(exactText("Напишите, чтобы посмотреть кандидатов"));
        } else if (failHeader.is(visible)) {
            failHeader.shouldHave(exactText("Попробуйте поискать по‑другому"));
        }
    }
}
