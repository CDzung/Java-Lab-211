
import java.util.ArrayList;
import java.util.Collections;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mido
 */
public class Manager {

    private static int count;

    public static int menu() {
        System.out.println("        1. Create");
        System.out.println("        2. Find and Sort");
        System.out.println("        3. Update/Delete");
        System.out.println("        4. Report");
        System.out.println("        5. Exit");
        int choice = Validation.getInt("Enter your choice(1->5): ", 1, 5);
        return choice;
    }

    //Create new student infomation
    public static void create(ArrayList<Student> list, ArrayList<Report> rList) {
        if (count > 5) {
            String s = Validation.getString("Do you want to continue(Y/N)? ", "Input must be Y/y or N/n", "^[ynYN]$");
            if (s.equalsIgnoreCase("n")) {
                return;
            }
        }

        System.out.println("Enter student infomation to create");
        while (true) {
            String id = Validation.getString("Enter id: ", "Format ID is wrong", "^[Hh][EeAaSs]\\d{6}$");
            String studentName;
            int age;
            Student s = getStudent(list, id);
            if (s != null) {
                studentName = s.getStudentName();
                age = s.getAge();
                System.out.println("Id is exist. Student name is " + studentName + ". Age is " + age);
            } else {
                studentName = Validation.getString("Enter student name: ", "Name contains letter and space", "^[a-zA-Z ]+$");
                age = Validation.getInt("Enter student age: ", 1, Integer.MAX_VALUE);
            }
            String semester = Validation.getString("Enter semester: ", "Semester contains letter and number", "^[a-zA-Z]*[0-9]+$");
            String courseName = Validation.getString("Enter course name: ", "There are only 3 courses: Java, .Net, C/C++", "course");
            if (checkInfoExist(list, id, semester, courseName)) {
                System.err.println("Student infomation is exist. Please re-input infomation");
                continue;
            }
            if (s == null) {
                count++;
            }
            System.out.println("Create successfull.");
            list.add(new Student(id, studentName, age, semester, courseName));
            addReport(rList, id, studentName, courseName);
            break;
        }

    }

    //Find student by name and sort by age
    public static void findAndSort(ArrayList<Student> list) {
        String name = Validation.getString("Enter student name to search: ", "Name contains letter and space", "^[a-zA-Z ]+$").toLowerCase().trim();

        ArrayList<Student> newList = new ArrayList<>();

        for (Student s : list) {
            if (s.getStudentName().toLowerCase().contains(name)) {
                newList.add(s);
            }
        }

        if (newList.isEmpty()) {
            System.out.println("Not Found!!");
        } else {
            Collections.sort(newList, (Student o1, Student o2) -> {
                return o1.getAge() - o2.getAge();
            });
            System.out.printf("|%-20s|%-20s|%-20s|%-20s|%-20s\n", "ID", "Student Name", "Semester", "Age", "Course Name");
            for (Student s : newList) {
                System.out.printf("|%-20s|%-20s|%-20s|%-20s|%-20s\n", s.getId(), s.getStudentName(), s.getSemester(), s.getAge(), s.getCourseName());
            }
        }

    }

    //Update or delete student by ID
    public static void updateAndDelete(ArrayList<Student> list, ArrayList<Report> rList) {
        if(list.isEmpty()) {
            System.err.println("The database has no student information.");
            return;
        }
        String id;
        while (true) {
            id = Validation.getString("Enter ID to update/delete: ", "Format ID is wrong", "^[Hh][EeAaSs]\\d{6}$");
            if (getStudent(list, id) == null) {
                System.err.println("ID is not existed");
            } else {
                break;
            }
        }
        String choice = Validation.getString("Do you want to update(U) or delete(D) student: ", "Input must be U/u or D/d", "^[udUD]$");

        //show infomation to choose
        System.out.printf("|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s\n", "Index", "ID", "Student Name", "Semester", "Age", "Course Name");
        int cnt = 0;
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Student s = list.get(i);
            if (s.getId().equalsIgnoreCase(id)) {
                ++cnt;
                System.out.printf("|%-20s|%-20s|%-20s|%-20s|%-20s|%-20s\n", cnt, s.getId(), s.getStudentName(), s.getSemester(), s.getAge(), s.getCourseName());
                index.add(i);
            }
        }

