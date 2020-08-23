import java.util.*;

public class PermutationsUse {

    public static void main(String[] args) {
        System.out.println(new PermutationsUse().permuteUnique(new int[] { 1,1,2,2,3,3,4,4,5,5,5,6,6,6,7,7,8,8,9}).size());
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> bigList = new ArrayList<>();
        Arrays.sort(nums);
        permute(nums, 0, bigList);
        return bigList;
    }

    private void permute(int[] nums, int index, List<List<Integer>> allResults) {


        if (index == nums.length) {
            List result = new ArrayList<Integer>(nums.length);
            for (int num : nums)
                result.add(num);
            System.out.println(result);
            allResults.add(result);
            return;
        }



        Set<Integer> dups = new HashSet<>();
        for (int i = index; i < nums.length; i++) {
            if (dups.add(nums[i])) {
                swap(nums, i, index);
                if(index < nums.length) {
                    if(index >= 1 && nums[index - 1] == nums[index]) {
                        continue;
                    }
                    if(index >= 2 && nums[index - 2] == nums[index]) {
                        continue;
                    }
                    if(index >= 3 && nums[index - 3] == nums[index]) {
                        continue;
                    }
                    permute(nums, index + 1, allResults);
                }

                swap(nums, i, index);
            }
        }
    }

    private void swap(int[] nums, int i, int index) {
        int temp = nums[i];
        nums[i] = nums[index];
        nums[index] = temp;
    }
}	