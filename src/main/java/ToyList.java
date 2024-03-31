package src.main.java;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Класс, призванный собирать объекты игрушек Toy из разных источников в один
 * Hashmap вида <id игрушки,объект игрушки> для быстрого доступа.
 * Чтобы избежать дублирования, переделывает индексы на max(key)++ у тех
 * игрушек, что добавляются вручную.
 * (контроль актуальных id в текстовом файле на совести скармливающего данные)
 */
public class ToyList {
    HashMap<Integer, Toy> toys = new HashMap<>();
    String toyFilepath;
    FileIO f = new FileIO();
    protected int maxKey;

    public ToyList(String filepath) {
        this.readFromFile(filepath);
        this.maxKey = Collections.max(toys.keySet());
    }

    public ToyList() {
        this.readFromFile("./toylist.txt");
        this.maxKey = Collections.max(toys.keySet());
    }

    void addToyList(Collection<Toy> newtoys) {
        for (Toy t : newtoys) {
            this.addToy(t);
        }
    }

    /**
     * добавляем одну игрушку. Если ключи совпали, назначается id =
     * максимально_знакомый_ключ+1
     * 
     * @param t объект игрушки
     * @return сообщаем,под каким id игрушка была записана
     */
    int addToy(Toy t) {
        int finalId = t.id;
        if (toys.containsKey(t.id)) {
            finalId = ++maxKey;
            t.setId(finalId);
        }
        toys.put(t.id, t);
        return finalId;
    }

    void removeToy(int idNum) {
        toys.remove(idNum);
    }

    /**
     * добавление игрушек из файла через указание пути к конкретному файлу
     * 
     * @param filepath относительный путь будет начинаться в корне проекта("./"
     *                 будет там же, где лежит readme.md)
     */
    void readFromFile(String filepath) {
        this.toyFilepath = filepath;
        for (String line : f.readToys(filepath)) {
            String[] toyParams = line.split(" ", 4);
            int toyId = Integer.parseInt(toyParams[0]);
            toys.put(toyId, new Toy(toyId,
                    Double.parseDouble(toyParams[1]),
                    toyParams[3],
                    Integer.parseInt(toyParams[2])));
        }

    }

    void saveToFile() {
        f.writeToys(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Toy t : toys.values()) {
            sb.append(t.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}