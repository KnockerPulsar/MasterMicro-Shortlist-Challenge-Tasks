import java.util.Objects;

public class Result {

    boolean success;
    Topology returned;
    String writePath;

    public Result(boolean success, Topology returned, String writePath)
    {
        this.success = success;
        this.returned = returned;
        this.writePath = writePath;
    }

    public Result(boolean success, Topology returned) {
        this.success = success;
        this.returned = returned;
    }

    public Result(boolean success, String writePath )
    {
        this.success = success;
        this.writePath = writePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return success == result.success && Objects.equals(returned, result.returned) && Objects.equals(writePath, result.writePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, returned, writePath);
    }
}
