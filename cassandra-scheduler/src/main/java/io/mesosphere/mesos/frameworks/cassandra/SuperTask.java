package io.mesosphere.mesos.frameworks.cassandra;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.apache.mesos.Protos;
import org.jetbrains.annotations.NotNull;

import static io.mesosphere.mesos.frameworks.cassandra.CassandraTaskProtos.TaskDetails;
import static org.apache.mesos.Protos.ExecutorInfo;
import static org.apache.mesos.Protos.TaskInfo;

public final class SuperTask {
    @NotNull
    private final String hostname;
    @NotNull
    private final TaskInfo taskInfo;
    @NotNull
    private final ExecutorInfo executorInfo;
    @NotNull
    private final TaskDetails taskDetails;

    public SuperTask(
        @NotNull final String hostname,
        @NotNull final TaskInfo taskInfo,
        @NotNull final ExecutorInfo executorInfo,
        @NotNull final TaskDetails taskDetails
    ) {
        this.hostname = hostname;
        this.taskInfo = taskInfo;
        this.executorInfo = executorInfo;
        this.taskDetails = taskDetails;
    }

    @NotNull
    public String getHostname() {
        return hostname;
    }

    @NotNull
    public TaskInfo getTaskInfo() {
        return taskInfo;
    }

    @NotNull
    public ExecutorInfo getExecutorInfo() {
        return executorInfo;
    }

    @NotNull
    public TaskDetails getTaskDetails() {
        return taskDetails;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SuperTask superTask = (SuperTask) o;

        if (!executorInfo.equals(superTask.executorInfo)) return false;
        if (!hostname.equals(superTask.hostname)) return false;
        if (!taskDetails.equals(superTask.taskDetails)) return false;
        if (!taskInfo.equals(superTask.taskInfo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hostname.hashCode();
        result = 31 * result + taskInfo.hashCode();
        result = 31 * result + executorInfo.hashCode();
        result = 31 * result + taskDetails.hashCode();
        return result;
    }

    @NotNull
    public static Predicate<SuperTask> hostnameEq(@NotNull final String hostname) {
        return new HostnameEq(hostname);
    }

    @NotNull
    public static Predicate<SuperTask> taskDetailsTypeEq(@NotNull final TaskDetails.TaskType taskType) {
        return new TaskDetailsTypeEq(taskType);
    }

    @NotNull
    public static Predicate<SuperTask> executorIdEq(@NotNull final Protos.ExecutorID executorID) {
        return new ExecutorIDEq(executorID);
    }

    @NotNull
    public static Predicate<SuperTask> taskIdEq(@NotNull final Protos.TaskID taskID) {
        return new TaskIdEq(taskID);
    }

    @NotNull
    public static Function<SuperTask, Protos.TaskID> toTaskId() {
        return ToTaskId.INSTANCE;
    }

    @NotNull
    public static Function<SuperTask, Protos.ExecutorID> toExecutorId() {
        return ToExecutorId.INSTANCE;
    }

    private static final class HostnameEq implements Predicate<SuperTask> {
        @NotNull
        private final String hostname;

        private HostnameEq(@NotNull final String hostname) {
            this.hostname = hostname;
        }

        @Override
        public boolean apply(final SuperTask superTask) {
            return hostname.equals(superTask.getHostname());
        }
    }

    private static final class TaskDetailsTypeEq implements Predicate<SuperTask> {
        @NotNull
        private final TaskDetails.TaskType taskType;

        private TaskDetailsTypeEq(@NotNull final TaskDetails.TaskType taskType) {
            this.taskType = taskType;
        }

        @Override
        public boolean apply(final SuperTask item) {
            return taskType == item.getTaskDetails().getTaskType();
        }
    }

    private static final class ExecutorIDEq implements Predicate<SuperTask> {
        @NotNull
        private final Protos.ExecutorID executorID;

        private ExecutorIDEq(@NotNull final Protos.ExecutorID executorID) {
            this.executorID = executorID;
        }

        @Override
        public boolean apply(final SuperTask item) {
            return executorID.equals(item.getExecutorInfo().getExecutorId());
        }
    }

    private static final class TaskIdEq implements Predicate<SuperTask> {
        @NotNull
        private final Protos.TaskID taskID;

        private TaskIdEq(@NotNull final Protos.TaskID taskID) {
            this.taskID = taskID;
        }

        @Override
        public boolean apply(final SuperTask item) {
            return taskID.equals(item.getTaskInfo().getTaskId());
        }
    }

    private static final class ToTaskId implements Function<SuperTask, Protos.TaskID> {
        private static final ToTaskId INSTANCE = new ToTaskId();
        @Override
        public Protos.TaskID apply(final SuperTask input) {
            return input.getTaskInfo().getTaskId();
        }
    }

    private static final class ToExecutorId implements Function<SuperTask, Protos.ExecutorID> {
        private static final ToExecutorId INSTANCE = new ToExecutorId();
        @Override
        public Protos.ExecutorID apply(final SuperTask input) {
            return input.executorInfo.getExecutorId();
        }
    }

}