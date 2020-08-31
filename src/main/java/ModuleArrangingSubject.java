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

            if(listSubjectValidationInfo.hopLe(all, index) == false) {
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
            soTiet = Integer.parseInt(codes[0]);
            tenMonHoc = codes[1];
            laMonTuNhien = codes[2] == "TN";
        }
        String tenMonHoc;
        int soTiet;
        boolean laMonTuNhien;

        @Override
        public int compareTo(Subject o) {
            return this.toString().compareTo(o.toString());
        }

        @Override
        public String toString() {
            return soTiet + ":" + tenMonHoc;
        }
    }

    static class ListSubjectValidation {
        int soTietTrong1Ngay = 8;
        int soMonTuNhienMaxTrongNgay = 6;
        int soMonXahoiMaxTrongNgay = 6;
        int[] tietCuoi = new int[] { 5, 13, 21, 29, 37, 45 };
        int[] tietDau = new int[] { 0, 6, 14, 22, 30, 38 };


        boolean hopLe(Subject[] subs, int si) {
            int li = startLessionIndex(subs, si);

            if(!tietKhongVuotQuaNgay(subs,li, si)) {
                return false;
            }
//            Subject[] các_môn_trong_2_ngày_gần_đây = list_môn_từ_hôm_qua(subs, li);

//            if(là_Môn_cuối_cùng_trong_ngày(li, si, subs)) {
//                Subject[] các_môn_trong_1_ngày_gần_đây = list_môn_từ_hôm_nay(subs, li);
//                if(đếm_số_tiết_XH(các_môn_trong_1_ngày_gần_đây) > số_môn_xh_max_trong_1_ngày) {
//                    return false;
//                }
//                if(đếm_số_tiết_TN(các_môn_trong_1_ngày_gần_đây) > số_môn_tn_max_trong_1_ngày) {
//                    return false;
//                }
//            }
            return true;
        }

        int demSoTietTuNhien(Subject[] subs) {
            int count = 0;
            for(Subject sub: subs) {
                if(sub.laMonTuNhien) {
                    count += sub.soTiet;
                }
            }
            return count;
        }

        int demSoTietXaHoi(Subject[] subs) {
            int count = 0;
            for(Subject sub: subs) {
                if(!sub.laMonTuNhien) {
                    count += sub.soTiet;
                }
            }
            return count;
        }


        int startLessionIndex(Subject[] subs, int si) {
            int output = 0;
            for(int i = 0; i < subs.length && i < si; i++) {
                output += subs[i].soTiet;
            }
            return output;
        }

        int startSubjectIndex(Subject[] subs, int li) {
            int start = 0;
            for(int si = 0; si < subs.length; si++) {
                int end = start + subs[si].soTiet;
                if(li >= start && li <= end) {
                    return si;
                }
                start = end;
            }
            return 0;
        }

        boolean laMonCuoiCungTrongNgay(int li, int si, Subject[] subs) {
            int soTiet = subs[si].soTiet;
            for(int tiet: tietDau) {
                if((li + soTiet) == tiet) {
                    return true;
                }
            }
            return false;
        }

        void log(String text, Object... args) {
            System.out.println(String.format(text, args));
        }

        boolean tietKhongVuotQuaNgay(Subject[] subs, int li, int si) {
            int soTiet = subs[si].soTiet;
            for(int i = 0; i < tietDau.length; i++) {
                int first = tietDau[i];
                int last = tietCuoi[i];

                if(first > li) { break; }
                boolean valid = (li >= first) && ((li + soTiet) <= last);
                if(valid) {
                    log("si = %s, li = %s, soTiet = %s, TietDau = %s, TietCuoi = %s", si, li, soTiet, first, last);
                    return true;
                }
//                boolean unValid = (li >= first) && (li <= last) && ((li + số_tiết) > last);
//                if(unValid) { return false; }
            }
            return false;
        }

        Integer indexCuaTietDauTienNgayHomQua(int li) {
            for(int i = 1; i < tietDau.length; i++) {
                if(li > tietDau[i]) {
                    return tietDau[i - 1];
                }
            }
            return null;
        }

        Integer indexCuaTietDauTienNgayHomNay(int li) {
            for(int i = 0; i < tietDau.length; i++) {
                if(li > tietDau[i]) {
                    return tietDau[i];
                }
            }
            return null;
        }

        Subject[] listMonHocTuHomQUa(Subject[] mons, int li) {
            Integer startIndex = indexCuaTietDauTienNgayHomQua(li);
            if(startIndex == null) {
                return null;
            }
            int runIndex = 0;
            ArrayList<Subject> output = new ArrayList<>();
            for(Subject mon: mons) {
                if(runIndex >= startIndex) {
                    output.add(mon);
                }
                runIndex += mon.soTiet;
            }

            return output.toArray(new Subject[output.size()]);
        }

        Subject[] listMonHocTrongHomNay(Subject[] mons, int li) {
            Integer startIndex = indexCuaTietDauTienNgayHomNay(li);
            if(startIndex == null) {
                return null;
            }
            int runIndex = 0;
            ArrayList<Subject> output = new ArrayList<>();
            for(Subject mon: mons) {
                if(runIndex >= startIndex) {
                    output.add(mon);
                }
                runIndex += mon.soTiet;
            }

            return output.toArray(new Subject[output.size()]);
        }
    }

}
