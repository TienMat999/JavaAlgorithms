import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


public class ModuleArrangingSubject {

    public static void main(String[] args) {
        ListSubjectValidation listSubjectValidationInfo = new ListSubjectValidation();
        ModuleArrangingSubject.ClassPermutationsListener listener = new ModuleArrangingSubject.ClassPermutationsListener(listSubjectValidationInfo);

        ArrayList<ModuleArrangingSubject.Subject> subjects = new ArrayList<>();
        subjects.add(mon("2:Toan:TN"));
        subjects.add(mon("2:Toan:TN"));
        subjects.add(mon("1:Toan:TN"));
        subjects.add(mon("2:Ly:TN"));
        subjects.add(mon("2:Ly:TN"));
        subjects.add(mon("2:Hoa:TN"));
        subjects.add(mon("2:Hoa:TN"));
        subjects.add(mon("2:Van:XH"));
        subjects.add(mon("2:Van:XH"));
        subjects.add(mon("1:Sinh:XH"));
        subjects.add(mon("1:Su:XH"));
        subjects.add(mon("1:Dia:XH"));
        PermutationsUse<ModuleArrangingSubject.Subject> permute = new PermutationsUse<ModuleArrangingSubject.Subject>(listener);

        System.out.println(
                permute.permuteUnique(subjects.toArray(new ModuleArrangingSubject.Subject[subjects.size()])).size()
        );
    }

    static ModuleArrangingSubject.Subject mon(String code) {
        return new ModuleArrangingSubject.Subject(code);
    }

    static class ClassPermutationsListener implements PermutationsListener<Subject> {
        final ListSubjectValidation listSubjectValidationInfo;

        ClassPermutationsListener(ListSubjectValidation listSubjectValidationInfo) {
            this.listSubjectValidationInfo = listSubjectValidationInfo;
        }

        @Override
        public void onResult(List<ModuleArrangingSubject.Subject> array) {
            System.out.println(array);
        }

        @Override
        public boolean acceptableForGoNext(ModuleArrangingSubject.Subject[] all, int index) {

            if(isBeside3(all, index)) {
                return false;
            }

            if(hasSameSubjectInDay(all, index)) {
                return false;
            }

            if(listSubjectValidationInfo.hợp_lệ(all, index) == false) {
                return false;
            }

            return true;
        }

        private boolean hasSameSubjectInDay(ModuleArrangingSubject.Subject[] subs, int si) {
            if(si > 0 && subs[si - 1].equals(subs[si]) ) {
                return true;
            }

            if(si > 1 && subs[si - 2].equals(subs[si])) {
                return true;
            }

            if(si > 2 && subs[si - 3].equals(subs[si])) {
                return true;
            }

            if(si > 3 && subs[si - 4].equals(subs[si])) {
                return true;
            }

            return false;
        }

        private boolean isBeside3(ModuleArrangingSubject.Subject[] subs, int si) {
            if(si < subs.length) {
                if(si >= 1 && subs[si - 1] == subs[si]) {
                    return true;
                }
                if(si >= 2 && subs[si - 2] == subs[si]) {
                    return true;
                }
                if(si >= 3 && subs[si - 3] == subs[si]) {
                    return true;
                }
            }
            return false;
        }
    }

    @Data
    @AllArgsConstructor
    static class Subject implements Comparable<Subject> {
        // Sample:
        // "2:Toan:TN" --> 2 tiet mon Toan lien tiep, Toan la mon TN
        // "1:Toan:TN" --> 1 tiet mon Toan
        // "2:Van:XH" --> 2 tiet mon Van lien tiep, Van la mon XH
        public Subject(String code) {
            String[] codes = code.split(":");
            số_tiết = Integer.parseInt(codes[0]);
            tên_môn_học = codes[1];
            là_môn_tự_nhiên = codes[2] == "TN";
        }
        String tên_môn_học;
        int số_tiết;
        boolean là_môn_tự_nhiên;

        @Override
        public int compareTo(Subject o) {
            return this.toString().compareTo(o.toString());
        }

        @Override
        public String toString() {
            return số_tiết + ":" + tên_môn_học;
        }
    }

    static class ListSubjectValidation {
        int số_tiết_trong_1_ngày = 8;
        int số_môn_tn_max_trong_1_ngày = 6;
        int số_môn_xh_max_trong_1_ngày = 6;
        int[] tiết_cuối = new int[] { 5, 13, 21, 29, 37, 45 };
        int[] tiết_đầu = new int[] { 0, 6, 14, 22, 30, 38 };


