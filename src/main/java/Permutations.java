import java.util.*;

public class Permutations {

    public static void main(String[] args) {
        System.out.println(new Permutations().permuteUnique(new int[] { 1,2,3,4,5,6,7,8,9, 0, 10, 11, 12 }).size());
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> bigList =
                new ArrayList<>();
        Arrays.sort(nums);
        permute(nums, 0, bigList);
        return bigList;
    }

    private void permute(int[] nums, int index, List<List<Integer>> bigList) {
        if (index == nums.length) {
            List l = new ArrayList<Integer>(nums.length);
            for (int num : nums)
                l.add(num);
            bigList.add(l);
            return;
        }
        Set<Integer> dups = new HashSet<>();
        for (int i = index; i < nums.length; i++) {
            if (dups.add(nums[i])) {
                swap(nums, i, index);
                permute(nums, index + 1, bigList);
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