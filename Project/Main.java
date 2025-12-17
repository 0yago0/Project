import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};

    static int[][][] allData = new int[MONTHS][DAYS][COMMS];

    public static int getCommodityIndex(String name) {
        for (int i = 0; i < commodities.length; i++) {
            if (commodities[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    // ======== REQUIRED METHOD LOAD DATA (Students fill these) ========
    public static void loadData() {
        for (int m = 0; m < MONTHS; m++) {
            String filename = "Data_Files/" + months[m] + ".txt";

            try {
                Scanner sc = new Scanner(new File(filename));

                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");

                    if (parts.length == 3) {
                        int day = Integer.parseInt(parts[0].trim()) - 1;
                        String commName = parts[1].trim();
                        int commIndex = getCommodityIndex(commName);
                        int profit = Integer.parseInt(parts[2].trim());

                        if (day >= 0 && day < DAYS && commIndex != -1) {
                            allData[m][day][commIndex] = profit;
                        }
                    }
                }
                sc.close();
            } catch (FileNotFoundException e) {

            }
        }
    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    // 1. O ay en çok kar eden emtia
    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }

        int maxProfit = Integer.MIN_VALUE;
        String bestCommName = "";

        for (int c = 0; c < COMMS; c++) {
            int currentCommTotal = 0;
            // Emtianın o aydaki toplam karını hesapla
            for (int d = 0; d < DAYS; d++) {
                currentCommTotal += allData[month][d][c];
            }

            if (currentCommTotal > maxProfit) {
                maxProfit = currentCommTotal;
                bestCommName = commodities[c]; //
            }
        }

        return bestCommName + " " + maxProfit;
    }

    // 2. Belirli bir gündeki toplam kar
    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) {
            return -99999;
        }

        int dayIndex = day - 1; // 1.gün 0. index
        int total = 0;

        for (int c = 0; c < COMMS; c++) {
            total += allData[month][dayIndex][c];
        }
        return total;
    }


    // 4. Ayın en karlı günü
    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) {
            return -1;
        }

        int maxProfit = Integer.MIN_VALUE;
        int bestDay = -1;

        for (int d = 1; d <= DAYS; d++) {
            int dailyTotal = totalProfitOnDay(month, d);
            if (dailyTotal > maxProfit) {
                maxProfit = dailyTotal;
                bestDay = d;
            }
        }
        return bestDay;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        int commIndex = getCommodityIndex(commodity);
        if(commIndex == -1 || from < 1 || to> DAYS || from > to){
            return -9999;
        }
        int totalProfit = 0;
        for(int m = 0;m<MONTHS; m++){
            for(int d = from -1;d<to;d++){
                totalProfit += allData[m][d][commIndex];
            }
        }
        return totalProfit;
    }

    public static String bestMonthForCommodity(String comm) {
        int commIndex = getCommodityIndex(comm);
        if(commIndex == -1){
            return "INVALID_COMMODITY";
        }
        int maxProfit = Integer.MIN_VALUE;
        int bestMonthIndex = -1;
        for(int m = 0;m<MONTHS;m++) {
            int currentMonthTotal = 0;

            for (int d = 0; d < DAYS; d++) {
                currentMonthTotal += allData[m][d][commIndex];

                if(currentMonthTotal > maxProfit){
                    maxProfit = currentMonthTotal;
                    bestMonthIndex = m;
                }
            }
        }
        return months[bestMonthIndex];
    }

    public static int consecutiveLossDays(String comm) {
        return 1234;
    }

    public static int daysAboveThreshold(String comm, int threshold) {
        return 1234;
    }

    public static int biggestDailySwing(int month) {
        return 1234;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        return "DUMMY is better by 1234";
    }

    public static String bestWeekOfMonth(int month) {
        return "DUMMY";
    }


    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");

    }

}