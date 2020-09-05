import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;


public class ModuleArrangingSubject {

    public static void main(String[] args) {
        ListSubjectValidation listSubjectValidationInfo = new ListSubjectValidation();

        ModuleArrangingSubject.ClassPermutationsListener listener = new ModuleArrangingSubject.ClassPermutationsListener(listSubjectValidationInfo);

        ArrayList<ModuleArrangingSubject.Subject> subjects = new ArrayList<>();
        subjects.add(mon("2:Toan:TN:1"));
        subjects.add(mon("2:Toan:TN:1"));
        subjects.add(mon("2:Toan:TN:1"));
        subjects.add(mon("1:Toan:TN:0"));
        subjects.add(mon("2:Ly:TN:1"));
        subjects.add(mon("2:Ly:TN:1"));
        subjects.add(mon("2:Hoa:TN:1"));
        subjects.add(mon("2:Hoa:TN:1"));
        subjects.add(mon("2:Van:XH:1"));
        subjects.add(mon("2:Van:XH:1"));
        subjects.add(mon("1:Van:XH:1"));
        subjects.add(mon("1:Sinh:XH:0"));
        subjects.add(mon("2:Su:XH:0"));
        subjects.add(mon("1:Dia:XH:0"));
        subjects.add(mon("2:TD:XH:0"));
        subjects.add(mon("1:GDCD:XH:0"));
        subjects.add(mon("2:KTCN:TN:0"));
        subjects.add(mon("2:AV:XH:1"));
        subjects.add(mon("2:AV:XH:1"));
        subjects.add(mon("1:AV:XH:1"));

        listSubjectValidationInfo.addOffAspiration("Ly", 14, 21);
        listSubjectValidationInfo.addOffAspiration("Dia", 14, 21);
        listSubjectValidationInfo.addOffAspiration("Su", 14, 21);

        listSubjectValidationInfo.addOffAspiration("Toan", 6, 13);
        listSubjectValidationInfo.addOffAspiration("AV", 6, 13);
        listSubjectValidationInfo.addOffAspiration("KTCN", 6, 13);

        listSubjectValidationInfo.addOffAspiration("Hoa", 22, 29);
        listSubjectValidationInfo.addOffAspiration("TD", 22, 29);
        listSubjectValidationInfo.addOffAspiration("Van", 22, 29);

        listSubjectValidationInfo.addOffAspiration("GDCD", 30, 37);
        listSubjectValidationInfo.addOffAspiration("Sinh", 30, 37);

        PermutationsUse<ModuleArrangingSubject.Subject> permute = new PermutationsUse<ModuleArrangingSubject.Subject>(listener);

        System.out.println(
                permute.permuteUnique(subjects.toArray(new ModuleArrangingSubject.Subject[subjects.size()])).size()
        );
    }

    static ModuleArrangingSubject.Subject mon(String code) {
        return new ModuleArrangingSubject.Subject(code);
    }

    @Data
    static class TeacherAspiration {
        final String subName;
        final int[] offLessionIndex;

        TeacherAspiration(String subName, int[] offLessionIndex) {
            this.subName = subName;
            this.offLessionIndex = offLessionIndex;
        }
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
            if (hasSameSubjectInDay(all, index)) return false;
            if (listSubjectValidationInfo.hopLe(all, index) == false) return false;

            return true;
        }

