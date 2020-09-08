import java.util.List;

public interface PermutationsListener<DATA> {
    void onResult(List<DATA> array);
    boolean acceptableForGoNext(DATA[] all, int index);
    void end();
}