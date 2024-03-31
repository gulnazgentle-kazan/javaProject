package src.main.java;

/**
 * Класс участника, присваивающий "электронный талон" с автоинкрементом
 * в поле id
 */
public class Participant implements Comparable<Participant> {
    String name;
    int id;
    static int totalCount = 0;

    public Participant(String name) {
        this.name = name;
        this.id = totalCount++;
    }

    public Participant() {
        this("Ребёнок" + totalCount);
    }

    @Override
    public String toString() {
        return String.format("Участник %s, под номером %d", this.name, this.id);
    }

    @Override
    public int compareTo(Participant o) {
        if (o.id == this.id) {
            return 0;
        }
        return this.id < o.id ? -1 : 1;
    }
}