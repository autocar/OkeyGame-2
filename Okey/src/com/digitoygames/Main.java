
package com.digitoygames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * @author sedos17
 *
 */
public class Main {

    //variables    
    static List<Stone> player1 = new ArrayList<>();
    static List<Stone> player2 = new ArrayList<>();
    static List<Stone> player3 = new ArrayList<>();
    static List<Stone> player4 = new ArrayList<>();
    static List<Integer> stones;
    private static int okey, gosterge;
    static Random random = new Random();
	private static Scanner scanner;

    public static void main(String[] args) {

        createStones();
        mixStones();
        generateOkey();
        distributeStones();
        int bestHand = FindBestHand();

        System.out.println("The best hand player : Player" + bestHand);
        scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    private static void createStones() {
        try {
            stones = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 53; i++) {
                    stones.add(i);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static void mixStones() {
        try {
            Collections.shuffle(stones);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    private static void generateOkey() {

        try {
            gosterge = random.nextInt(53);
            switch (gosterge) {
                case 12:
                    okey = 0;
                    break;
                case 25:
                    okey = 13;
                    break;
                case 38:
                    okey = 26;
                    break;
                case 51:
                    okey = 39;
                    break;
                default:
                    okey = gosterge + 1;
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    

    private static void distributeStones() {
        try {
            int gostergeGivenNum = 0;
            for (int i = 0; i < 56; i++) {
                int stoneToAdd = stones.get(i);

                // 1 kere gösterge daðýtýldýðýna dair kontrol
                if (gosterge == stones.get(i)) {
                    gostergeGivenNum++;
                    if (gostergeGivenNum == 2) {
                        stoneToAdd = stones.get(57);
                    }
                }

                if (i < 14) {
                    player1.add(new Stone(stoneToAdd));
                } else if (i < 28) {
                    player2.add(new Stone(stoneToAdd));
                } else if (i < 42) {
                    player3.add(new Stone(stoneToAdd));
                } else if (i < 56) {
                    player4.add(new Stone(stoneToAdd));
                }
            }

            int newStone = gostergeGivenNum == 2 ? stones.get(58) : stones.get(57); // 1 kere gösterge daðýtýldýðýna dair kontrol
            if (gostergeGivenNum != 0 && newStone == gosterge) {
                newStone = stones.get(59);
            }
            int fifteenStoneUser = random.nextInt(4);
            switch (fifteenStoneUser) {
                case 0:
                    player1.add(new Stone(newStone));
                    break;
                case 1:
                    player2.add(new Stone(newStone));
                    break;
                case 2:
                    player3.add(new Stone(newStone));
                    break;
                case 3:
                    player4.add(new Stone(newStone));
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    /**
     * which player is the best
     */
    private static int FindBestHand() {
    	
        int player1Points = calculateHandPoint(player1);
        int player2Points = calculateHandPoint(player2);
        int player3Points = calculateHandPoint(player3);
        int player4Points = calculateHandPoint(player4);
        
        if (player1Points > player2Points) {
            if (player3Points > player4Points) {
                if (player3Points > player1Points) {
                    return 3;
                } else {
                    return 1;
                }
            } else {
                if (player4Points > player1Points) {
                    return 4;
                } else {
                    return 1;
                }
            }
        } else {
            if (player3Points > player4Points) {
                if (player3Points > player2Points) {
                    return 3;
                } else {
                    return 2;
                }
            } else {
                if (player4Points > player2Points) {
                    return 4;
                } else {
                    return 2;
                }
            }
        }
    }



    private static int calculateHandPoint(List<Stone> userStones) {
    	int point = 0;
        int jokerCount = Collections.frequency(userStones, new Stone(okey));
        point += jokerCount * 3;

        userStones.forEach(s -> s.setNumber(s.getNumber() == okey ? 999 : s.getNumber()));
        userStones.forEach(s -> s.setNumber(s.getNumber() == 52 ? okey : s.getNumber()));
        Collections.sort(userStones, Comparator.comparing(o -> o.getNumber()));
        Predicate<Stone> byYellow = p -> ((int) (p.getNumber() / 13)) == 0;
        Predicate<Stone> byBlue = p -> ((int) (p.getNumber() / 13)) == 1;
        Predicate<Stone> byBlack = p -> ((int) (p.getNumber() / 13)) == 2;
        Predicate<Stone> byRed = p -> ((int) (p.getNumber() / 13)) == 3;
        List<List<Integer>> groupsYellow = groupConsecutive(userStones.stream().filter(byYellow).collect(Collectors.<Stone>toList()));
        List<List<Integer>> groupsBlue = groupConsecutive(userStones.stream().filter(byBlue).collect(Collectors.<Stone>toList()));
        List<List<Integer>> groupsBlack = groupConsecutive(userStones.stream().filter(byBlack).collect(Collectors.<Stone>toList()));
        List<List<Integer>> groupsRed = groupConsecutive(userStones.stream().filter(byRed).collect(Collectors.<Stone>toList()));

        int gainPoint = calcPoints(groupsYellow, userStones);
        point += gainPoint;
        gainPoint = calcPoints(groupsBlue, userStones);
        point += gainPoint;
        gainPoint = calcPoints(groupsBlack, userStones);
        point += gainPoint;
        gainPoint = calcPoints(groupsRed, userStones);
        point += gainPoint;


        for (int i = 0; i < userStones.size(); i++) {
            if (!userStones.get(i).isUsed()) {
                int finalI = i;
                List<Stone> lst = userStones.stream()
                        .filter((stone) -> (stone.isUsed() == false) && (

                                stone.getNumber() == userStones.get(finalI).getNumber() + 13
                                        || stone.getNumber() == userStones.get(finalI).getNumber() + 26
                                        || stone.getNumber() == userStones.get(finalI).getNumber() + 39
                        )).filter(ObjectHelper.distinctByKey(s -> s.getNumber())).collect(Collectors.toList());


                long count = lst.size() + 1;
                if (count == 2) {
                    point += 1;
                } else if (count == 3) {
                    point += 3;
                } else if (count == 4) {
                    point += 4;
                }
                if (count > 2) {
                    for (Stone item : lst) {
                        int index = getIndexByProperty(userStones, item.getNumber());
                        Stone newObj = new Stone(item.getNumber());
                        newObj.setUsed(true);
                        userStones.set(index, newObj);
                    }
                    Stone newObj = new Stone(userStones.get(i).getNumber());
                    newObj.setUsed(true);
                    userStones.set(i, newObj);

                }
            }
        }

        //control for twinPoint
        int twinPoint = 0;
        for (int i = 0; i < userStones.size(); i++) {
            if (userStones.get(i).getNumber() != 999 && Collections.frequency(userStones, userStones.get(i).getNumber()) == 2) {
                twinPoint += 2;
            }
        }
        twinPoint += (jokerCount * 2);
        return Math.max(point, twinPoint);
    }

 
    private static int calcPoints(List<List<Integer>> group, List<Stone> userStones) {
        int plusPoint = 0;
        try {
            if (group != null && group.size() != 0) {

                for (int i = 0; i < group.size(); i++) {
                    plusPoint += getPoint(group.get(i).size());
                    if (group.get(i).size() > 2) {
                        for (Integer item : group.get(i)) {
                            int index = getIndexByProperty(userStones, item);
                            Stone stone = new Stone(userStones.get(i).getNumber());
                            stone.setUsed(true);
                            userStones.set(index, stone);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return plusPoint;
    }

    /**
     * return Point
     */
    private static int getPoint(int v) {
        int newPoint = 0;
        switch (v) {
            case 0:
            case 1:
                break;
            case 2:
                newPoint = 1;
                break;
            case 3:
            case 4:
                newPoint = v;
                break;
            case 5:
                newPoint = 4;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                newPoint = v;
                break;
            default:
                break;
        }

        return newPoint;
    }

    public static int getIndexByProperty(List<Stone> userStones, Integer prop) {
        for (int i = 0; i < userStones.size(); i++) {
            if (userStones.get(i).getNumber().equals(prop)) {
                return i;
            }
        }
        return -1;
    }

	

    /**
     * list of same color stone control and listed total
     */
    public static List<List<Integer>> groupConsecutive(List<Stone> list) {
        try {
            List<List<Integer>> listMain = new ArrayList<>();
            List<Integer> temp = new ArrayList<>();
            if (list.size() != 0) {
                temp.add(list.get(0).getNumber());
                for (int i = 0; i < list.size() - 1; i++) {
                    if (list.get(i + 1).getNumber() == list.get(i).getNumber() + 1) {
                        temp.add(list.get(i + 1).getNumber());
                    } else {
                        listMain.add(temp);
                        temp = new ArrayList<>();
                        temp.add(list.get(i + 1).getNumber());
                    }
                }
                listMain.add(temp);
                return listMain;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}


