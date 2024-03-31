package src.main.java;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Класс, который смотрит веса игрушек, назначает им относительный шанс в
 * текущем розыгрыше, и разыгрывает игрушки по списку участников.
 * Шансы считаются по простой формуле (вес игрушки/общий вес очереди),
 * возможность проигрыша закладывается переменной lossWeight,
 * которая задает вес вероятного проигрыша.
 */
public class Raffle {
    ToyList currentToys;
    ParticipantQueue currentParticipants;
    double lossWeight = 0; // 0 для соответствия заданию, где веса разбиваются на полную вероятность в 100%
    int lossId;

    ChanceCalc cc = new ChanceCalc();
    Raffle.QuantityCalc qc = new Raffle.QuantityCalc();

    public Raffle(ParticipantQueue kids, ToyList tl) {

        this.currentToys = cc.assignChance(tl);
        this.currentParticipants = kids;
    }

    /**
     * основной метод перебора розыгрыша
     */
    public void runRaffle() {
        ParticipantQueue kids = this.currentParticipants;
        ToyList tl = this.currentToys;
        PriorityQueue<Toy> prizes = new PriorityQueue<>(tl.toys.values());
        try {
            BufferedWriter log = FileIO.raffleLog();

            while(kids.iterator().hasNext()){
                double winRoll = cc.doRoll();
                Participant k = kids.iterator().next();
                try {
                    Toy win = cc.checkPrize(prizes, winRoll);
                    //showRoll(k,win,winRoll);
                    prizes = qc.adjustQuantityLeft(win,tl,prizes);
                    log.write(showWin(k, win) + "\n");
                } catch(Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            log.close();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

String showWin(Participant kid, Toy prize) {
        String winLine;
        if(prize.name.equals("ничего")){
            winLine = kid.toString() + " н