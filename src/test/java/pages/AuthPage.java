package pages;

import io.qameta.allure.Step;
import java.util.Properties;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

public class AuthPage {

    @Step("Вход")
    public void loginByMailAndPassword(Properties props) {
        String mail = props.getProperty("test.user.mail");
        String password = props.getProperty("test.password");

        $x("//*[@data-qa='cell']").click();
        $x("//button[@data-qa='submit-button']").click();
        $x("//*[@data-qa='credential-type-EMAIL']/ancestor::label").click();

//        $x("//input[@data-qa='applicant-login-input-email']")
//                .shouldBe(visible, Duration.ofSeconds(10))
//                .setValue(mail);

        sleep(1000);
        $x("//input[@data-qa='applicant-login-input-email']").setValue(mail);
        $x("//button[@data-qa='expand-login-by-password']").click();
        sleep(1000);
        $x("//input[@data-qa='applicant-login-input-password']").setValue(password);
        $x("//button[@data-qa='submit-button']").click();
    }
}
