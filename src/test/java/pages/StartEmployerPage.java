package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class StartEmployerPage {

    @Step("Страница содержит промотекст для работодателя")
    public void checkPromoTitle() {
        sleep(1000);
        SelenideElement promo1 = $x("//*[@data-qa='employer-index-subtitle']");
        SelenideElement promo2 = $x("//a[@data-qa='employer-index-publish-vacancy']");

        if (promo1.is(visible)) {
            Assertions.assertTrue(promo1.getText().contains("Находите сотрудников среди тех, кто хочет у вас работать."),
                    "Заголовок должен содержать другой текст");
        } else if (promo2.is(visible)) {
            Assertions.assertTrue(promo2.getText().equals("Разместить вакансию"),
                    "Заголовок должен содержать: Разместить вакансию");
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

    @Step("Получить текст заголовка страницы поиска")
    public String getSearchHeader() {
        sleep(1000);
        SelenideElement catalogHeader = $$x("//h4[@data-qa='title']").get(3);
        catalogHeader.shouldBe(visible);
        String text = catalogHeader.getText();
        if (text == null || text.isBlank()) {
            text = catalogHeader.innerText();
        }
        return (text != null ? text : "").trim();
    }

    @Step("Проверка отображения заголовка страницы поиска при проблемах с результатом")
    public void checkFailSearchHeader() {
        sleep(1000);
        SelenideElement failHeader = $x("//h3[@data-qa='title']");
        SelenideElement failHelper = $x("//*[@data-qa='form-helper-error']");
        if (failHelper.is(visible)) {
            Assertions.assertTrue(failHelper.getText().equals("Напишите, чтобы посмотреть кандидатов"),
                    "Заголовок должен содержать: Напишите, чтобы посмотреть кандидатов");
        } else if (failHeader.is(visible)) {
            Assertions.assertTrue(failHeader.getText().equals("Попробуйте поискать по‑другому"),
                    "Заголовок должен содержать: Попробуйте поискать по‑другому");
        }
    }
}
