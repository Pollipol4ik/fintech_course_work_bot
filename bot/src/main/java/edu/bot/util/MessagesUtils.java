package edu.bot.util;

import lombok.experimental.UtilityClass;

import static edu.bot.command.Command.ADD_LESSON;
import static edu.bot.command.Command.ADD_STUDENT;
import static edu.bot.command.Command.DELETE_LESSON;
import static edu.bot.command.Command.DELETE_STUDENT;
import static edu.bot.command.Command.LIST;
import static edu.bot.command.Command.LIST_LESSONS;
import static edu.bot.command.Command.MARK_PAYMENT;
import static edu.bot.command.Command.RESCHEDULE_LESSON;
import static edu.bot.command.Command.SET_REMINDER;
import static edu.bot.command.Command.TRACK_GOOGLE_DISC;
import static edu.bot.command.Command.UNTRACK_GOOGLE_DISC;
import static edu.bot.command.Command.VIEW_INCOME_REPORT;
import static edu.bot.command.Command.VIEW_PAYMENTS;
import static edu.bot.command.Command.VIEW_STUDENT_LIST;

@UtilityClass
public class MessagesUtils {

    public static final String NO_TRACKED_LESSON =
            "У вас пока нет занятий ❎. Добавьте занятие с помощью команды /add_lesson.";
    public static final String WELCOME_MESSAGE = """
            <b>Привет! 🖐️</b>
            Это бот для отслеживания изменений на GitHub и Stack Overflow.
            Справка по командам - /help""";
    public static final String TRACKED_LESSON = "Ваши занятия: ";
    public static final String NO_TRACKED_STUDENTS =
            "У вас пока нет учеников ❎. Добавьте учеников с помощью команды /add_student.";
    public static final String TRACKED_STUDENTS = "Ваши ученики: ";
    public static final String HELP_MESSAGE = """
            <b>Доступные команды:</b>
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            %s - %s
            """.formatted(
            ADD_LESSON.getName(), ADD_LESSON.getDescription().toLowerCase(),
            LIST_LESSONS.getName(), LIST_LESSONS.getDescription().toLowerCase(),
            DELETE_LESSON.getName(), DELETE_LESSON.getDescription().toLowerCase(),
            RESCHEDULE_LESSON.getName(), RESCHEDULE_LESSON.getDescription().toLowerCase(),
            ADD_STUDENT.getName(), ADD_STUDENT.getDescription().toLowerCase(),
            VIEW_STUDENT_LIST.getName(), VIEW_STUDENT_LIST.getDescription().toLowerCase(),
            DELETE_STUDENT.getName(), DELETE_STUDENT.getDescription().toLowerCase(),
            SET_REMINDER.getName(), SET_REMINDER.getDescription().toLowerCase(),
            MARK_PAYMENT.getName(), MARK_PAYMENT.getDescription().toLowerCase(),
            VIEW_PAYMENTS.getName(), VIEW_PAYMENTS.getDescription().toLowerCase(),
            VIEW_INCOME_REPORT.getName(), VIEW_INCOME_REPORT.getDescription().toLowerCase(),
            TRACK_GOOGLE_DISC.getName(), TRACK_GOOGLE_DISC.getDescription().toLowerCase(),
            UNTRACK_GOOGLE_DISC.getName(), UNTRACK_GOOGLE_DISC.getDescription().toLowerCase(),
            LIST.getName(), LIST.getDescription().toLowerCase()
    );

    public static final String ERROR_MESSAGE = """
            <b>Ошибка:</b> Данной команды не существует. ❌
            Пожалуйста, воспользуйтесь командой /help для получения списка доступных команд.
            """;

    public static final String LESSON_ADD_EXAMPLE = "Формат команды добавления занятия: /add_lesson <Имя_ученика> <Предмет> <Оплата (да/нет)> <Дата (yyyy-MM-dd HH:mm)> <Длительность в минутах> <Стоимость>";
    public static final String LESSON_ADDED = "Занятие успешно добавлено.";
    public static final String INVALID_DATE_FORMAT = "Неверный формат даты. Используйте формат: yyyy-MM-dd HH:mm.";
    public static final String LESSON_OVERLAP = "Ошибка: У вас уже есть занятие в это время. Пожалуйста, выберите другое время.";
    public static final String INVALID_DURATION_FORMAT = "Неверный формат длительности. Пожалуйста, укажите число минут.";

    public static final String PAYMENT_MARKED = "Оплата за занятие успешно отмечена.";
    public static final String NO_UNPAID_LESSONS = "Нет неоплаченных занятий.";
    public static final String SELECT_LESSON_FOR_PAYMENT = "Выберите занятие для оплаты:";
    public static final String INVALID_PAYMENT_AMOUNT = "Некорректная сумма. Пожалуйста, введите правильное значение.";
    public static final String NO_LESSON_SELECTED_FOR_PAYMENT = "Ошибка: занятие для оплаты не выбрано.";

    public static final String PAYMENT_LIST_EMPTY = "Нет записанных платежей.";
    public static final String PAYMENT_LIST_TITLE = "Список платежей:";
    public static final String INCOME_REPORT_TITLE = "Отчет о доходах:";
    public static final String INCOME_TOTAL = "Общий доход: ";

    public static final String UNKNOWN_COMMAND = "Неизвестная команда. Пожалуйста, проверьте и попробуйте снова.";
    public static final String LESSON_NOT_FOUND = "Занятие не найдено.";
    public static final String STUDENT_NOT_FOUND = "Студент не найден.";

    public static final String LESSON_DELETED = "Занятие успешно удалено.";
    public static final String STUDENT_DELETED = "Студент успешно удален.";
    public static final String LESSON_RESCHEDULED = "Занятие успешно перенесено на новую дату.";

    public static final String AWAITING_NEW_DATE = "Пожалуйста, введите новую дату для переноса занятия в формате yyyy-MM-dd HH:mm.";
    public static final String AWAITING_PAYMENT_AMOUNT = "Введите сумму для оплаты.";

    public static final String LESSON_LIST_EMPTY = "Список занятий пуст.";
    public static final String REMINDER_SET = "Напоминание установлено!";
    public static final String PAYMENT_CONFIRMED = "Подтверждение оплаты отправлено.";
    public static final String PAYMENT_LIST = "Список оплат:";
    public static final String STUDENT_LIST = "Список учеников:";
    public static final String INCOME_REPORT = "Отчет по доходам за выбранный период.";
    public static final String ONLY_TEXT_TO_SEND = "Для отправки доступен только текст!";
    public static final String LESSON_UPDATED = "Занятие успешно обновлено!";
    public static final String FORMAT_ERROR = "Некорректный формат данных";

    public static final String STUDENT_ADDED = "Ученик %s с контактной информацией %s успешно добавлен.";

    public static final String NO_STUDENTS_FOUND = "Список студентов пуст.";
    public static final String CHOOSE_STUDENT_TO_DELETE = "Выберите студента для удаления:";


    public static final String NO_LESSONS_FOUND = "У вас нет отслеживаемых уроков.";
    public static final String CHOOSE_LESSON_TO_DELETE = "Выберите урок, который хотите удалить:";

    public static final String NO_PAYMENTS = "Нет доступных оплат.";
    public static final String NO_INCOME_DATA = "Нет данных для отчета по доходам.";

    public static final String INVALID_FORMAT = "Неверный формат команды. Пожалуйста, проверьте ввод и повторите попытку.";

}
