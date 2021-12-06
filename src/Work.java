import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Ques implements Serializable {    // класс - вопрос
    int var;    // тип вопроса:  0 - надпись, 1 - один ответ из всех вохможных, 2 - несколько, 3 - нет вариантов ответа
    String text;    // сам опрос
    ArrayList<String> ans;  // варианты ответов
    Ques(int i, String s) {
        var = i;
        text = s;
    }
    Ques(int i, String s, String t) {
        var = i;
        text = s;
        ans = new ArrayList<>(Arrays.asList(t.split("; ")));
    }
    public String toString() {
        String s = "";
        if (this.var == 1 || this.var == 2)
            for (int i = 0; i < ans.size(); i++) s = s + " " + (i+1) + ") " + ans.get(i) + '\n';
        return this.text + '\n' + s;
    }
}

class Quiz implements Serializable {
    ArrayList<Ques> qu = new ArrayList<>();
    public String toString() {
        String s = "";
        for (Ques ques : qu) s = s + "\n" + ques.toString();
        return s;
    }
    public void savetofile(String f) throws Exception {
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        for (Ques ques : qu) os.writeObject(ques);
        os.close();
    }
    public void readfromfile(String f) throws Exception {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream is = new ObjectInputStream(fis);
        while (fis.available() > 0) qu.add((Ques) is.readObject());
        is.close();
    }
    public void create() {
        Scanner sc = new Scanner(System.in);
        int i; String s, t, ch;
        System.out.print("Введите 0, если вы закончили создавать опрос, иначе - любой символ: "); ch = sc.next();
        while (!ch.equals("0")) {
            System.out.print("Введите тип вопроса: "); i = sc.nextInt();
            System.out.print("Введите вопрос: "); s = sc.next() + sc.nextLine();
            if (i == 1 || i == 2) {
                System.out.print("Введите варианты ответа в 1 строку через '; ': "); t = sc.nextLine();
                this.qu.add(new Ques(i, s, t));
            } else this.qu.add(new Ques(i, s));
            System.out.print("Введите 0, если вы закончили создавать опрос: "); ch = sc.next();
        }
    }
}

class Answ implements Serializable {
    int var;
    String ans;
    Answ(int i) {
        var = i;
    }
    Answ(int i, String s) {
        var = i;
        ans = s;
    }
    public String toString() {
        return var + " " + ans;
    }
}

