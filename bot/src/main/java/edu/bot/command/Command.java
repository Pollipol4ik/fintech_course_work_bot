package edu.bot.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Command {
    START("/start", "Запустить бота ▶️"),
    HELP("/help", "Справка по командам ℹ️"),
    ADD_LESSON("/add_lesson", "Добавить занятие 📅"),
    LIST_LESSONS("/list_lessons", "Просмотреть список занятий 📋"),
    DELETE_LESSON("/delete_lesson", "Удалить занятие 🗑️"),
    RESCHEDULE_LESSON("/reschedule_lesson", "Перенести занятие 🔄"),
    ADD_STUDENT("/add_student", "Добавить ученика 👥"),
    VIEW_STUDENT_LIST("/student_list", "Список учеников 👥"),
    DELETE_STUDENT("/delete_student", "Удалить ученика 👥"),
    SET_REMINDER("/set_reminder", "Установить напоминание ⏰"),
    MARK_PAYMENT("/mark_payment", "Отметить оплату 💵"),
    VIEW_PAYMENTS("/view_payments", "Просмотреть оплаты 💰"),
    VIEW_INCOME_REPORT("/income_report", "Отчет по доходам 📊"),
    TRACK_GOOGLE_DISC("/track_disc", "Начать отслеживание обновлений на диске 🔔"),
    UNTRACK_GOOGLE_DISC("/untrack_disc", "Прекратить отслеживание обновлений на диске ⛔"),
    LIST("/list_google_disc", "Просмотреть список отслеживаемых дисков/папок 📋");


    private final String name;
    private final String description;
}