        boolean hợp_lệ(Subject[] subs, int si) {
            int li = startLessionIndex(subs, si);

            if(!tiết_không_vượt_quá_ngày(subs,li, si)) {
                return false;
            }
//            Subject[] các_môn_học_trong_2_ngày_gần_đây = list_môn_học_từ_hôm_qua(subs, li);

//            if(là_Môn_cuối_cùng_trong_ngày(li, si, subs)) {
//                Subject[] các_môn_học_trong_1_ngày_gần_đây = list_môn_học_từ_hôm_nay(subs, li);
//                if(đếm_số_tiết_XH(các_môn_học_trong_1_ngày_gần_đây) > số_môn_xh_max_trong_1_ngày) {
//                    return false;
//                }
//                if(đếm_số_tiết_TN(các_môn_học_trong_1_ngày_gần_đây) > số_môn_tn_max_trong_1_ngày) {
//                    return false;
//                }
//            }
            return true;
        }

        int đếm_số_tiết_TN(Subject[] subs) {
            int count = 0;
            for(Subject sub: subs) {
                if(sub.là_môn_tự_nhiên) {
                    count += sub.số_tiết;
                }
            }
            return count;
        }

        int đếm_số_tiết_XH(Subject[] subs) {
            int count = 0;
            for(Subject sub: subs) {
                if(!sub.là_môn_tự_nhiên) {
                    count += sub.số_tiết;
                }
            }
            return count;
        }


        int startLessionIndex(Subject[] subs, int si) {
            int output = 0;
            for(int i = 0; i < subs.length && i < si; i++) {
                output += subs[i].số_tiết;
            }
            return output;
        }

        int startSubjectIndex(Subject[] subs, int li) {
            int start = 0;
            for(int si = 0; si < subs.length; si++) {
                int end = start + subs[si].số_tiết;
                if(li >= start && li <= end) {
                    return si;
                }
                start = end;
            }
            return 0;
        }

        boolean là_Môn_cuối_cùng_trong_ngày(int li, int si, Subject[] subs) {
            int số_tiết = subs[si].số_tiết;
            for(int tiết: tiết_cuối) {
                if((li + số_tiết) == tiết) {
                    return true;
                }
            }
            return false;
        }

        void log(String text, Object... args) {
            System.out.println(String.format(text, args));
        }

        boolean tiết_không_vượt_quá_ngày(Subject[] subs, int li, int si) {
            int số_tiết = subs[si].số_tiết;
            for(int i = 0; i < tiết_cuối.length; i++) {
                int first = tiết_đầu[i];
                int last = tiết_cuối[i];
                if(first > li) { break; }
                log("si = %s, li = %s, TietDau = %s, TietCuoi = %s", si, li, first, last);
                boolean valid = (li >= first) && ((li + số_tiết) <= last);
                if(valid) { return true; }
//                boolean unValid = (li >= first) && (li <= last) && ((li + số_tiết) > last);
//                if(unValid) { return false; }
            }
            return false;
        }

        Integer index_của_tiết_đầu_tiên_ngày_hôm_trước(int li) {
            for(int i = 1; i < tiết_đầu.length; i++) {
                if(li > tiết_đầu[i]) {
                    return tiết_đầu[i - 1];
                }
            }
            return null;
        }

        Integer index_của_tiết_đầu_tiên_ngày_hôm_nay(int li) {
            for(int i = 0; i < tiết_đầu.length; i++) {
                if(li > tiết_đầu[i]) {
                    return tiết_đầu[i];
                }
            }
            return null;
        }

        Subject[] list_môn_học_từ_hôm_qua(Subject[] mons, int li) {
            Integer startIndex = index_của_tiết_đầu_tiên_ngày_hôm_trước(li);
            if(startIndex == null) {
                return null;
            }
            int runIndex = 0;
            ArrayList<Subject> output = new ArrayList<>();
            for(Subject mon: mons) {
                if(runIndex >= startIndex) {
                    output.add(mon);
                }
                runIndex += mon.số_tiết;
            }

            return output.toArray(new Subject[output.size()]);
        }

        Subject[] list_môn_học_từ_hôm_nay(Subject[] mons, int li) {
            Integer startIndex = index_của_tiết_đầu_tiên_ngày_hôm_nay(li);
            if(startIndex == null) {
                return null;
            }
            int runIndex = 0;
            ArrayList<Subject> output = new ArrayList<>();
            for(Subject mon: mons) {
                if(runIndex >= startIndex) {
                    output.add(mon);
                }
                runIndex += mon.số_tiết;
            }

            return output.toArray(new Subject[output.size()]);
        }
    }

}
