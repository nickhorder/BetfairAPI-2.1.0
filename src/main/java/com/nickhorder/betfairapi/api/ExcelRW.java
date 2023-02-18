package com.nickhorder.betfairapi.api;
import java.io.*;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

import com.nickhorder.betfairapi.entities.SyntheticBalanceSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

//ExcelRW (Excel Read/Write) obtain the Stake Multiplier data, write Betting Reports at End of Day
public class ExcelRW {

    private static String stakeMultiplierExcelPath = "C:\\Users\\X1 Carbon\\OneDrive\\Documents\\Gaming\\Gambling\\Betfair\\Backing The Favourite 2023\\ProgramInputOutput\\stakemultiplierrepo.xlsx";
    private static String SyntheticBalanceSheetExcelPath = "C:\\Users\\X1 Carbon\\OneDrive\\Documents\\Gaming\\Gambling\\Betfair\\Backing The Favourite 2023\\ProgramInputOutput\\SyntheticBalance.xlsx";
   //private static HashMap<Double, Double> stakeMultiplierMap = new HashMap<>();
    private static NavigableMap<Double,Double> stakeMultiplierMap = new TreeMap<Double, Double>();

  //  private static HashMap<Double, Double> stakeMultiplierMapSorted = new HashMap<>();

    public ExcelRW() throws FileNotFoundException {
    }