class Answers implements Serializable {
    ArrayList<Answ> an = new ArrayList<>();
    public String toString() {
        String s = "";
        for (Answ answ : an) s = s + "\n" + answ.toString();
        return s;
    }
    public void readfromfile(String f) throws Exception {
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream is = new ObjectInputStream(fis);
        while (fis.available() > 0) an.add((Answ) is.readObject());
        is.close();
    }
    public void savetofile(String f) throws Exception {
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        for (Answ ans : an) os.writeObject(ans);
        os.close();
    }
    public void save(String f) throws Exception {
        File qud = new File(f);
        this.savetofile(f + "\\Ответ" + qud.list().length);
    }
    public int pruv(Ques q, String a) {
        switch (q.var) {
            case 1 -> {
                try {
                    int aa = Integer.parseInt(a);
                    if (aa < 1 || aa > q.ans.size() + 1)
                        return 0;
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
            case 2 -> {
                String[] sm = a.split(" "); int n;
                for (int i = 0; i < sm.length; i++) {
                    try {
                        n = Integer.parseInt(sm[i]);
                        if (n < 1 || n > q.ans.size() + 1)
                            return 0;
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            }
        }
        return 1;
    }
    public void pas(Quiz q) {
        Scanner sc = new Scanner(System.in); String s; int re;
        System.out.println("Сейчас вы будете проходить опрос.\nНа каждый вопрос необходимо дать ответ в форме, указанной рядом с вопросом, в одной строке.");
        for (int i = 0; i < q.qu.size(); i++) {
            System.out.print(q.qu.get(i).toString());
            if (q.qu.get(i).var == 0) an.add(new Answ(0));
            else {
                if (q.qu.get(i).var == 1) System.out.println("Ответом на вопрос является 1 цифра(число) - номер вашего варианта ответа.");
                if (q.qu.get(i).var == 2) System.out.println("Ответом на вопрос является последовательность цифр(чисел) номеров вариантов ответов, записанная через пробел.");
                if (q.qu.get(i).var == 3) System.out.println("Ответом на вопрос является произвольная строка.");
                System.out.print("Ваш ответ: "); s = sc.nextLine();
                re = this.pruv(q.qu.get(i), s);
                while (re == 0) {
                    System.out.println("Ошибка. Введён некорректный ответ. Введите ответ заново.");
                    System.out.print("Ваш ответ: "); s = sc.nextLine();
                    re = this.pruv(q.qu.get(i), s);
                }
                an.add(new Answ(q.qu.get(i).var, s));
            }
        }
    }
}

public class Work {
    public static void main(String[] args) throws Exception {
        String dir = "D:\\Учёба\\3 семестр\\ООП\\Файлы проекта\\";
        Quiz q = new Quiz(); Answers a = new Answers(); Work ww = new Work();
        Scanner sc = new Scanner(System.in); int re; String na = ""; File qud = new File(dir);
        System.out.print("Выберите действие:\n 0 - выйти из программы\n 1 - добавить опрос\n 2 - пройти опрос\n" +
                " 3 - получить статистику по опросу\nВаше решение: ");
        if (sc.hasNextInt()) re = sc.nextInt(); else re = -1;
        while (re != 0) {
            if (re != -1) {
                System.out.print("Введите имя опроса: ");  na = sc.next() + sc.nextLine();
                qud = new File(dir + na);
                if (qud.exists() && !new File(dir + na + "\\Опрос").exists()) {
                    System.out.println("Ошибка. Файл с опросом с таким названием не существует.");
                    if (!qud.delete())
                        if (!ww.delet(dir + na))
                            System.out.println("Ошибка. Необходимо удалить директорию с именем " + na + ".");
                    continue;
                }
            }
            switch (re) {
                case -1 -> System.out.println("Ошибка. Пожалуйста введите число, соответствующее выбранному действию.");
                case 1 -> {
                    if (qud.exists()) {
                        System.out.println("Ошибка. Опрос с таким названием уже существует.");
                        break;
                    }
                    if (!qud.mkdir()) {
                        System.out.println("Ошибка. Не удалось создать папку для опроса с таким именем.");
                        break;
                    }
                    q.create();
                    System.out.print("Новый опрос '" + na + "' :\n" + q.toString());
                    q.savetofile(qud.getPath() + "\\Опрос");
                    q.qu.clear();
                }
                case 2 -> {
                    if (!qud.exists()) {
                        System.out.println("Ошибка. Опрос с таким названием не существует.");
                        break;
                    }
                    q.readfromfile(dir + na + "\\Опрос");
                    a.pas(q);
                    a.save(dir + na);
                    a.an.clear();
                }
                case 3 -> {
                    if (!qud.exists()) {
                        System.out.println("Ошибка. Опрос с таким названием не существует.");
                        break;
                    }
                    if (!new File(dir + na + "\\Ответ1").exists()) {
                        System.out.println("Ошибка. Опрос с таким названием никто не проходил.");
                        break;
                    }
                    ww.stat(dir + na);
                }
            }
            System.out.print("Выберите действие:\n 0 - выйти из программы\n 1 - добавить опрос\n 2 - пройти опрос\n" +
                    " 3 - получить статистику по опросу\nВаше решение: ");
            if (sc.hasNextInt()) re = sc.nextInt(); else re = -1;
        }
        System.out.print("До свидания!");
    }
    public boolean delet(String f) {
        File qud = new File(f); String[] anfi = qud.list();
        for (String s : anfi)
            if (!new File(f + "\\" + s).delete())
                return false;
        return qud.delete();
    }
    public void stat(String f) throws Exception {
        Scanner sc = new Scanner(System.in); int re = -1, ty = 1;
        System.out.print("Выберите желаемую статистику по опросу:\n 1 - общая\n 2 - по полу\n 3 - по возрасту\n" +
                " 4 - по полу и возрасту\n 5 - общий рейтинг\n 0 - покинуть раздел\nВаше решение: ");
        if (sc.hasNextInt()) re = sc.nextInt();
        while (re != 0) {
            if (re != -1 && re != 5) {
                System.out.print("Выберите в каком ввиде хотите получить статистику:\n 1 - число людей, выбравших данный ответ\n" +
                        " 2 - процент людей, выбравших данный ответ\nВаше решение: ");
                if (sc.hasNextInt()) ty = sc.nextInt();
                else { System.out.println("Ошибка. Пожалуйста введите число, соответствующее выбранному способу вывода."); continue; }
                if (ty != 1 && ty != 2) { System.out.println("Ошибка. Пожалуйста введите число, соответствующее выбранному способу вывода."); continue; }
            }
            switch (re) {
                case -1 -> System.out.println("Ошибка. Пожалуйста введите число, соответствующее выбранному действию.");
                case 1 -> this.statistic(f, ty);
                case 2 -> this.statisticque(f, ty, "Какой у вас пол?");
                case 3 -> this.statisticque(f, ty, "Сколько вам лет?");
                case 4 -> this.statisticquegen(f, ty, "Сколько вам лет?");
                case 5 -> this.rate(f);
            }
            System.out.print("Выберите желаемую статистику по опросу:\n 1 - общая\n 2 - по полу\n 3 - по возрасту\n" +
                    " 4 - по полу и возрасту\n 0 - покинуть раздел\nВаше решение: ");
            if (sc.hasNextInt()) re = sc.nextInt(); else re = -1;
        }
    }
    public void statistic(String f, int ty) throws Exception {
        File qud = new File(f); String[] anfi = qud.list();
        Quiz q = new Quiz(); q.readfromfile(f+ "\\Опрос");
        Answers a = new Answers(); String[] sm;
        int col3 = 0, c3, co;
        Integer[][] st = new Integer[q.qu.size()][];
        for (int i = 0; i < q.qu.size(); i++) if (q.qu.get(i).var == 3) col3++;   //col3 - кол-во вопросов 3 типа
        String[][] s3 = new String[col3][anfi.length - 1];   //массив для хранения вариантов ответов на вопросы 3 типа
        //инициализация s3 и st
        for (int i = 0; i < col3; i++)
            Arrays.fill(s3[i], "");
        for (int i = 0; i < q.qu.size(); i++) {
            if (q.qu.get(i).var == 0)
                st[i] = new Integer[0];
            if (q.qu.get(i).var == 1 || q.qu.get(i).var == 2)
                st[i] = new Integer[q.qu.get(i).ans.size()];
            if (q.qu.get(i).var == 3)
                st[i] = new Integer[anfi.length - 1];
            Arrays.fill(st[i], 0);
        }
        //заполнение st
        for (int j = 1; j < anfi.length; j++) {
            a.readfromfile(f + "\\" + anfi[j]);
            c3 = 0;
            for (int i = 1; i < a.an.size(); i++) {
                switch (a.an.get(i).var) {
                    case 1 -> st[i][Integer.parseInt(a.an.get(i).ans) - 1]++;
                    case 2 -> {
                        sm = a.an.get(i).ans.split(" ");
                        for (String s : sm) st[i][Integer.parseInt(s) - 1]++;
                    }
                    case 3 -> {
                        co = 0;
                        while (!s3[c3][co].equals(a.an.get(i).ans) && !s3[c3][co].equals("")) { co++;}
                        if (s3[c3][co].equals("")) s3[c3][co] = a.an.get(i).ans;
                        st[i][co] ++;
                        c3++;
                    }
                }
            }
            a.an.clear();
        }
        //вывод на экран
        c3 = 0;
        if (ty == 1) {
            System.out.println("\nОбщее число проходивших: " + (anfi.length-1));
            for (int i = 0; i < q.qu.size(); i++) {
                System.out.println(q.qu.get(i).text);
                if (q.qu.get(i).var == 1 || q.qu.get(i).var == 2)
                    for (int j = 0; j < st[i].length; j++)
                        System.out.println(st[i][j] + " - " + q.qu.get(i).ans.get(j));
                if (q.qu.get(i).var == 3) {
                    co = 0;
                    while (!s3[c3][co].equals("")) {
                        System.out.println(st[i][co] + " - " + s3[c3][co]);
                        co++;
                    }
                    c3++;
                }
                System.out.println();
            }
        }
        else {
            for (int i = 0; i < q.qu.size(); i++) {
                System.out.println(q.qu.get(i).text);
                if (q.qu.get(i).var == 1 || q.qu.get(i).var == 2)
                    for (int j = 0; j < st[i].length; j++)
                        System.out.println(Math.round(100.0 * st[i][j]/ (anfi.length-1)) + "%" + " - " + q.qu.get(i).ans.get(j));
                if (q.qu.get(i).var == 3) {
                    co = 0;
                    while (!s3[c3][co].equals("")) {
                        System.out.println(Math.round(100.0 * st[i][co]/ (anfi.length-1)) + "%" + " - " + s3[c3][co]);
                        co++;
                    }
                    c3++;
                }
                System.out.println();
            }
        }
    }
    public void statisticque(String f, int ty, String que) throws Exception {
        File qud = new File(f); String[] anfi = qud.list();
        Quiz q = new Quiz(); q.readfromfile(f+ "\\Опрос");
        Answers a = new Answers(); String[] sm;
        int col3 = 0, c3, co, zn, re = 0;
        //re - номер в опросе вопроса que
        while (!q.qu.get(re).text.equals(que)) { re++; }
        Integer[][][] st = new Integer[q.qu.get(re).ans.size()][q.qu.size()][];
        for (int i = 0; i < q.qu.size(); i++) if (q.qu.get(i).var == 3) col3++;   //col3 - кол-во вопросов 3 типа
        String[][] s3 = new String[col3][anfi.length - 1];   //массив для хранения вариантов ответов на вопросы 3 типа
        //инициализация s3 и st
        for (int i = 0; i < col3; i++)
            Arrays.fill(s3[i], "");
        for (zn = 0; zn < q.qu.get(re).ans.size(); zn++)
            for (int i = 0; i < q.qu.size(); i++) {
                if (q.qu.get(i).var == 0)
                    st[zn][i] = new Integer[0];
                if (q.qu.get(i).var == 1 || q.qu.get(i).var == 2)
                    st[zn][i] = new Integer[q.qu.get(i).ans.size()];
                if (q.qu.get(i).var == 3)
                    st[zn][i] = new Integer[anfi.length - 1];
                Arrays.fill(st[zn][i], 0);
            }
        //заполнение st
        for (int j = 1; j < anfi.length; j++) {
            a.readfromfile(f + "\\" + anfi[j]);
            c3 = 0; zn = Integer.parseInt(a.an.get(re).ans) - 1;
            for (int i = 1; i < a.an.size(); i++) {
                switch (a.an.get(i).var) {
                    case 1 -> st[zn][i][Integer.parseInt(a.an.get(i).ans) - 1]++;
                    case 2 -> {
                        sm = a.an.get(i).ans.split(" ");
                        for (String s : sm) st[zn][i][Integer.parseInt(s) - 1]++;
                    }
                    case 3 -> {
                        co = 0;
                        while (!s3[c3][co].equals(a.an.get(i).ans) && !s3[c3][co].equals("")) { co++;}
                        if (s3[c3][co].equals("")) s3[c3][co] = a.an.get(i).ans;
                        st[zn][i][co] ++;
                        c3++;
                    }
                }
            }
            a.an.clear();
        }
        //вывод на экран
        System.out.println("\nОбщее число проходивших: " + (anfi.length-1) + "\n" + que);
        for (zn = 0; zn < q.qu.get(re).ans.size(); zn++) {
            c3 = 0;
            if (ty == 1) {
                System.out.println(st[zn][re][zn] + " - " + q.qu.get(re).ans.get(zn));
                for (int i = 0; i < q.qu.size(); i++) { if (i != re) {
                    System.out.println(q.qu.get(i).text);
                    if (q.qu.get(i).var == 1 || q.qu.get(i).var == 2)
                        for (int j = 0; j < st[zn][i].length; j++)
                            System.out.println(st[zn][i][j] + " - " + q.qu.get(i).ans.get(j));
                    if (q.qu.get(i).var == 3) {
                        co = 0;
                        while (!s3[c3][co].equals("")) {
                            System.out.println(st[zn][i][co] + " - " + s3[c3][co]);
                            co++;
                        }
                        c3++;
                    }
                    System.out.println();
                } }
            }
            else {
                System.out.println("Среди всех проходивших" + Math.round(100.0 * st[zn][re][zn]/ (anfi.length-1)) + "%" + " - " + q.qu.get(re).ans.get(zn));
                for (int i = 0; i < q.qu.size(); i++) { if (i != re) {
                    System.out.println(q.qu.get(i).text);
                    if (q.qu.get(i).var == 1 || q.qu.get(i).var == 2)
                        for (int j = 0; j < st[zn][i].length; j++)
                            System.out.println(Math.round(100.0 * st[zn][i][j]/ st[zn][re][zn]) + "%" + " - " + q.qu.get(i).ans.get(j));
                    if (q.qu.get(i).var == 3) {
                        co = 0;
                        while (!s3[c3][co].equals("")) {
                            System.out.println(Math.round(100.0 * st[zn][i][co]/ st[zn][re][zn]) + "%" + " - " + s3[c3][co]);
                            co++;
                        }
                        c3++;
                    }
                    System.out.println();
                } }
            }
        }
    }
    public void statisticquegen(String f, int ty, String que) throws Exception {
        File qud = new File(f); String[] anfi = qud.list();
        Quiz q = new Quiz(); q.readfromfile(f+ "\\Опрос");
        Answers a = new Answers(); String[] sm;
        int col3 = 0, c3, co, zn, re = 0, rer = 0, k;
        //re - номер в опросе вопроса que; rer - номер в опросе вопроса "Какой у вас пол?"
        while (!q.qu.get(re).text.equals(que)) { re++; }
        while (!q.qu.get(rer).text.equals("Какой у вас пол?")) { rer++; }
        Integer[][][][] st = new Integer[2][q.qu.get(re).ans.size()][q.qu.size()][];
        for (int i = 0; i < q.qu.size(); i++) if (q.qu.get(i).var == 3) col3++;   //col3 - кол-во вопросов 3 типа
        String[][] s3 = new String[col3][anfi.length - 1];   //массив для хранения вариантов ответов на вопросы 3 типа
        //инициализация s3 и st
        for (int i = 0; i < col3; i++)
            Arrays.fill(s3[i], "");
        for (zn = 0; zn < q.qu.get(re).ans.size(); zn++)
            for (int i = 0; i < q.qu.size(); i++) {
                if (q.qu.get(i).var == 0) {
                    st[0][zn][i] = new Integer[0];
                    st[1][zn][i] = new Integer[0];
                }
                if (q.qu.get(i).var == 1 || q.qu.get(i).var == 2) {
                    st[0][zn][i] = new Integer[q.qu.get(i).ans.size()];
                    st[1][zn][i] = new Integer[q.qu.get(i).ans.size()];
                }
                if (q.qu.get(i).var == 3) {
                    st[0][zn][i] = new Integer[anfi.length - 1];
                    st[1][zn][i] = new Integer[anfi.length - 1];
                }
                Arrays.fill(st[0][zn][i], 0);
                Arrays.fill(st[1][zn][i], 0);
            }
        //заполнение st
        for (int j = 1; j < anfi.length; j++) {
            a.readfromfile(f + "\\" + anfi[j]);
            c3 = 0; zn = Integer.parseInt(a.an.get(re).ans) - 1; k = Integer.parseInt(a.an.get(rer).ans) - 1;
            for (int i = 1; i < a.an.size(); i++) {
                switch (a.an.get(i).var) {
                    case 1 -> st[k][zn][i][Integer.parseInt(a.an.get(i).ans) - 1]++;
                    case 2 -> {
                        sm = a.an.get(i).ans.split(" ");
                        for (String s : sm) st[k][zn][i][Integer.parseInt(s) - 1]++;
                    }
                    case 3 -> {
                        co = 0;
                        while (!s3[c3][co].equals(a.an.get(i).ans) && !s3[c3][co].equals("")) { co++;}
                        if (s3[c3][co].equals("")) s3[c3][co] = a.an.get(i).ans;
                        st[k][zn][i][co] ++;
                        c3++;
                    }
                }
            }
            a.an.clear();
        }
        //вывод на экран
        System.out.println("\nОбщее число проходивших: " + (anfi.length-1) + "\n" + que);
        for (k = 0; k < 2; k++) {
            for (zn = 0; zn < q.qu.get(re).ans.size(); zn++) {
                c3 = 0;
                if (ty == 1) {
                    System.out.println(st[k][zn][rer][k] + " - " + q.qu.get(rer).ans.get(k) + " " + q.qu.get(re).ans.get(zn));
                    if (st[k][zn][re][zn] != 0)
                    for (int i = 0; i < q.qu.size(); i++) {
                        if (i != re && i != rer) {
                            System.out.println(q.qu.get(i).text);
                            if (q.qu.get(i).var == 1 || q.qu.get(i).var == 2)
                                for (int j = 0; j < st[k][zn][i].length; j++)
                                    System.out.println(st[k][zn][i][j] + " - " + q.qu.get(i).ans.get(j));
                            if (q.qu.get(i).var == 3) {
                                co = 0;
                                while (!s3[c3][co].equals("")) {
                                    System.out.println(st[k][zn][i][co] + " - " + s3[c3][co]);
                                    co++;
                                }
                                c3++;
                            }
                            System.out.println();
                        }
                    }
                }
                else {
                    System.out.println(Math.round(100.0 * st[k][zn][rer][k] / (anfi.length - 1)) + "% - " + q.qu.get(rer).ans.get(k) + " " + q.qu.get(re).ans.get(zn));
                    if (st[k][zn][rer][k] != 0)
                    for (int i = 0; i < q.qu.size(); i++) {
                        if (i != re && i != rer) {
                            System.out.println(q.qu.get(i).text);
                            if (q.qu.get(i).var == 1 || q.qu.get(i).var == 2)
                                for (int j = 0; j < st[k][zn][i].length; j++)
                                    System.out.println(Math.round(100.0 * st[k][zn][i][j] / (st[k][zn][rer][k])) + "%" + " - " + q.qu.get(i).ans.get(j));
                            if (q.qu.get(i).var == 3) {
                                co = 0;
                                while (!s3[c3][co].equals("")) {
                                    System.out.println(Math.round(100.0 * st[k][zn][i][co] / (st[k][zn][rer][k])) + "%" + " - " + s3[c3][co]);
                                    co++;
                                }
                                c3++;
                            }
                            System.out.println();
                        }
                    }
                }
            }
        }
    }
    public void rate(String f) throws Exception {
        File qud = new File(f); String[] anfi = qud.list();
        Quiz q = new Quiz(); q.readfromfile(f+ "\\Опрос");
        Answers a = new Answers(); int re = 0, rg = 0, ra = 0;
        while (!q.qu.get(re).text.startsWith("Как бы вы оценили")) { re++; }
        while (!q.qu.get(ra).text.equals("Сколько вам лет?")) { ra++; }
        while (!q.qu.get(rg).text.equals("Какой у вас пол?")) { rg++; }
        Integer[][] st = new Integer[4][q.qu.get(ra).ans.size() + 1];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < q.qu.get(ra).ans.size() + 1; j++)
                st[i][j] = 0;
        for (int j = 1; j < anfi.length; j++) {
            a.readfromfile(f + "\\" + anfi[j]);
            st[Integer.parseInt(a.an.get(rg).ans) - 1][Integer.parseInt(a.an.get(ra).ans) - 1] ++;
            st[Integer.parseInt(a.an.get(rg).ans) - 1][q.qu.get(ra).ans.size()] ++;
            st[Integer.parseInt(a.an.get(rg).ans) + 1][Integer.parseInt(a.an.get(ra).ans) - 1] += Integer.parseInt(a.an.get(re).ans) - 1;
            st[Integer.parseInt(a.an.get(rg).ans) + 1][q.qu.get(ra).ans.size()] += Integer.parseInt(a.an.get(re).ans) - 1;
            a.an.clear();
        }
        System.out.printf("Общий рейтинг: %-5.1f \n", (1.0 * (st[2][q.qu.get(ra).ans.size()] + st[3][q.qu.get(ra).ans.size()]) / (anfi.length - 1)));
        System.out.print("Рейтинг в зависимости от пола и возраста:\n ");
        System.out.print("        ");
        for (int i = 0; i < q.qu.get(ra).ans.size(); i++) {
            System.out.print(q.qu.get(ra).ans.get(i));
            for (int j = 0; j < 11 - q.qu.get(ra).ans.get(i).length(); j++)
                System.out.print(" ");
        }
        System.out.println("общий");
        for (int i = 0; i < 2; i++) {
            if (i == 0) System.out.print("мужчины: ");
            else System.out.print("женщины: ");
            for (int j = 0; j <= q.qu.get(ra).ans.size(); j++)
                if (st[i+2][j] != 0) System.out.printf(" %-5.1f     ", 10.0 * st[i][j] / st[i+2][j]);
                else System.out.print(" 0         ");
            System.out.println();
        }
    }
}
