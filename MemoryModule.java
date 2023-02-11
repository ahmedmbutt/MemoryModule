/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package memorymodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MemoryModule {

    /**
     * @param args the command line arguments
     */
    private static final int SIZE = 1024;
    private static final String[] Memory = new String[SIZE];

    static void BestFit(Integer[] process, Integer[] holes) {
        int bestIdx = -1;
        for (int j = 0; j < holes.length; j++) {
            if (process[1] <= holes[j]) {
                if (bestIdx == -1) {
                    bestIdx = j;
                } else if (holes[j] < holes[bestIdx]) {
                    bestIdx = j;
                }
            }
        }
        if (bestIdx == -1) {
            System.err.println("\n** P" + process[0] + " can't be allocated ** Not enough space in Memory **");
            return;
        }
        int j = 0, idx = 0;
        boolean flag = false;
        for (int k = 0; k < SIZE && j < process[1]; k++) {
            if (Memory[k] == null) {
                if (idx == bestIdx) {
                    Memory[k] = "P" + process[0];
                    j++;
                }
                flag = true;
            } else {
                if (flag) {
                    ++idx;
                    flag = false;
                }
            }
        }
    }

    private static void addProcess(List<Integer[]> Process) {
        Integer[][] Processes = Process.toArray(Integer[][]::new);
        Arrays.sort(Processes, (Integer[] c1, Integer[] c2) -> {
            if (c1[2] > c2[2]) {
                return 1;
            } else {
                return -1;
            }
        });
        for (Integer[] process : Processes) {
            int j = 0;
            boolean flag = false;
            List<Integer> holes = new ArrayList<>();
            for (int k = 0; k < SIZE; k++) {
                if (Memory[k] == null) {
                    j++;
                    flag = true;
                } else {
                    if (flag) {
                        holes.add(j);
                        flag = false;
                        j = 0;
                    }
                }
            }
            if (flag) {
                holes.add(j);
            }
            BestFit(process, holes.toArray(Integer[]::new));
        }
    }

    private static void removeProcess(String R) {
        boolean flag = false;
        for (int k = 0; k < SIZE; k++) {
            if (Memory[k] == null ? R == null : Memory[k].equals(R)) {
                Memory[k] = null;
                flag = true;
            }
        }
        if (!flag) {
            System.err.println("** No such process exist in memory! **");
        }
    }

    private static void print() throws InterruptedException {
        for (int k = 0; k < Memory.length - 1; k++) {
            if (k == 0) {
                System.out.println("\n" + k + "  +------------+");
                System.out.println("  \t  " + Memory[0]);
            }
            if (Memory[k] == null ? Memory[k + 1] != null : !Memory[k].equals(Memory[k + 1])) {
                System.out.println(k + "  +------------+");
                System.out.println("  \t  " + Memory[k + 1]);
            }
        }
        System.out.println(Memory.length - 1 + "  +------------+\n");

        int freePartitions = 0;
        List<Integer> allocatedPartitions = new ArrayList<>();
        boolean[] visited = new boolean[SIZE];
        for (int k = 0; k < SIZE; k++) {
            if (visited[k] == true) {
                continue;
            }
            int count = 1;
            for (int j = k + 1; j < SIZE; j++) {
                if (Memory[k] == null ? Memory[j] == null : Memory[k].equals(Memory[j])) {
                    visited[j] = true;
                    count++;
                }
            }
            if (Memory[k] == null) {
                freePartitions = count;
            } else {
                allocatedPartitions.add(count);
            }
        }

        Thread.sleep(1000);
        int sum = allocatedPartitions.stream().mapToInt(val -> val).sum();
        int avg = allocatedPartitions.isEmpty() ? 0 : sum / allocatedPartitions.size();
        int max = allocatedPartitions.isEmpty() ? 0 : Collections.max(allocatedPartitions);
        int min = allocatedPartitions.isEmpty() ? 0 : Collections.min(allocatedPartitions);
        System.out.println("\n  *************************  \n");
        System.out.println("Total no. of processes in Memory: " + allocatedPartitions.size()+"\n");
        System.out.println("Average size of processes in Memory: " + avg+"\n");
        System.out.println("Max size of processes in Memory: " + max+"\n");
        System.out.println("Min size of processes in Memory: " + min+"\n");
        System.out.println("Total amount of available Memory: " + freePartitions+"\n");
        System.out.println("\n  *************************  ");
        new Scanner(System.in).nextLine();        
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int i = 0;
        int opt = 0;
        System.out.println("\n\t***** MEMORY MODULE *****\n");
        do {
            System.out.println("\t1) ENTER A NEW PROCESS\n\n\t2) REMOVE A PROCESS\n\n\t3) SEE CURRENT STATE OF MEMORY\n\n\t4) EXIT");
            do {
                System.out.print("\nEnter your choice: ");
                opt = sc.nextInt();
            } while (opt < 1 || opt > 4);
            switch (opt) {
                case 1:
                System.out.println("\n ***TO STOP, ENTER ANY CHARACTER***\n");                    
                    System.out.println("\tSize   Arrival Time");
                    List<Integer[]> Process = new ArrayList<>();
                    while (true) {
                        try {
                            System.out.print("P" + (i + 1) + "\t");
                            Process.add(new Integer[]{++i, sc.nextInt(), sc.nextInt()});
                        } catch (Exception e) {
                            sc.next();
                            break;
                        }
                    }
                    addProcess(Process);
                    break;
                case 2:
                    System.out.print("\nEnter process name: ");
                    String R = sc.next();
                    removeProcess(R);
                    break;
                case 3:
                    try {
                    print();
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
                break;
                case 4:
                default:
                    break;
            }
            System.out.println();
        } while (opt != 4);
    }
}
