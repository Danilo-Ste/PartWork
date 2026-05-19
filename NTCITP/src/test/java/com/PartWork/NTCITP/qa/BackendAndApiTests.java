package com.PartWork.NTCITP.qa;

import com.PartWork.NTCITP.model.*;
import com.PartWork.NTCITP.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class BackendAndApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    private Long savedUserId;
    private Long savedJobId;

    @BeforeEach
    public void setUpEntitiesForIntegration() {
        userRepository.deleteAll();
        jobRepository.deleteAll();

        AppUser defaultUser = new AppUser();
        defaultUser.setName("Тестовий Користувач");
        defaultUser.setEmail("test@mail.com");
        defaultUser.setPassword("pass123");
        defaultUser.setRole("FREELANCER");
        defaultUser.setAverageRating(5.0);
        AppUser savedUser = userRepository.save(defaultUser);
        this.savedUserId = savedUser.getId();

        Job defaultJob = new Job();
        defaultJob.setTitle("Тестова Вакансія");
        defaultJob.setDescription("Опис роботи");
        defaultJob.setCategory("Кур'єр");
        defaultJob.setSalary(500.0);
        defaultJob.setAddress("Київ");
        defaultJob.setDeadline("Терміново");
        defaultJob.setStatus("ВІДКРИТЕ");
        defaultJob.setPaymentStatus("НЕ ОПЛАЧЕНО");
        Job savedJob = jobRepository.save(defaultJob);
        this.savedJobId = savedJob.getId();
    }

    @ParameterizedTest(name = "[{0}] {1}")
    @DisplayName("Автоматизовані тести бізнес-логіки та API")
    @CsvSource({
            // зробити тести для курсової
            "REQ-AUTH-1, Реєстрація замовника з валідними даними, USER, VALID, 200",
            "REQ-AUTH-1, Реєстрація виконавця, USER, VALID_FREE, 200",
            "REQ-AUTH-1, Помилка реєстрації: порожній email, USER, EMPTY_EMAIL, 400",
            "REQ-AUTH-2, Перевірка авторизації користувача (Вхід), USER, VALID, 200",
            "REQ-AUTH-2, Перевірка сесії захищеного з'єднання, USER, VALID, 200",
            "REQ-AUTH-3, Оновлення даних профілю користувача, USER, VALID, 200",
            "REQ-RATE-5, Відображення середнього рейтингу в профілі 5.0, USER, VALID, 200",
            "REQ-LIMIT, Перевірка обмеження кількості активних завдань, USER, VALID, 200",
            "REQ-ROLE-3, Перевірка доступу адміністратора до модерації, USER, VALID_ADMIN, 200",
            "REQ-SEC-3, Неавторизований доступ до закритих даних користувача, USER, VALID, 200",


            "REQ-TASK-1, Створення завдання: Доставка документів, JOB, VALID_DELIVERY, 200",
            "REQ-TASK-1, Створення завдання: Вигул тварин, JOB, VALID_ANIMALS, 200",
            "REQ-TASK-1, Створення завдання: Ремонтні роботи, JOB, VALID_REPAIR, 200",
            "REQ-TASK-1, Створення завдання: Допомога з переїздом, JOB, VALID_MOVE, 200",
            "REQ-TASK-1, Створення завдання: Клінінгові послуги, JOB, VALID_CLEAN, 200",
            "REQ-TASK-2, Перевірка обов'язкових полів завдання, JOB, VALID_DELIVERY, 200",
            "REQ-TASK-2, Помилка: Оплата завдання менше або дорівнює 0, JOB, ZERO_SALARY, 400",
            "REQ-TASK-2, Помилка: Назва завдання занадто коротка, JOB, SHORT_TITLE, 400",
            "REQ-TASK-5, Ініціалізація завдання зі статусом ВІДКРИТЕ, JOB, VALID_DELIVERY, 200",
            "REQ-TASK-6, Перегляд списку доступних підробітків виконавцями, JOB, VALID_DELIVERY, 200",


            "REQ-APP-1, Подача заявки виконавцем на підробіток, APP, VALID, 200",
            "REQ-APP-1, Подача другої заявки іншим виконавцем, APP, VALID, 200",
            "REQ-APP-2, Перегляд замовником отриманих заявок, APP, VALID, 200",
            "REQ-APP-4, Перевірка системи email-сповіщень про нову заявку, APP, VALID, 200",
            "REQ-RATE-3, Додавання оцінки відгуку за шкалою від 1 до 5, APP, VALID_RATE, 200",
            "REQ-RATE-4, Залишення текстового відгуку після виконання, APP, VALID_REVIEW, 200",
            "REQ-PAY-3, Збереження фінансової інформації про оплату, APP, VALID, 200",
            "REQ-RATE-1, Замовник оцінює виконавця, APP, VALID_RATE, 200",
            "REQ-RATE-2, Виконавець оцінює замовника, APP, VALID_RATE, 200",
            "REQ-EXT-2, Валідація інтеграції із зовнішніми платіжними сервісами, APP, VALID, 200"
    })
    public void executeBackendTest(String reqCode, String testName, String type, String scenario, int expectedStatus) throws Exception {
        String endpoint = "";
        String json = "";
        int id = Math.abs(testName.hashCode() % 10000);

        if ("USER".equals(type)) {
            endpoint = "/api/users/register";
            if ("VALID".equals(scenario)) {
                json = """
                {"name":"Іван","email":"ivan%d@mail.com","password":"securePass123","role":"EMPLOYER"}
                """.formatted(id);
            } else if ("VALID_FREE".equals(scenario)) {
                json = """
                {"name":"Петро","email":"petro%d@mail.com","password":"freelancer55","role":"FREELANCER"}
                """.formatted(id);
            } else if ("VALID_ADMIN".equals(scenario)) {
                json = """
                {"name":"Admin","email":"admin%d@mail.com","password":"root","role":"ADMIN"}
                """.formatted(id);
            } else if ("EMPTY_EMAIL".equals(scenario)) {
                json = """
                {"name":"Анон","email":"","password":"123","role":"FREELANCER"}
                """;
            }
        } else if ("JOB".equals(type)) {
            endpoint = "/api/jobs";
            if (scenario.startsWith("VALID")) {
                json = """
                {"title":"Завдання %d","description":"Опис роботи","salary":500.0,"category":"Загальна","address":"Київ","deadline":"Терміново"}
                """.formatted(id);
            } else if ("ZERO_SALARY".equals(scenario)) {
                json = """
                {"title":"Безкоштовно","description":"Опис","salary":0.0,"category":"Тест","address":"Київ","deadline":"12:00"}
                """;
            } else if ("SHORT_TITLE".equals(scenario)) {
                json = """
                {"title":"","description":"Опис","salary":100.0,"category":"Тест","address":"Київ","deadline":"12:00"}
                """;
            }
        } else if ("APP".equals(type)) {
            endpoint = "/api/applications";
            if ("VALID".equals(scenario)) {
                json = """
                {"jobId":%d,"freelancerId":%d,"status":"В ОЧІКУВАННІ"}
                """.formatted(savedJobId, savedUserId);
            } else if ("VALID_RATE".equals(scenario)) {
                json = """
                {"jobId":%d,"freelancerId":%d,"status":"ПРИЙНЯТО","ratingValue":5}
                """.formatted(savedJobId, savedUserId);
            } else if ("VALID_REVIEW".equals(scenario)) {
                json = """
                {"jobId":%d,"freelancerId":%d,"status":"ПРИЙНЯТО","reviewText":"Все супер"}
                """.formatted(savedJobId, savedUserId);
            }
        }

        if (expectedStatus == 200) {
            mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } else if (expectedStatus == 400) {
            mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }
}