        //delete infomation
        if (choice.equalsIgnoreCase("d")) {
            int idx = Validation.getInt("Enter index you want to delete: ", 1, cnt);
            Student st = list.get(index.get(idx - 1));
            removeReport(rList, id, st.getStudentName(), st.getCourseName());
            list.remove(st);
            if (cnt == 1) {
                count--;
            }
            System.out.println("Remove Successfull");
        } //update infomation
        else {
            int idx = Validation.getInt("Enter index you want to update: ", 1, cnt);
            Student st = list.get(index.get(idx - 1));

            // enter new infomation
            String studentName = Validation.getString("Enter new student name: ", "Name contains letter and space", "^[a-zA-Z ]+$");
            int age = Validation.getInt("Enter new age: ", 1, Integer.MAX_VALUE);
            String semester = Validation.getString("Enter new semester: ", "Semester contains letter and number", "^[a-zA-Z]*[0-9]+$");
            String courseName = Validation.getString("Enter new course name: ", "There are only 3 courses: Java, .Net, C/C++", "course");
            if(checkInfoExist(list, id, semester, courseName)) {
                System.err.println("Update failed. Student infomation is exist");
                return;
            }
            
            if (!studentName.equalsIgnoreCase(st.getStudentName())) {
                for (Report r : rList) {
                    if (r.getStudentID().equalsIgnoreCase(id)) {
                        r.setStudentName(studentName);
                    }
                }
                for (Student s : list) {
                    if (s.getId().equalsIgnoreCase(id)) {
                        s.setStudentName(studentName);
                    }
                }
            }
            if (!courseName.equalsIgnoreCase(st.getCourseName())) {
                removeReport(rList, id, studentName, st.getCourseName());
                addReport(rList, id, studentName, courseName);
            }
            if (age != st.getAge()) {
                for (Student s : list) {
                    if (s.getId().equalsIgnoreCase(id)) {
                        s.setAge(age);
                    }
                }
            }
            st.setSemester(semester);
            st.setCourseName(courseName);
            System.out.println("Update Sucessfull");
        }
    }

    public static void report(ArrayList<Report> rList) {
        if(rList.isEmpty()) {
            System.err.println("The database has no student information.");
            return;
        }
        System.out.printf("|%-20s|%-20s|%-20s|%-20s\n", "ID", "NAME", "COURSE NAME", "TOTAL COURSE");
        for (Report r : rList) {
            System.out.printf("|%-20s|%-20s|%-20s|%-20s\n", r.getStudentID(), r.getStudentName(), r.getCourseName(), r.getTotalCourse());
        }
    }

    public static Student getStudent(ArrayList<Student> list, String id) {
        for (Student s : list) {
            if (id.equalsIgnoreCase(s.getId())) {
                return s;
            }
        }
        return null;
    }

    public static boolean checkStudentName(ArrayList<Student> list, String id, String name) {
        for (Student s : list) {
            if (id.equalsIgnoreCase(s.getId()) && !name.equalsIgnoreCase(s.getStudentName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkInfoExist(ArrayList<Student> list, String id, String semester, String courseName) {
        for (Student st : list) {
            if (id.equalsIgnoreCase(st.getId())
                    && semester.equalsIgnoreCase(st.getSemester())
                    && courseName.equalsIgnoreCase(st.getCourseName())) {
                return true;
            }
        }
        return false;
    }

    public static Report getReport(ArrayList<Report> rList, String id, String courseName) {
        for (Report r : rList) {
            if (r.getStudentID().equalsIgnoreCase(id) && r.getCourseName().equalsIgnoreCase(courseName)) {
                return r;
            }
        }
        return null;
    }

    public static void addReport(ArrayList<Report> rList, String id, String studentName, String courseName) {
        Report r = getReport(rList, id, courseName);
        if (r != null) {
            r.setTotalCourse(r.getTotalCourse() + 1);
        } else {
            rList.add(new Report(id, studentName, courseName, 1));
        }
    }

    public static void removeReport(ArrayList<Report> rList, String id, String studentName, String courseName) {
        Report r = getReport(rList, id, courseName);
        if(r == null)
            return;
        r.setTotalCourse(r.getTotalCourse() - 1);
        if (r.getTotalCourse() == 0) {
            rList.remove(r);
        }
    }
}
