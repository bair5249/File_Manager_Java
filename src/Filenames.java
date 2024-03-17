import java.io.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Filenames {


    List<Double> grades = new ArrayList<>();

    // В file_path хранится полный путь до файла, в lessons названия предметов для проверки, в check_grades оценки для проверки
    public Double[][] getName(String full_path, String[] lessons, String[] check_grades, Integer files_length, Double[][] all_grades) throws IOException {

        String line;
        double middle;

        FileReader fileReader = new FileReader(full_path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int line_counter = 0;
        while ((line = bufferedReader.readLine()) != null) {
            line_counter += 1;
            String[] list_line = line.split(" ");

            // Проверка на правильность строки
            checking_line(line_counter, list_line, lessons, check_grades);

            // Добавление оценки в массив для среднего значения
            adding_grade(list_line);

            // Добавление оценок в общий массив
            all_grades[files_length][line_counter - 1] = (double) Integer.parseInt(list_line[list_line.length - 1]);
        }
        middle = count_middle(grades);

        // Добавление лучших и худших учеников в 6 и 7 столбец для того, чтобы выводить их после средних баллов по предметам
        for(int i = 0;i < all_grades.length; i++){
            all_grades[i][5] = middle;
        }

        grades.clear();

        fileReader.close();
        return all_grades;
    }


    private float count_middle(List<Double> grades) {
        int sum_of_grades = 0;

        for (Double grade : grades) {
            sum_of_grades += grade;
        }

        return ((float) sum_of_grades / grades.size());
    }

    private void adding_grade(String[] list_line) {
        double grade = Integer.parseInt(list_line[list_line.length - 1]);
        grades.add(grade);
    }

    private void checking_line(Integer line_counter, String[] list_line, String[] lessons, String[] check_grades) {
        if ((line_counter > 5) && (list_line.length == 0)) {
            throw new AssertionError("Количество предметов больше 5 или в файле обнаружен пустой массив!");
        }else if (!Arrays.asList(lessons).contains(list_line[0])){
            throw new AssertionError("Данные не совпадают в первом элементе массива!");
        }else if ((!list_line[1].equals("-")) && (!Arrays.asList(lessons).contains(list_line[1]))){
            throw new AssertionError("Данные не совпадают во втором элементе массива!");
        } else if ((!Arrays.asList(check_grades).contains(list_line[2])) && (!Arrays.asList(check_grades).contains(list_line[3]))){
            throw new AssertionError("Данные не совпадают в третьем элементе массива!");
        }
    }
}
