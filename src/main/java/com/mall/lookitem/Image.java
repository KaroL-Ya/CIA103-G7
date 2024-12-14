package com.mall.lookitem;
import java.io.*;
import java.sql.*;

public class Image {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://localhost:3306/cia103-7?serverTimezone=Asia/Taipei"; // 資料庫 URL
        String User = "root"; // 資料庫使用者
        String Password = "1234"; // 資料庫密碼
        
        // 圖片檔案夾路徑
        String imageFolderPath = "src/main/resources/templates/front-end/lookitem/img/"; // 這裡指定資料夾路徑
        
        // 讀取資料夾並上傳所有圖片
        try (Connection connection = DriverManager.getConnection(URL, User, Password)) {
            File folder = new File(imageFolderPath);
            
            // 檢查資料夾是否存在
            if (!folder.exists() || !folder.isDirectory()) {
                System.out.println("指定的資料夾不存在或無效: " + imageFolderPath);
                return;
            }
            
            // 取得資料夾中的所有檔案
            File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));
            
            // 檢查資料夾是否有圖片檔案
            if (imageFiles == null || imageFiles.length == 0) {
                System.out.println("資料夾中沒有圖片檔案.");
                return;
            }

            // 準備 SQL 語句，將圖片儲存到 `item` 表格中的 `COVER_IMG` 欄位
            String sql = "UPDATE item SET COVER_IMG=? Where ITEM_ID=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // 遍歷所有圖片檔案並上傳
                int a = 11;
                for (File imageFile : imageFiles) {
                    try (FileInputStream inputStream = new FileInputStream(imageFile)) {
                        // 設定 BLOB 資料
                        statement.setBinaryStream(1, inputStream, (int) imageFile.length());
                        statement.setInt(2, a); // 假設 ITEM_ID 由 a 變數控制                      
                        if (a <= 32) {
                            a++; // 如果 a 還在 1~10 範圍內，繼續遞增                
                        }
                        
                        // 執行 SQL 語句
                        int rowsInserted = statement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("圖片已成功匯入資料庫: " + imageFile.getName());
                        }
                    } catch (IOException e) {
                        System.out.println("讀取圖片檔案出錯: " + imageFile.getName() + ", 錯誤訊息: " + e.getMessage());
                    }
                }
            }
            
        } catch (SQLException e) {
            System.out.println("資料庫連接錯誤: " + e.getMessage());
            e.printStackTrace(); // 顯示更詳細的錯誤信息
        }
    }
}
