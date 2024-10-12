package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import data.DataHelper;
import data.SQLHelper;
import page.OrderPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebServiceTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
        SQLHelper.clearDB();
    }

    @Test
    @DisplayName("Купить. Оплата одобренной картой")
    void ShouldPayIfCardIsActive() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.notificationIfSuccess();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Купить. Оплата отклонённой картой")
    void ShouldNotPayIfCardIsInactive() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidInactiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.notificationIfFailure();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Купить. Поле 'CVC/CVV' не заполнено")
    void ShouldMessageIfCVCIsNullByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.continueClick();
        paymentPage.messageIfNull();
    }

    @Test
    @DisplayName("Купить. Поле 'Владелец' не заполнено")
    void ShouldMessageIfHolderIsNullByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfNull();
    }

    @Test
    @DisplayName("Купить. Поле 'Месяц' не заполнено")
    void ShouldMessageIfMonthIsNullByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfNull();
    }

    @Test
    @DisplayName("Купить. Поле 'Номер карты' не заполнено")
    void ShouldMessageIfCardNumberIsNullByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfNull();
    }

    @Test
    @DisplayName("Купить. CVC из двух символов")
    void ShouldMessageIfCVCIsShortByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        String cvc = DataHelper.generateNumbers(2);
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(cvc);
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
    }

    @Test
    @DisplayName("Купить. Код CVC спецсимволами")
    void ShouldMessageIfCVCBySymbolsByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateSymbol());
        paymentPage.continueClick();
        paymentPage.messageIfNull();
        assertEquals("", paymentPage.getFieldValue(4));
    }

    @Test
    @DisplayName("Купить. Код CVC латиницей")
    void ShouldMessageIfCVCByLettersEnByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateStringInEn(3));
        paymentPage.continueClick();
        paymentPage.messageIfNull();
        assertEquals("", paymentPage.getFieldValue(4));
    }

    @Test
    @DisplayName("Купить. Код CVC кириллицей")
    void ShouldMessageIfCVCByLettersRuByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateStringInRu(3));
        paymentPage.continueClick();
        paymentPage.messageIfNull();
        assertEquals("", paymentPage.getFieldValue(4));
    }

    @Test
    @DisplayName("Купить. Поле 'Владелец' цифрами")
    void ShouldMessageIfHolderInNumbersByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateNumbers(7));
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(3));
    }

    @Test
    @DisplayName("Купить. Поле 'Владелец' спецсимволами")
    void ShouldMessageIfHolderInSymbolsByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateSymbol());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(3));
    }

    @Test
    @DisplayName("Купить. Поле 'Владелец' кириллицей")
    void ShouldPayIfHolderInRussian() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInRu());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.notificationIfSuccess();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Купить. Поле 'Год' спецсимволами")
    void ShouldMessageIfYearWithSymbolsByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateSymbol());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(2));
    }

    @Test
    @DisplayName("Купить. Поле 'Год' кириллицей")
    void ShouldMessageIfYearWithRuByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateStringInRu(2));
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(2));
    }

    @Test
    @DisplayName("Купить. Поле 'Год' латиницей")
    void ShouldMessageIfYearWithEnByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateStringInEn(2));
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(2));
    }

    @Test
    @DisplayName("Купить. Поле 'Год' из одной цифры")
    void ShouldMessageIfYearIsShortByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateNumbers(1));
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
    }

    @Test
    @DisplayName("Купить. Некорректный год")
    void ShouldMessageIfYearIsInvalidByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateInvalidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfPeriodIsIncorrect();
    }

    @Test
    @DisplayName("Купить. Истек срок действия карты - прошлые года")
    void ShouldMessageIfExpiredPreviousYearByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateExpiredYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfExpired();
    }

    @Test
    @DisplayName("Купить. Поле 'Месяц' спецсимволами")
    void ShouldMessageIfMonthWithSymbolsByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateSymbol());
        paymentPage.fillYear(DataHelper.generateCurrentYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(1));
    }

    @Test
    @DisplayName("Купить. Поле 'Месяц' кириллицей")
    void ShouldMessageIfMonthWithRuByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateStringInRu(2));
        paymentPage.fillYear(DataHelper.generateCurrentYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(1));
    }

    @Test
    @DisplayName("Купить. Поле 'Месяц' латиницей")
    void ShouldMessageIfMonthWithEnByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateStringInEn(2));
        paymentPage.fillYear(DataHelper.generateCurrentYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(1));
    }

    @Test
    @DisplayName("Купить. Поле 'Месяц' из одной цифры")
    void ShouldMessageIfMonthIsShortByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateNumbers(1));
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
    }

    @Test
    @DisplayName("Купить. Поле 'Месяц' равен 00")
    void ShouldMessageIfMonthIsZeroByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth("00");
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfPeriodIsIncorrect();
    }

    @Test
    @DisplayName("Купить. Истек срок действия карты - текущий год")
    void ShouldMessageIfExpiredCurrentYearByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getValidActiveCard());
        paymentPage.fillMonth(DataHelper.generateExpiredMonth());
        paymentPage.fillYear(DataHelper.generateCurrentYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfExpired();
    }

    @Test
    @DisplayName("Купить. Поле 'Номер карты' cпецсимволами")
    void ShouldMessageIfCardNumberInSymbolsByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.generateSymbol());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(0));
    }

    @Test
    @DisplayName("Купить. Поле 'Номер карты' латиницей")
    void ShouldMessageIfCardNumberInEnByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.generateStringInEn(20));
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(0));
    }

    @Test
    @DisplayName("Купить. Поле 'Номер карты' кириллицей")
    void ShouldMessageIfCardNumberInRuByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.generateStringInRu(20));
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
        assertEquals("", paymentPage.getFieldValue(0));
    }

    @Test
    @DisplayName("Купить. Поле 'Номер карты' слишком короткий")
    void ShouldMessageIfCardNumberIsShortByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        paymentPage.fillCardNumber(DataHelper.getShortCardNumber());
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        paymentPage.messageIfIncorrect();
    }

    @Test
    @DisplayName("Купить. Поле 'Номер карты' слишком длинный")
    void ShouldMessageIfCardNumberIsLongByPayment() {
        OrderPage orderPage = new OrderPage();
        var paymentPage = orderPage.pay();
        String longCardNumber = DataHelper.getLongCardNumber();
        paymentPage.fillCardNumber(longCardNumber);
        paymentPage.fillMonth(DataHelper.generateValidMonth());
        paymentPage.fillYear(DataHelper.generateValidYear());
        paymentPage.fillHolder(DataHelper.generateValidHolderInEn());
        paymentPage.fillCVC(DataHelper.generateValidCVCCode());
        paymentPage.continueClick();
        assertEquals(longCardNumber.substring(0, 19), paymentPage.getFieldValue(0));
    }

    @Test
    @DisplayName("Купить в кредит. Оплата одобренной картой")
    void ShouldPayCreditIfCardIsActive() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.notificationIfSuccess();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Купить в кредит. Оплата отклонённой картой ")
    void ShouldNotPayCreditIfCardIsInactive() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidInactiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.notificationIfFailure();
        assertEquals("DECLINED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'CVC/CVV' не заполнено")
    void ShouldMessageIfCVCIsNullByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.continueClick();
        creditPage.messageIfNull();
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Владелец' не заполнено")
    void ShouldMessageIfHolderIsNullByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfNull();
    }


    @Test
    @DisplayName("Купить в кредит. Поле 'Год' не заполнено")
    void ShouldMessageIfYearIsNullByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfNull();
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Месяц' не заполнено")
    void ShouldMessageIfMonthIsNullByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfNull();
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Номер карты' не заполнено")
    void ShouldMessageIfCardNumberIsNullByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfNull();
    }

    @Test
    @DisplayName("Купить в кредит. Код CVC из двух символов")
    void ShouldCutIfCVCIsShortByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        String cvc = DataHelper.generateNumbers(2);
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(cvc);
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
    }

    @Test
    @DisplayName("Купить в кредит. Код CVC спецсимволами")
    void ShouldMessageIfCVCBySymbolsByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateSymbol());
        creditPage.continueClick();
        creditPage.messageIfNull();
        assertEquals("", creditPage.getFieldValue(4));
    }

    @Test
    @DisplayName("Купить в кредит. Код CVC латиницей")
    void ShouldMessageIfCVCByLettersEnByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateStringInEn(3));
        creditPage.continueClick();
        creditPage.messageIfNull();
        assertEquals("", creditPage.getFieldValue(4));
    }

    @Test
    @DisplayName("Купить в кредит. Код CVC кириллицей")
    void ShouldMessageIfCVCByLettersRuByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateStringInRu(3));
        creditPage.continueClick();
        creditPage.messageIfNull();
        assertEquals("", creditPage.getFieldValue(4));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Владелец' цифрами")
    void ShouldMessageIfHolderInNumbersByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateNumbers(7));
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(3));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Владелец' спецсимволами")
    void ShouldMessageIfHolderInSymbolsByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateSymbol());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(3));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Владелец' кириллицей")
    void ShouldCreditIfHolderInRussian() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInRu());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.notificationIfSuccess();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Год' спецсимволами")
    void ShouldMessageIfYearInSymbolsByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateSymbol() + DataHelper.generateSymbol());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(2));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Год' кириллицей")
    void ShouldMessageIfYearInRuByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateStringInRu(2));
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(2));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Год' латиницей")
    void ShouldMessageIfYearInEnByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateStringInEn(2));
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(2));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Год' из одной цифры")
    void ShouldMessageIfYearIsShortByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateNumbers(1));
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
    }

    @Test
    @DisplayName("Купить в кредит. Некорректный год")
    void ShouldMessageIfYearIsInvalidByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateInvalidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfPeriodIsIncorrect();
    }

    @Test
    @DisplayName("Купить в кредит. Истек срок действия карты - прошлые года")
    void ShouldMessageIfYearIsExpiredByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateExpiredYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfExpired();
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Месяц'спецсимволами")
    void ShouldMessageIfMonthInSymbolsByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateSymbol() + DataHelper.generateSymbol());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(1));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Месяц' кирилицей")
    void ShouldMessageIfMonthInRuByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateStringInRu(2));
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(1));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Месяц' латиницей")
    void ShouldMessageIfMonthInEnByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateStringInEn(2));
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(1));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Месяц' из одной цифры")
    void ShouldMessageIfMonthIsShortByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateNumbers(1));
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Месяц' равен 00")
    void ShouldMessageIfMonthIsZeroByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth("00");
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfPeriodIsIncorrect();
    }

    @Test
    @DisplayName("Купить в кредит. Истек срок действия карты - текущий год")
    void ShouldMessageIfMonthIsExpiredByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getValidActiveCard());
        creditPage.fillMonth(DataHelper.generateExpiredMonth());
        creditPage.fillYear(DataHelper.generateCurrentYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfExpired();
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Номер карты' cпецсимволами")
    void ShouldMessageIfCardNumberInSymbolsByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.generateSymbol());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(0));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Номер карты' латиницей")
    void ShouldMessageIfCardNumberInEnByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.generateStringInEn(20));
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(0));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Номер карты' кириллицей")
    void ShouldMessageIfCardNumberInRuByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.generateStringInRu(20));
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
        assertEquals("", creditPage.getFieldValue(0));
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Номер карты' слишком короткий")
    void ShouldMessageIfCardNumberIsShortByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        creditPage.fillCardNumber(DataHelper.getShortCardNumber());
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        creditPage.messageIfIncorrect();
    }

    @Test
    @DisplayName("Купить в кредит. Поле 'Номер карты' слишком длинный")
    void ShouldMessageIfCardNumberIsLongByCredit() {
        OrderPage orderPage = new OrderPage();
        var creditPage = orderPage.payByCredit();
        String longCardNum = DataHelper.getLongCardNumber();
        creditPage.fillCardNumber(longCardNum);
        creditPage.fillMonth(DataHelper.generateValidMonth());
        creditPage.fillYear(DataHelper.generateValidYear());
        creditPage.fillHolder(DataHelper.generateValidHolderInEn());
        creditPage.fillCVC(DataHelper.generateValidCVCCode());
        creditPage.continueClick();
        assertEquals(longCardNum.substring(0, 19), creditPage.getFieldValue(0));
    }
}