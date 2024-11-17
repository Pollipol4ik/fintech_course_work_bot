package edu.bot.configuration;

import com.pengrad.telegrambot.model.BotCommand;
import edu.bot.command.AddLessonCommand;
import edu.bot.command.AddStudentCommand;
import edu.bot.command.CommandChain;
import edu.bot.command.CommandExecutor;
import edu.bot.command.DeleteLessonCommand;
import edu.bot.command.DeleteStudentCommand;
import edu.bot.command.HelpCommand;
import edu.bot.command.ListDiscCommand;
import edu.bot.command.ListLessonsCommand;
import edu.bot.command.ListStudentsCommand;
import edu.bot.command.MarkPaymentCommand;
import edu.bot.command.RescheduleLessonCommand;
import edu.bot.command.StartCommand;
import edu.bot.command.TrackDiscCommand;
import edu.bot.command.UntrackDiscCommand;
import edu.bot.command.ViewIncomeReportCommand;
import edu.bot.command.ViewPaymentsCommand;
import edu.bot.resolver.UpdateCallbackResolver;
import edu.bot.resolver.UpdateMessageResolver;
import edu.bot.resolver.UpdateResolver;
import edu.bot.service.CommandService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static edu.bot.command.Command.ADD_LESSON;
import static edu.bot.command.Command.ADD_STUDENT;
import static edu.bot.command.Command.DELETE_LESSON;
import static edu.bot.command.Command.DELETE_STUDENT;
import static edu.bot.command.Command.HELP;
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

@Configuration
public class BotConfiguration {

    @Bean
    public CommandService commandService() {
        return new CommandService();
    }

    @Bean
    public UpdateResolver updateResolver() {
        return UpdateResolver.link(
                new UpdateMessageResolver(commandChain()),
                new UpdateCallbackResolver(commandService())
        );
    }

    @Bean
    public CommandChain commandChain() {
        return new CommandChain(
                CommandExecutor.link(
                        new StartCommand(),
                        new HelpCommand(),
                        new AddLessonCommand(commandService()),
                        new DeleteLessonCommand(commandService()),
                        new ListLessonsCommand(commandService()),
                        new AddStudentCommand(commandService()),
                        new DeleteStudentCommand(commandService()),
                        new ListStudentsCommand(commandService()),
                        new RescheduleLessonCommand(commandService()),
                        new MarkPaymentCommand(commandService()),
                        new ViewPaymentsCommand(commandService()),
                        new ViewIncomeReportCommand(commandService()),
                        new TrackDiscCommand(commandService()),
                        new UntrackDiscCommand(commandService()),
                        new ListDiscCommand(commandService())
                ));
    }

    @Bean
    public BotCommand[] commands() {
        return new BotCommand[]{
                new BotCommand(ADD_LESSON.getName(), ADD_LESSON.getDescription()),
                new BotCommand(LIST_LESSONS.getName(), LIST_LESSONS.getDescription()),
                new BotCommand(SET_REMINDER.getName(), SET_REMINDER.getDescription()),
                new BotCommand(DELETE_LESSON.getName(), DELETE_LESSON.getDescription()),
                new BotCommand(RESCHEDULE_LESSON.getName(), RESCHEDULE_LESSON.getDescription()),
                new BotCommand(ADD_STUDENT.getName(), ADD_STUDENT.getDescription()),
                new BotCommand(DELETE_STUDENT.getName(), DELETE_STUDENT.getDescription()),
                new BotCommand(VIEW_STUDENT_LIST.getName(), VIEW_STUDENT_LIST.getDescription()),
                new BotCommand(MARK_PAYMENT.getName(), MARK_PAYMENT.getDescription()),
                new BotCommand(VIEW_PAYMENTS.getName(), VIEW_PAYMENTS.getDescription()),
                new BotCommand(VIEW_INCOME_REPORT.getName(), VIEW_INCOME_REPORT.getDescription()),
                new BotCommand(TRACK_GOOGLE_DISC.getName(), TRACK_GOOGLE_DISC.getDescription()),
                new BotCommand(UNTRACK_GOOGLE_DISC.getName(), UNTRACK_GOOGLE_DISC.getDescription()),
                new BotCommand(LIST.getName(), LIST.getDescription()),
                new BotCommand(HELP.getName(), HELP.getDescription())
        };
    }

    @Bean
    public String telegramToken(ApplicationConfig applicationConfig) {
        return applicationConfig.telegramToken();
    }
}

