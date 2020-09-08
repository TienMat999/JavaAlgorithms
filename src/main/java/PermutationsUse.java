import java.util.*;

public class PermutationsUse<DATA> {
    public PermutationsUse(PermutationsListener<DATA> listener) {
        this.listener = listener;
    }

    private final PermutationsListener<DATA> listener;

    public void permuteUnique(DATA[] nums) {
        Arrays.sort(nums);
        permute(nums, 0);
        listener.end();
    }


    private void permute(DATA[] nums, int index) {
        if (index == nums.length) {
            List result = new ArrayList<DATA>(nums.length);
            for (DATA num : nums) result.add(num);
            listener.onResult(result);
            return;
        }

        Set<DATA> checkDuplicated = new HashSet<>();
        for (int i = index; i < nums.length; i++) {
            boolean isCheckDupPassed = checkDuplicated.add(nums[i]);
            if (!isCheckDupPassed) {
                continue;
            }
            swap(nums, i, index);
            if (listener.acceptableForGoNext(nums, index)) {
                permute(nums, index + 1);
            }
            swap(nums, i, index);
        }

    }

    private void swap(DATA[] nums, int i, int index) {
        DATA temp = nums[i];
        nums[i] = nums[index];
        nums[index] = temp;
    }
}	