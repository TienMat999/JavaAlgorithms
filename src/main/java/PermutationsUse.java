import java.util.*;

public class PermutationsUse<DATA> {


    public PermutationsUse(PermutationsListener<DATA> listener) {
        this.listener = listener;
    }

    public static void main(String[] args) {
        PermutationsListener<Integer> listener = new PermutationsListener<Integer>() {
            @Override
            public void onResult(List<Integer> array) {
                System.out.println(array);
            }

            @Override
            public boolean acceptableForGoNext(Integer[] all, int index) {
                if(isBeside3(all, index)) {
                    return false;
                }

                if(hasSameSubjectInDay(all, index)) {
                    return false;
                }
                return true;
            }

            private boolean hasSameSubjectInDay(Integer[] all, int index) {
                if(index % 4 != 0 || index == 0) {
                    return false;
                }

                if(all[index - 1] == all[index]) {
                    return true;
                }
                if(all[index - 2] == all[index]) {
                    return true;
                }
                if(all[index - 3] == all[index]) {
                    return true;
                }

                if(all[index - 4] == all[index]) {
                    return true;
                }

                return false;
            }

            private boolean isBeside3(Integer[] all, int index) {
                if(index < all.length) {
                    if(index >= 1 && all[index - 1] == all[index]) {
                        return true;
                    }
                    if(index >= 2 && all[index - 2] == all[index]) {
                        return true;
                    }
                    if(index >= 3 && all[index - 3] == all[index]) {
                        return true;
                    }
                }
                return false;
            }
        };
        PermutationsUse<Integer> permute = new PermutationsUse<Integer>(listener);
        System.out.println(permute.permuteUnique(new Integer[] { 1,1,2,2,3,3,4,4,5,5}).size());
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
            List result = new ArrayList<Integer>(nums.length);
            for (DATA num : nums)
                result.add(num);
            System.out.println(result);
            allResults.add(result);
            return;
        }

        Set<DATA> dups = new HashSet<>();
        for (int i = index; i < nums.length; i++) {
            if (dups.add(nums[i])) {
                swap(nums, i, index);
                if(listener.acceptableForGoNext(nums, index)) {
                    permute(nums, index + 1, allResults);
                }
                swap(nums, i, index);
            }
        }
    }

    private void swap(DATA[] nums, int i, int index) {
        DATA temp = nums[i];
        nums[i] = nums[index];
        nums[index] = temp;
    }
}	