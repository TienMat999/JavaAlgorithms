import java.util.*;

public class PermutationsUse<DATA> {
    public PermutationsUse(PermutationsListener<DATA> listener) {
        this.listener = listener;
    }

    private final PermutationsListener<DATA> listener;

    public List<List<DATA>> permuteUnique(DATA[] nums) {
        List<List<DATA>> bigList = new ArrayList<>();
        Arrays.sort(nums);
        permute(nums, 0, bigList);
        return bigList;
    }

    private void permute(DATA[] nums, int index, List<List<DATA>> allResults) {
        if (index == nums.length) {
            List result = new ArrayList<DATA>(nums.length);
            for (DATA num : nums)
                result.add(num);
            System.out.println(result);
//            nums.clone()
            allResults.add(result);
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
                permute(nums, index + 1, allResults);
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