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
}
