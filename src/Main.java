import java.io.*;
import java.util.Objects;


public class Main {
    public static void main(String[] args) throws IOException {

        String[] lessons = new String[]{"Физика", "Литература", "Матматика", "Русский", "Химия", "язык"};
        String[] check_grades = new String[]{"5", "4", "3", "2", "1"};

        String path_files = "C:/Users/user/IdeaProjects/Erenzhenov_Bair_File_Manager/data";
        String path_to_report = "C:/Users/user/IdeaProjects/Erenzhenov_Bair_File_Manager/data/report.txt";

        File folder = new File(path_files);
        File[] files = folder.listFiles();
        if (files == null) {
            throw new AssertionError("Папка пуста!");
        }

        // Для удаления report.txt

        File fileToRemove = new File(path_to_report);
        if (fileToRemove.exists()) {
            if (fileToRemove.delete()) {
                System.out.println("Файл report.txt удален и будет создан снова\n");
                folder = new File(path_files);
                files = folder.listFiles();
            }
        }

        for(int i = 0; i< Objects.requireNonNull(files).length; i++){
            if (files[i].getName().split(" ").length != 3){
                fileToRemove = new File(files[i].getName());
                if (fileToRemove.delete()) {
                    System.out.println("Файл report.txt удален и будет создан снова\n");
                    folder = new File(path_files);
                    files = folder.listFiles();
                }
            }
        }


        int files_length = files.length;
        // Для счётчика всех оценок во всех файлах
        Double[][] all_grades = new Double[files_length][lessons.length];

        // Для всех средних значений
        Float[] all_middle_grades = new Float[files_length];

        for (int i = 0; i < files.length;i ++) {
            if (files[i].isFile()) {
                files_length--;
                Filenames file_reader = new Filenames();
                String name_file = files[i].getName();
                String full_path = path_files + "/" + name_file;
                all_grades = file_reader.getName(full_path, lessons, check_grades, files_length, all_grades);
                all_middle_grades[i] = all_grades[i][5].floatValue();
            }
        }

        files_length = files.length;
        int all_grades_length = all_grades.length;

        // Удаление Средних баллов ученика
        Integer[][] allGrades = new Integer[all_grades_length][all_grades[0].length - 1];
        for (int i = 0; i < all_grades_length; i++) {
            for (int j = 0; j < all_grades[i].length - 1; j++) {
                allGrades[i][j] = all_grades[i][j].intValue();
            }
        }

        // СОЗДАНИЕ ОТЧЁТА
        FileWriter fileWriter = new FileWriter(path_to_report);

        Float[] averageGrades = average_grades(allGrades);

        // max_value и min_value сделаны для того, чтобы находить максимальное и минимальное значение
        float max_value = 0;
        float min_value = 5.00F;
        String best_student = "";
        String baddest_student = "";

        boolean best_students = false;

        for(int i = 0; i < all_middle_grades.length; i ++){
            if (all_middle_grades[i] > max_value){
                max_value = all_middle_grades[i];
                best_student = files[i].getName();
            }

            if (all_middle_grades[i] < min_value){
                min_value = all_middle_grades[i];
                baddest_student = files[i].getName();
            }
        }

        String[] bests_students = new String[files_length];
        Float[] grades_best_students = new Float[files_length];

        String[] bads_students = new String[files_length];
        Float[] grades_bads_students = new Float[files_length];

        for (int i = 0; i < all_middle_grades.length; i ++){
            if (all_middle_grades[i] == max_value){
                best_students = true;
                bests_students[i] = files[i].getName();
                grades_best_students[i] = max_value;
            }
            if (all_middle_grades[i] == min_value){
                best_students = true;
                bads_students[i] = files[i].getName();
                grades_bads_students[i] = min_value;
            }
        }

        String string_to_file;
        for(int i = 0; i < averageGrades.length; i ++){

            string_to_file = String.format(lessons[i] + " - " + "%.2f\n", averageGrades[i]);
            System.out.println(string_to_file);
            fileWriter.write(string_to_file);

        }

        if (best_students){
            System.out.println("\n" +
                    "Лучшие ученики: \n");
            fileWriter.write("\n" +
                    "Лучшие ученики: \n");
            for (int i = 0; i < files_length; i++){
                if (bests_students[i] != null) {
                    string_to_file = String.format("%s (средний балл - %.2f)\n", bests_students[i], grades_best_students[i]);
                    System.out.printf(string_to_file);
                    fileWriter.write(string_to_file);
                }

            }
            System.out.println("\n" +
                    "Худшие ученики: \n");
            fileWriter.write("\n" +
                    "Худшие ученики: \n");
            for (int i = 0; i < files_length; i++){
                if (bads_students[i] != null) {
                    string_to_file = String.format("%s (средний балл - %.2f)\n", bads_students[i], grades_bads_students[i]);
                    System.out.printf(string_to_file);
                    fileWriter.write(string_to_file);
                }
            }

        }else {

            string_to_file = String.format("\n" +
                            "Лучший ученик: \n" +
                            "%s (средний балл - %.2f)\n" +
                            "\n" +
                            "Худший ученик: \n" +
                            "%s (средний балл - %.2f)\n"
                    , best_student, max_value, baddest_student, min_value);
            System.out.println(string_to_file);
            fileWriter.write(string_to_file);
        }
        System.out.println("Количество учеников: " + files_length);
        fileWriter.write("\nКоличество учеников: " + files_length);

        fileWriter.close();

    }

    public static Float[] average_grades(Integer[][] array) {
        // sum_all_grades[i][j]
        // Нам нужно пройти по всему массиву и взять сначала первые элементы всего массива, затем вторые и так далее
        int amount_students = array.length;
        int amount_grades = array[0].length;
        Float[] lst_for_middle_grades = new Float[amount_grades];
        for (int i = 0; i < amount_grades; i++){
            int sum_all_grades = 0;
            for (Integer[] integers : array) {
                sum_all_grades += integers[i];
            }
            float average = (float) sum_all_grades / amount_students;
            lst_for_middle_grades[i] = average;
        }
        return lst_for_middle_grades;
    }
}





