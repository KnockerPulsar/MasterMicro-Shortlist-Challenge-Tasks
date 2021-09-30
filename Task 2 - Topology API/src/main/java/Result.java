import java.util.Objects;

// Holds the result to read, write, and loaded topology manipulation operations.
public class Result {

    boolean success;
    Topology returned;
    String writePath;

    // Shortcut
    public static Result Fail = new Result(false, null, null);

    public Result(boolean success, Topology returned, String writePath)
    {
        this.success = success;
        this.returned = returned;
        this.writePath = writePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return success == result.success && Objects.equals(returned, result.returned) && Objects.equals(writePath, result.writePath);
    }
}
