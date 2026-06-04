package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

    @Step("Закрыть whatsNew")
    public void closeWhatsNew() {
        SelenideElement whatsNew = $x("//*[@data-qa='whats-new-modal-close']");
        if (whatsNew.is(visible)) {
            whatsNew.click();
        }
    }

    @Step("Удалить резюме")
    public void removeResume(String speciality) {
        ElementsCollection resumeCollection = $$x("//*[@data-qa='resume']");
        for (SelenideElement resume : resumeCollection) {
            if (resume.getText().contains(speciality)) {
                $x("//*[contains(@data-qa,'resume-card-link-')]").click();
                $x("//*[@data-qa='resume-delete']").click();
                $x("//*[@data-qa='resume-delete-confirm']").click();
            }
        }
    }
}
