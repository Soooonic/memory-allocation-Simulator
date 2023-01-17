import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Simulator {
    private int partitionCnt;
    private ArrayList<Process> processes;
    private ArrayList<Partition> partitions;

    public Simulator() {
        processes = new ArrayList<>();
        partitionCnt = 0;
        partitions = new ArrayList<>();
    }

    public void initialize() {
        Scanner intVal = new Scanner(System.in);
        Scanner strVal = new Scanner(System.in);
        // process partitions
        System.out.println("Enter the number of partitions ");
        int noOfPartitions = intVal.nextInt();
        // fill partitions array list
        for (int i = 0; i < noOfPartitions; ++i) {
            Partition part = new Partition();
            System.out.println("Enter the name of the partition");
            String name = strVal.next();
            System.out.println("Enter the size of the partition");
            int siz = intVal.nextInt();
            part.setName(name);
            part.setSize(siz);
            partitions.add(part);
            partitionCnt++;
        }
        // process processes
        System.out.println("Enter the number of processes ");
        int noOfProcesses = intVal.nextInt();
        // fill processes array list
        for (int i = 0; i < noOfProcesses; ++i) {
            Process process = new Process();
            System.out.println("Enter the name of the process");
            String name = strVal.next();
            System.out.println("Enter the size of the process");
            int siz = intVal.nextInt();
            process.setName(name);
            process.setSize(siz);
            processes.add(process);
        }
    }

    public void selectPolicy() {
        System.out.println("Select the policy you want to apply ( enter a number )");
        System.out.println("1. First fit");
        System.out.println("2. Best fit");
        System.out.println("3. Worst fit");
        Scanner intVal = new Scanner(System.in);
        int input = intVal.nextInt();
        switch (input) {
            case 1: {
                firstFit();
                System.out.println("Do you want to compact ?    1.yes   2.no ");
                int val = intVal.nextInt();
                if (val == 1) {
                    compact();
                    firstFit();
                }
            }
            break;
            case 2: {
                bestFit();
                System.out.println("Do you want to compact ?    1.yes   2.no ");
                int val = intVal.nextInt();
                if (val == 1) {
                    compact();
                    bestFit();
                }
            }
            break;
            case 3: {
                worstFit();
                System.out.println("Do you want to compact ?    1.yes   2.no ");
                int val = intVal.nextInt();
                if (val == 1) {
                    compact();
                    worstFit();
                }
            }
            break;
            default: {
                System.out.println("invalid number");
            }
        }
    }

    public void compact() {
        int siz = 0;
        for (int i = 0; i < partitions.size(); ++i) {
            if (partitions.get(i).process != null)
                continue;
            siz += partitions.get(i).getSize();
            partitions.remove(i);
            i--;
        }
        // initialize a new partition that has the sum of all the empty partitions
        Partition part = new Partition();
        part.setName("part_" + String.valueOf(partitionCnt));
        part.setSize(siz);
        // compact to the array
        partitions.add(part);
        partitionCnt++;
    }

    public void firstFit() {
        for (int i = 0; i < processes.size(); ++i) {
            for (int j = 0; j < partitions.size(); ++j) {
                // check if this partition don't contain a process
                if (partitions.get(j).process == null) {
                    // if the size is large enough to hold the process
                    if (partitions.get(j).getSize() >= processes.get(i).getSize()) {
                        // set process
                        partitions.get(j).process = processes.get(i);
                        int siz = partitions.get(j).getSize() - processes.get(i).getSize();
                        // modify the size of the partition
                        partitions.get(j).setSize(processes.get(i).getSize());
                        if (siz > 0) {
                            Partition part = new Partition();
                            part.setSize(siz);
                            part.setName("part_" + String.valueOf(partitionCnt));
                            partitionCnt++;
                            // add the new partition
                            partitions.add(j + 1, part);
                            // remove this process from the queue
                            processes.remove(i);
                            // to adjust the index in the next for loop iteration
                            i--;   // after removing p1 the index will point to p3
                        }
                        // break and get the next process
                        break;
                    }
                }
            }
        }
        for (int j = 0; j < partitions.size(); ++j) {
            if (partitions.get(j).process != null)
                System.out.println(partitions.get(j).getName() + " (" + partitions.get(j).process.getSize() + " KB) => " + partitions.get(j).process.getName());
            else
                System.out.println(partitions.get(j).getName() + " (" + partitions.get(j).getSize() + " KB) => External Fragment");
        }
        for (int i = 0; i < processes.size(); ++i) {
            System.out.println(processes.get(i).getName() + " can not be allocated");

        }
    }

//// best fit fun priorities the smallest partition in selection
public void bestFit() {
        for (int i = 0; i < processes.size(); i++) {
            int min = Integer.MAX_VALUE, min_indx = 0;
            for (int j = 0; j < partitions.size(); j++) {
                // check if this partition don't contain a process
                int diff = partitions.get(j).getSize() - processes.get(i).getSize();
                if (diff < min && diff >= 0) {  //// store the min difference which is best fit
                    min = diff;
                    min_indx = j;
                }
            }
            /// if the target partition is empy and the processes state is true still doesnt belong to any partition
            if (partitions.get(min_indx).process == null && processes.get(i).getstate()) {
                partitions.get(min_indx).process = processes.get(i);
                // when process enter the partition its state becomes false
                processes.get(i).setState(false);
                int siz = partitions.get(min_indx).getSize() - processes.get(i).getSize();
                // modify the size of the partition
                partitions.get(min_indx).setSize(processes.get(i).getSize());
                if (siz > 0) {
                    Partition part = new Partition();
                    part.setSize(siz);
                    part.setName("part_" + String.valueOf(partitionCnt));
                    partitionCnt++;
                    processes.remove(i);
                    // to adjust the index in the next for loop iteration
                    i--;
                    // add the new partition
                    partitions.add(min_indx + 1, part);
                }
            }
        }
        for (int j = 0; j < partitions.size(); ++j) {
            if (partitions.get(j).process != null)
                System.out.println(partitions.get(j).getName() + " (" + partitions.get(j).process.getSize() + " KB) => " + partitions.get(j).process.getName());
            else
                System.out.println(partitions.get(j).getName() + " (" + partitions.get(j).getSize() + " KB) => External Fragment");
        }
       //// print true / free processes which are not  allocated
        for (int i = 0; i < processes.size(); ++i) {
            if (processes.get(i).getstate())
                System.out.println(processes.get(i).getName() + " can not be allocated");

        }
    }

 //// worst fit fun priorities the biggest partition in selection
 public void worstFit() {
        for (int i = 0; i < processes.size(); i++) {
            int max = Integer.MIN_VALUE, max_indx = processes.size() - 1;
            for (int j = 0; j < partitions.size(); j++) {
                int diff = partitions.get(j).getSize() - processes.get(i).getSize();
                //// if it has the max diff it must be an empty part otherwise we will overwrite a used value
                if (diff > max && diff >= 0) {
                    max = diff;
                    max_indx = j;
                }
            }
            /// if the target partition is empy and the processes state is true still doesnt belong to any partition
            if (partitions.get(max_indx).process == null && processes.get(i).getstate()) {
                if (partitions.get(max_indx).process == null) {
                    partitions.get(max_indx).process = processes.get(i);// modify the size of the partition
                    // when process enter the partition its state becomes false
                    partitions.get(max_indx).process.setState(false);
                    int siz = partitions.get(max_indx).getSize() - processes.get(i).getSize();
                    // modify the size of the partition
                    partitions.get(max_indx).setSize(processes.get(i).getSize());
                    if (siz > 0) {
                        Partition part = new Partition();
                        part.setSize(siz);
                        part.setName("part_" + String.valueOf(partitionCnt));
                        partitionCnt++;
                        processes.remove(i);
                        // to adjust the index in the next for loop iteration
                        i--;
                        // add the new partition
                        partitions.add(max_indx + 1, part);
                    }
                }
            }
        }
            for (int j = 0; j < partitions.size(); ++j) {
                if (partitions.get(j).process != null)
                    System.out.println(partitions.get(j).getName() + " (" + partitions.get(j).process.getSize() + " KB) => " + partitions.get(j).process.getName());
                else
                    System.out.println(partitions.get(j).getName() + " (" + partitions.get(j).getSize() + " KB) => External Fragment");
            }
        for (int i = 0; i < processes.size(); ++i) {
            //// print true / free processes which are not  allocated
            if(processes.get(i).getstate())
                System.out.println(processes.get(i).getName() + " can not be allocated");

             }
        }
}