        private boolean hasSameSubjectInDay(ModuleArrangingSubject.Subject[] subs, int si) {
            if (si > 0 && subs[si - 1].isSameSubName(subs[si])) {
                return true;
            }

            if (si > 1 && subs[si - 2].isSameSubName(subs[si])) {
                return true;
            }

            if (si > 2 && subs[si - 3].isSameSubName(subs[si])) {
                return true;
            }

            if (si > 3 && subs[si - 4].isSameSubName(subs[si])) {
                return true;
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
            cach1Ngay = codes[3].equals("1");
        }

        String tenMonHoc;
        int soTiet;
        boolean laMonTuNhien;
        boolean cach1Ngay;

        boolean isSameSubName(Subject o) {
            return tenMonHoc.equals(o.tenMonHoc);
        }

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
        ArrayList<TeacherAspiration> teacherAspirations = new ArrayList<>();

        public void addOffAspiration(String name, int... offs) {
            teacherAspirations.add(new TeacherAspiration(name, Arrays.stream(offs).toArray()));
        }

        public void addOffAspiration(String name, int from, int to) {
            int[] tempt = new int[to - from + 1];
            for (int i = from; i <= to; i++) {
                tempt[i - from] = i;
            }
            teacherAspirations.add(new TeacherAspiration(name, tempt));
        }

        boolean checkValidAspiration(String sub, int wli) {
            for (TeacherAspiration a : teacherAspirations) {
                if (!a.subName.equals(sub)) continue;
                for (int i : a.offLessionIndex) {
                    if (i == wli) return false;
                }
            }
            return true;
        }

        static int[] parseInts(String text) {
            String[] strings = text.trim().split("\t");
            int[] ints = new int[strings.length];
            for (int i = 0; i < strings.length; i++) {
                ints[i] = Integer.parseInt(strings[i]);
            }
            return ints;
        }

        boolean hopLe(Subject[] subs, int si) {
            int li = startLessionIndexInWeek(subs, si);

            if (checkTietDaVuotQuaBuoi(subs, li, si)) return false;
            if (!checkValidAspiration(subs[si].tenMonHoc, li)) return false;
//            if (!checkMonCach1Ngay(subs, li, si)) return false;


            if (laMonCuoiCungTrongNgay(li, si, subs)) {
                Subject[] monhocHomNays = listTietHocTrongHomNay(subs, li, si);

//                if (demSoTietTuNhien(monhocHomNays) > soMonTuNhienMaxTrongNgay) return false;
//                if (demSoTietXaHoi(monhocHomNays) > soMonXahoiMaxTrongNgay) return false;
                if (soMonHoc(monhocHomNays) > soMonToiDaTrongHomNay(li)) return false;
            }
            if (checkBiTrungMonTrongNgay(subs, li, si)) return false;
            return true;
        }

        private int demSoTietTuNhien(Subject[] subs) {
            int count = 0;
            for (Subject sub : subs) {
                if (sub.laMonTuNhien) {
                    count += sub.soTiet;
                }
            }
            return count;
        }

        private int demSoTietXaHoi(Subject[] subs) {
            int count = 0;
            for (Subject sub : subs) {
                if (!sub.laMonTuNhien) {
                    count += sub.soTiet;
                }
            }
            return count;
        }

        private Integer startLessionIndexInWeek(Subject[] subs, int si) {
            if (si < 0) return null;
            int tiet = 0;
            for (int i = 0; i < si; i++) {
                tiet += subs[i].soTiet;
            }
            return tiet;
        }

        private boolean laMonCuoiCungTrongNgay(int li, int si, Subject[] subs) {
            int soTiet = subs[si].soTiet;
            for (int tiet : tietDauNgay) {
                if ((li + soTiet) == tiet) {
                    return true;
                }
            }
            return false;
        }

        private boolean checkMonCach1Ngay(Subject[] allSubs, int li, int si) {
            Subject mon = allSubs[si];
            if(mon.cach1Ngay == false) return true;

            Subject[] listTietHocHomQua = listTietHocTrongHomQua(allSubs, li, si);
            if (listTietHocHomQua == null) return true;

            for (Subject tietHocHomQua : listTietHocHomQua) {
                if (mon == tietHocHomQua) continue;
                if (tietHocHomQua.toString().equals(mon.toString())) return false;
            }
            return true;
        }

        private boolean checkBiTrungMonTrongNgay(Subject[] allSubs, int li, int si) {
            Subject[] listTietHocHomNay = listTietHocTrongHomNay(allSubs, li, si);
            Set<String> check = new HashSet<>();
            for(Subject mon: listTietHocHomNay) {
                if(check.add(mon.tenMonHoc) == false) {
                    return true;
                }
            }
            return false;
        }

        private String toString(Subject[] subs) {
            String ss = "List:";
            for (int i = 0; i < subs.length; i++) {
                ss += subs[i].toString() + "-";
            }
            return ss;
        }

        private boolean checkTietDaVuotQuaBuoi(Subject[] subs, int li, int si) {
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
            return 4;
//            int n = soTietTrongHomNay(li);
//            return (n <= 6) ? 4 : 5;
        }


        private Integer indexCuaTietDauTienNgayHomNay(int li) {
            for (int i = 0; i < tietDauNgay.length; i++) {
                if (tietDauNgay[i] <= li && tietCuoiNgay[i] >= li)
                    return tietDauNgay[i];
            }
            return null;
        }

        private Integer indexCuaTietDauTienNgayHomQua(int li) {
            for (int i = 1; i < tietDauNgay.length; i++) {
                if (tietDauNgay[i] <= li && tietCuoiNgay[i] >= li)
                    return tietDauNgay[i - 1];
            }
            return null;
        }

        private Integer indexCuaTietCuoiCungNgayHomQua(int li) {
            for (int i = 1; i < tietDauNgay.length; i++) {
                if (tietDauNgay[i] <= li && tietCuoiNgay[i] >= li)
                    return tietCuoiNgay[i - 1];
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

        private Subject[] listTietHocTrongHomQua(Subject[] allSubsInWeek, int li, int si) {
            Integer startIndex = indexCuaTietDauTienNgayHomQua(li);
            Integer lastIndex = startLessionIndexInWeek(allSubsInWeek, si);
            if (startIndex == null || lastIndex == null) return null;
            int runIndex = 0;
            ArrayList<Subject> output = new ArrayList<>();

            for (int ssi = 0; ssi < allSubsInWeek.length; ssi++) {
                Subject mon = allSubsInWeek[ssi];
                if (runIndex >= startIndex && runIndex <= lastIndex) {
                    output.add(mon);
                }
                runIndex += mon.soTiet;
            }
            return output.toArray(new Subject[output.size()]);
        }
    }
}
