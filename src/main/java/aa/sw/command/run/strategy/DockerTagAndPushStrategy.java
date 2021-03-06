package aa.sw.command.run.strategy;

import aa.sw.command.CommandResult;
import aa.sw.command.RunnableEntry;
import aa.sw.command.run.Command;
import aa.sw.command.run.CommandRunnerContext;
import aa.sw.command.run.RunnableEntryExecutionStrategy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DockerTagAndPushStrategy implements RunnableEntryExecutionStrategy {

    private final Command tag;
    private final Command push;

    public static RunnableEntryExecutionStrategy of(final RunnableEntry entry) {
        requireNonNull(entry);

        final String workspace = entry.getWorkspace();
        final Optional<String> workingDirectory = entry.getWorkingDirectory();
        final Optional<Map<String, String>> values = entry.getValues();
        final String source = entry.getParameterAtOrFail(0, "Missing Docker source image name");
        final String remote = entry.getParameterAtOrFail(1, "Missing Docker target image name");
        final List<String> environmentVariables = entry.getEnvironmentVariables().orElse(Collections.emptyList());

        /* TODO: we need to ensure that the tags and the message do not escape, by having a single or double quote */
        final Command tag = Command.parse(List.of(String.format("docker tag '%s' '%s'", source, remote)))
                .withWorkspace(workspace)
                .withWorkingDirectory(workingDirectory)
                .withEnvironmentVariables(environmentVariables)
                .withInterpolatedValues(values);

        /* TODO: we need to ensure that the tags and the message do not escape, by having a single or double quote */
        final Command push = Command.parse(List.of(String.format("docker push '%s'", remote)))
                .withWorkspace(workspace)
                .withWorkingDirectory(workingDirectory)
                .withEnvironmentVariables(environmentVariables)
                .withInterpolatedValues(values);

        return new DockerTagAndPushStrategy(tag, push);
    }

    @Override
    public CommandResult execute(final CommandRunnerContext context) {
        return tag(context)
                .then(() -> push(context));
    }

    private CommandResult tag(final CommandRunnerContext context) {
        return CommandStrategy.builder(tag)
                .execute(context);
    }

    private CommandResult push(final CommandRunnerContext context) {
        return CommandStrategy.builder(push)
                .execute(context);
    }
}
