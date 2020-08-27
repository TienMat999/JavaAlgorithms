import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Dictionary;


public class SubjectArrange {
    @Data
    @AllArgsConstructor
    class Subject {
        String tên_môn_học;
        int số_tiết;
        int số_tiết_trong_2_ngày_liên_tiếp;
        boolean là_môn_tự_nhiên;
    }

    static class Config {
        static int số_tiết_trong_1_ngày = 8;
        static int số_môn_tn_max_trong_1_ngày = 6;
        static int số_môn_xh_max_trong_1_ngày = 6;
        static int[] tiết_cuối = new int[] { 5, 13, 21, 29, 37, 45 };
        static int[] tiết_đầu = new int[] { 0, 6, 14, 22, 30, 38 };

        static boolean là_Môn_cuối_cùng_trong_ngày(int index, int số_tiết) {
            for(int tiết: tiết_cuối) {
                if((index + số_tiết) == tiết) {
                    return true;
                }
            }
            return false;
        }

        static Integer index_của_tiết_đầu_tiên_ngày_hôm_trước(int index) {
            for(int i = 1; i < tiết_đầu.length; i++) {
                if(index > tiết_đầu[i]) {
                    return tiết_đầu[i - 1];
                }
            }
            return null;
        }
    }

}