    // Read the offline stakemultiplierrepo XLS once, at startup, and move into NavigableMap.
    public static NavigableMap<Double, Double> createStakeMultiplierMap() throws FileNotFoundException {

        try {
            File stakeMultiplierExcel = new File(stakeMultiplierExcelPath);
            FileInputStream fis = new FileInputStream(stakeMultiplierExcel);
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    int colIndex0 = 0, colIndex1 = 1;
                    Cell cell0 = row.getCell(colIndex0);
                    Cell cell1 = row.getCell(colIndex1);
                    if (cell0 != null && cell1 != null) {
                        // Add Odds Range & Multiplier to HashMap
                        stakeMultiplierMap.put(cell0.getNumericCellValue(), cell1.getNumericCellValue());
                    }
                }
            }
      //      NavigableMap<Double,Double> stakeMultiplierMapSorted = stakeMultiplierMap.entrySet().stream()
        //    LinkedHashMap<Double, Double> stakeMultiplierMapSorted = stakeMultiplierMap.entrySet().stream()
          //         .sorted(Map.Entry.comparingByKey(/* Optional: Comparator.reverseOrder() */))
          //          .collect(Collectors.toMap(Map.Entry::getKey,
          //                Map.Entry::getValue,
          //                (e1, e2) -> e1, LinkedHashMap::new));
            //This will print the elements of the hashmap
            //    Set<Map.Entry<Double, Double>> entries = stakeMultiplierMap.entrySet();
            //  for (Map.Entry<Double, Double> entry : entries) {
            //     System.out.println(entry.getKey() + " ==> " + entry.getValue());
            // }
            //    sortStakeMultiplierMap(stakeMultiplierMap);
            return stakeMultiplierMap;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Read the offline SyntheticBalanceSheet XLS once, at startup, and move into List.
      public static List<SyntheticBalanceSheet> importSyntheticBalanceSheet() {

        List<SyntheticBalanceSheet> daySyntheticBalanceSheet = new ArrayList<>();

          try {
              File syntheticBalanceSheetExcel = new File(SyntheticBalanceSheetExcelPath);
              FileInputStream fis = new FileInputStream(syntheticBalanceSheetExcel);
              //creating Workbook instance that refers to .xlsx file
              XSSFWorkbook wb = new XSSFWorkbook(fis);
              XSSFSheet sheet = wb.getSheetAt(0);
              for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                  Row row = sheet.getRow(rowIndex);

                  if (row != null) {
                      Cell cell0 = row.getCell(0);
                      Cell cell1 = row.getCell(1);
                      Cell cell2 = row.getCell(2);
                      Cell cell3 = row.getCell(3);
                      Cell cell4 = row.getCell(4);
                      Cell cell5 = row.getCell(5);

                      if (cell0 != null) {
                          SyntheticBalanceSheet importSyntheticBalanceSheet = new SyntheticBalanceSheet();

                          importSyntheticBalanceSheet.setSyntheticBalanceSheetMarketID(cell0.getNumericCellValue());
                          importSyntheticBalanceSheet.setSyntheticBalanceSheetSelectionID((long) cell1.getNumericCellValue());
                          importSyntheticBalanceSheet.setSythenticBalanceSheetStake(cell2.getNumericCellValue());
                          importSyntheticBalanceSheet.setSythenticBalanceSheetReturn(cell3.getNumericCellValue());
                          importSyntheticBalanceSheet.setSythenticBalanceSheetBalance(cell4.getNumericCellValue());
                          importSyntheticBalanceSheet.setSythenticBalanceSheetSMA(cell5.getNumericCellValue());

                          daySyntheticBalanceSheet.add(importSyntheticBalanceSheet);
                      }
                  }
              }
              //This will print the elements of the hashmap
              //    Set<Map.Entry<Double, Double>> entries = stakeMultiplierMap.entrySet();
              //  for (Map.Entry<Double, Double> entry : entries) {
              //     System.out.println(entry.getKey() + " ==> " + entry.getValue());
              // }
              //    sortStakeMultiplierMap(stakeMultiplierMap);

          } catch (IOException e) {
              throw new RuntimeException(e);
          }
          return daySyntheticBalanceSheet;
      }


    //   public void setStakeMultiplierMap(HashMap<Double, Double> stakeMultiplierMap) {
    //       this.stakeMultiplierMap = stakeMultiplierMap;
    //   }


    //  public static HashMap<Double, Double> getStakeMultiplierMapSorted() {
    //     return stakeMultiplierMapSorted;
    //  }

    //   public void setStakeMultiplierMapSorted(HashMap<Double, Double> stakeMultiplierMapSorted) {
    //       this.stakeMultiplierMapSorted = stakeMultiplierMapSorted;
    //   }

    //   public static String getStakeMultiplierExcelPath() {
    //      return stakeMultiplierExcelPath;
    //  }

    //   public void setStakeMultiplierExcelPath(String stakeMultiplierExcelPath) {
    //       this.stakeMultiplierExcelPath = stakeMultiplierExcelPath;
    //   }

    // Read the offline SyntheticBalanceSheet XLS once, at startup, and move into List.
    public static void readSyntheticBalanceSheet() {

    }

    /*
    This is to mod the day's synthetic results back into the Excel. But we're reading the whole Excel file in,
    then adding to it in a list. So just create new at the end of the day, which will have previous day's plus
    todays?
    public static void writeSyntheticBalanceSheet(String SyntheticBalanceSheetExcelPath, File file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
        Sheet sheet = workbook.getSheetAt(0);
        int rowNum = sheet.getLastRowNum() + 1;

        //  Map<Integer, Object[]> prepareData ( int rowNum,
        //  List<BusinessEntity> recordsToWrite){

        Map<Integer, Object[]> data = new HashMap<>();
        for (BusinessEntity entity : recordsToWrite) {
            rowNum++;
            data.put(rowNum, new Object[]{rowNum, entity.getFirstName(),
                    entity.getLastName(), entity.getAge()});
        }
        //  Map<Integer, Object[]> data = prepareData(rowNum, recordsToWrite);

        Set<Integer> keySet = data.keySet();
        for (Integer key : keySet) {
            Row row = sheet.createRow(rowNum++);
            Object[] objArr = data.get(key);
            int cellNum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellNum++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(new File(SyntheticBalanceSheetExcelPath));

        workbook.write(out);
        out.close();

    }
*/
}


