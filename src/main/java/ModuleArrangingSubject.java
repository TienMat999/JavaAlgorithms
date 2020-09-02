import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ModuleArrangingSubject {

    public static void main(String[] args) {
        ListSubjectValidation listSubjectValidationInfo = new ListSubjectValidation();
        ModuleArrangingSubject.ClassPermutationsListener listener = new ModuleArrangingSubject.ClassPermutationsListener(listSubjectValidationInfo);

        ArrayList<ModuleArrangingSubject.Subject> subjects = new ArrayList<>();
        subjects.add(mon("2:Toan:TN"));
        subjects.add(mon("2:Toan:TN"));
        subjects.add(mon("2:Toan:TN"));
        subjects.add(mon("1:Toan:TN"));
        subjects.add(mon("2:Ly:TN"));
        subjects.add(mon("2:Ly:TN"));
        subjects.add(mon("2:Hoa:TN"));
        subjects.add(mon("2:Hoa:TN"));
        subjects.add(mon("2:Van:XH"));
        subjects.add(mon("2:Van:XH"));
        subjects.add(mon("1:Van:XH"));
        subjects.add(mon("1:Sinh:XH"));
        subjects.add(mon("2:Su:XH"));
        subjects.add(mon("1:Dia:XH"));
        subjects.add(mon("2:TD:XH"));
        subjects.add(mon("1:GDCD:XH"));
        subjects.add(mon("2:KTCN:TN"));
        subjects.add(mon("2:AV:XH"));
        subjects.add(mon("2:AV:XH"));
        subjects.add(mon("1:AV:XH"));
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
            if (isBeside3(all, index)) return false;
            if (hasSameSubjectInDay(all, index)) return false;

            if (listSubjectValidationInfo.hopLe(all, index) == false) {
                return false;
            }

            return true;
        }

        private boolean hasSameSubjectInDay(ModuleArrangingSubject.Subject[] subs, int si) {
            if (si > 0 && subs[si - 1].equals(subs[si])) {
                return true;
            }

            if (si > 1 && subs[si - 2].equals(subs[si])) {
                return true;
            }

            if (si > 2 && subs[si - 3].equals(subs[si])) {
                return true;
            }

            if (si > 3 && subs[si - 4].equals(subs[si])) {
                return true;
            }

            return false;
        }

        private boolean isBeside3(ModuleArrangingSubject.Subject[] subs, int si) {
            if (si < subs.length) {
                if (si >= 1 && subs[si - 1] == subs[si]) {
                    return true;
                }
                if (si >= 2 && subs[si - 2] == subs[si]) {
                    return true;
                }
                if (si >= 3 && subs[si - 3] == subs[si]) {
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
            laMonTuNhien = codes[2].equals("TN");
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

    static void log(String text, Object... args) {
        System.out.println(String.format(text, args));
    }

    static class ListSubjectValidation {
        int soTietTrong1Ngay = 8;
        int soMonTuNhienMaxTrongNgay = 5;
        int soMonXahoiMaxTrongNgay = 5;
        int[] tietCuoiSlot = parseInts("1\t3\t5\t7\t9\t11\t13\t15\t17\t19\t21\t23\t25\t27\t29\t31\t33\t35\t37");
        int[] tietDauSlot = parseInts("0\t2\t4\t6\t8\t10\t12\t14\t16\t18\t20\t22\t24\t26\t28\t30\t32\t34\t36");
        int[] tietDauNgay = parseInts("0\t6\t14\t22\t30");
        int[] tietCuoiNgay = parseInts("5\t13\t21\t29\t37");

        static int[] parseInts(String text) {
            String[] strings = text.trim().split("\t");
            int[] ints = new int[strings.length];
            for (int i = 0; i < strings.length; i++) {
                ints[i] = Integer.parseInt(strings[i]);
            }
            return ints;
        }

        boolean hopLe(Subject[] subs, int si) {
            int li = startLessionIndex(subs, si);

            if (tietVuotQuaBuoi(subs, li, si)) {
                return false;
            }

            if (laMonCuoiCungTrongNgay(li, si, subs)) {
                Subject[] monhocHomNays = listTietHocTrongHomNay(subs, li, si);
                if (demSoTietTuNhien(monhocHomNays) > soMonTuNhienMaxTrongNgay) return false;
                if (demSoTietXaHoi(monhocHomNays) > soMonXahoiMaxTrongNgay) return false;
                if (soMonHoc(monhocHomNays) > soMonToiDaTrongHomNay(li)) return false;
            }
            return true;
        }

        int demSoTietTuNhien(Subject[] subs) {
            int count = 0;
            for (Subject sub : subs) {
                if (sub.laMonTuNhien) {
                    count += sub.soTiet;
                }
            }
            return count;
        }

        int demSoTietXaHoi(Subject[] subs) {
            int count = 0;
            for (Subject sub : subs) {
                if (!sub.laMonTuNhien) {
                    count += sub.soTiet;
                }
            }
            return count;
        }


        int startLessionIndex(Subject[] subs, int si) {
            int tiet = 0;
            for (int i = 0; i < si; i++) {
                tiet += subs[i].soTiet;
            }
            return tiet;
        }

        int startSubjectIndex(Subject[] subs, int li) {
            int start = 0;
            for (int si = 0; si < subs.length; si++) {
                int end = start + subs[si].soTiet;
                if (li >= start && li <= end) return si;
                start = end;
            }
            return 0;
        }

        boolean laMonCuoiCungTrongNgay(int li, int si, Subject[] subs) {
            int soTiet = subs[si].soTiet;
            for (int tiet : tietDauNgay) {
                if ((li + soTiet) == tiet) {
                    return true;
                }
            }
            return false;
        }

        boolean tietVuotQuaBuoi(Subject[] subs, int li, int si) {
            int soTiet = subs[si].soTiet;
            for (int day = 0; day < tietCuoiSlot.length; day++) {

                int first = tietDauSlot[day];
                int last = tietCuoiSlot[day];

                if (first > li || last < li) continue;
                if ((li + soTiet - 1) > last) {
                    return true;
                }
            }

            return false;
        }

        private int soTietTrongHomNay(int li) {
            for (int i = 0; i < tietCuoiNgay.length; i++) {
                int first = tietDauNgay[i];
                int last = tietCuoiNgay[i];
                if (first >= li && last <= li) {
                    return last - first + 1;
                }
            }
            return 0;
        }

        private int soMonToiDaTrongHomNay(int li) {
            int n = soTietTrongHomNay(li);
            return (n <= 6) ? 4 : 5;
        }

        private Integer indexCuaTietDauTienNgayHomQua(int li) {
            for (int i = 1; i < tietDauNgay.length; i++) {
                if (li <= tietDauNgay[i]) {
                    return tietDauNgay[i - 1];
                }
            }
            return null;
        }

        private Integer indexCuaTietDauTienNgayHomNay(int li) {
            for (int i = 0; i < tietDauNgay.length; i++) {
                if (tietDauNgay[i] <= li) {
                    return tietDauNgay[i];
                }
            }
            return null;
        }

        private int soMonHoc(Subject[] subs) {
            Set<String> mon = new HashSet<>();
            for (Subject sub : subs) {
                mon.add(sub.tenMonHoc);
            }
            return mon.size();
        }

        private Subject[] listTietHocTrongHomNay(Subject[] allSubsInDay, int li, int si) {
            Integer startIndex = indexCuaTietDauTienNgayHomNay(li);
            if (startIndex == null) return null;
            int runIndex = 0;
            ArrayList<Subject> output = new ArrayList<>();

            for (int ssi = 0; ssi < allSubsInDay.length; ssi++) {
                Subject mon = allSubsInDay[ssi];
                if (runIndex >= startIndex && ssi <= si) {
                    output.add(mon);
                }
                runIndex += mon.soTiet;
            }

            return output.toArray(new Subject[output.size()]);
        }
    }
}
