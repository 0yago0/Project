import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;

    static String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    static String[] commodities = {"Gold", "Oil", "Silver", "Copper", "Gas"};
    static int[][][] allData = new int[MONTHS][DAYS][COMMS];



    // --- YARDIMCI METOTLAR ---
    public static int getCommodityIndex(String name) {
        for (int i = 0; i < commodities.length; i++) {
            if (commodities[i].equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    public static void loadData() {
        for (int m = 0; m < MONTHS; m++) {
            String filename = "Project/Data_Files/" + months[m] + ".txt";
            try {
                Scanner sc = new Scanner(new File(filename));
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (line.startsWith("Day") || line.trim().isEmpty()) {
                        continue;
                    }
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
                System.out.println("HATA: " + filename + " bulunamadı!");
            }
        }
    }

    // --- ANALİZ METOTLARI (1-10 SIRALI) ---


    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) return "INVALID_MONTH";
        long maxProfit = Long.MIN_VALUE;
        String bestComm = "";
        for (int c = 0; c < COMMS; c++) {
            long total = 0;
            for (int d = 0; d < DAYS; d++) total += allData[month][d][c];
            if (total > maxProfit) {
                maxProfit = total;
                bestComm = commodities[c];
            }
        }
        return bestComm + " " + maxProfit;
    }
    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) {
            return -99999;
        }
        int total = 0;
        for (int c = 0; c < COMMS; c++){
            total += allData[month][day - 1][c];
        }
        return total;
    }
    public static int commodityProfitInRange(String commodity, int from, int to) {
        int index = getCommodityIndex(commodity);
        if (index == -1 || from < 1 || to > DAYS || from > to){
            return -9999;
        }
        int totalProfit = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = from - 1; d < to; d++){
                totalProfit += allData[m][d][index];
            }
        }
        return totalProfit;
    }

    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS){
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

    public static String bestMonthForCommodity(String comm) {
        int index = getCommodityIndex(comm);
        if (index == -1){
            return "INVALID_COMMODITY";
        }
        long maxProfit = Long.MIN_VALUE;
        int bestMonthIndex = -1;
        for (int m = 0; m < MONTHS; m++) {
            long currentMonthTotal = 0;
            for (int d = 0; d < DAYS; d++){
                currentMonthTotal += allData[m][d][index];
            }
            if (currentMonthTotal > maxProfit) {
                maxProfit = currentMonthTotal;
                bestMonthIndex = m;
            }
        }
        return months[bestMonthIndex];
    }
    public static int consecutiveLossDays(String comm) {
        int index = getCommodityIndex(comm);
        if (index == -1) return -1;
        int maxStreak = 0, currentStreak = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (allData[m][d][index] < 0) {
                    currentStreak++;
                    if (currentStreak > maxStreak){
                        maxStreak = currentStreak;
                    }
                } else {
                    currentStreak = 0;
                }
            }
        }
        return maxStreak;
    }
    public static int daysAboveThreshold(String comm, int threshold) {
        int index = getCommodityIndex(comm);
        if (index == -1){
            return -1;
        }
        int count = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (allData[m][d][index] > threshold){
                    count++;
                }
            }
        }
        return count;
    }
    public static int biggestDailySwing(int month) {
        if (month < 0 || month >= MONTHS){
            return -99999;
        }
        int maxSwing = -1;
        for (int d = 0; d < DAYS; d++) {
            int dailyMax = Integer.MIN_VALUE;
            int dailyMin = Integer.MAX_VALUE;
            for (int c = 0; c < COMMS; c++) {
                int profit = allData[month][d][c];
                if (profit > dailyMax){
                    dailyMax = profit;
                }
                if (profit < dailyMin){
                    dailyMin = profit;
                }
            }
            int currentSwing = dailyMax - dailyMin;
            if (currentSwing > maxSwing){
                maxSwing = currentSwing;
            }
        }
        return maxSwing;
    }
    public static String compareTwoCommodities(String c1, String c2) {
        int commindex1 = getCommodityIndex(c1);
        int commindex2 = getCommodityIndex(c2);
        if (commindex1 == -1 || commindex2 == -1) {
            return "INVALID_COMMODITY";
        }
        long total1 = 0;
        long total2 = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                total1 += allData[m][d][commindex1];
                total2 += allData[m][d][commindex2];
            }
        }
        if (total1 > total2){
            return c1 + " is better by " + (total1 - total2);
        }
        if (total2 > total1){
            return c2 + " is better by " + (total2 - total1);
        }
        return "Both are equal";
    }

    public static String bestWeekOfMonth(int month) {
        if (month < 0 || month >= MONTHS){
            return "INVALID_MONTH";
        }
        long[] weeklyProfits = new long[4];
        for (int w = 0; w < 4; w++) {
            int startDay = w * 7;
            int endDay = startDay + 7;
            for (int d = startDay; d < endDay; d++) {
                for (int c = 0; c < COMMS; c++){
                    weeklyProfits[w] += allData[month][d][c];
                }
            }
        }
        long maxW = Long.MIN_VALUE;
        int bestW = -1;
        for (int w = 0; w < 4; w++) {
            if (weeklyProfits[w] > maxW) {
                maxW = weeklyProfits[w];
                bestW = w + 1;
            }
        }
        return "Week " + bestW;
    }

    public static void main(String[] args) {
        loadData();
    }
}