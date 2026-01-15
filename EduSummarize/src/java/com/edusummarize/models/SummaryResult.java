package com.edusummarize.models;

import com.edusummarize.utils.AIService;
import com.edusummarize.utils.DatabaseUtil;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SummaryResult Model Class - Matches Class Diagram
 * 
 * CLASS DIAGRAM: Document → SummaryResult (Directed Association, 1 to 1)
 * CLASS DIAGRAM: SummaryResult → AIService (Dependency, <<uses>>)
 * 
 * CLASS DIAGRAM ATTRIBUTES:
 * - summaryID: int
 * - documentID: int
 * - summaryText: String
 * - keywords: List<String>
 * - generatedDate: Date
 * 
 * CLASS DIAGRAM OPERATIONS:
 * - generateSummary(): boolean
 * - displayOutput(): String
 * - downloadOutput(): boolean
 * - saveSummary(): boolean
 * - exportToPDF(): boolean
 * - shareViaEmail(email: String): boolean
 */
public class SummaryResult {
    // Attributes (from class diagram)
    private int summaryID;
    private int documentID;
    private String summaryText;
    private List<String> keywords;
    private Timestamp generatedDate;
    
    // Additional attributes
    private String originalText;
    private int originalLength;
    private int summaryLength;
    private double compressionRatio;
    
    // Constructors
    public SummaryResult() {
        this.keywords = new ArrayList<>();
        this.generatedDate = Timestamp.valueOf(LocalDateTime.now());
    }
    
    public SummaryResult(int summaryID, int documentID) {
        this();
        this.summaryID = summaryID;
        this.documentID = documentID;
    }
    
    // CLASS DIAGRAM OPERATIONS
    
    /**
     * Operation: generateSummary()
     * Generates summary using AIService (DEPENDENCY relationship)
     * @return true if summary generated successfully
     */
    public boolean generateSummary() {
        if (originalText == null || originalText.trim().isEmpty()) {
            System.err.println("Original text is required to generate summary");
            return false;
        }
        
        if (originalText.length() < 200) {
            System.err.println("Text too short for summarization (minimum 200 characters)");
            return false;
        }
        
        try {
            // CLASS DIAGRAM: SummaryResult → AIService (Dependency, <<uses>>)
            // SummaryResult temporarily uses AIService to generate summary
            AIService aiService = new AIService();
            
            // Generate summary text
            this.summaryText = aiService.generateSummary(originalText);
            
            // Extract keywords/key points
            this.keywords = aiService.extractKeyPoints(originalText);
            
            // Calculate metrics
            this.originalLength = originalText.length();
            this.summaryLength = summaryText.length();
            this.compressionRatio = (double) summaryLength / originalLength * 100;
            this.generatedDate = Timestamp.valueOf(LocalDateTime.now());
            
            System.out.println("Summary generated successfully");
            System.out.println("Compression ratio: " + String.format("%.1f%%", compressionRatio));
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error generating summary: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Operation: displayOutput()
     * Formats and returns the summary output for display
     * @return formatted summary output
     */
    public String displayOutput() {
        StringBuilder output = new StringBuilder();
        
        output.append("========== SUMMARY RESULT ==========\n");
        output.append("Generated: ").append(generatedDate).append("\n");
        output.append("Original Length: ").append(originalLength).append(" characters\n");
        output.append("Summary Length: ").append(summaryLength).append(" characters\n");
        output.append("Compression: ").append(String.format("%.1f%%", compressionRatio)).append("\n");
        output.append("=====================================\n\n");
        
        output.append("SUMMARY:\n");
        output.append(summaryText).append("\n\n");
        
        if (keywords != null && !keywords.isEmpty()) {
            output.append("KEY POINTS:\n");
            for (int i = 0; i < keywords.size(); i++) {
                output.append((i + 1)).append(". ").append(keywords.get(i)).append("\n");
            }
        }
        
        output.append("\n=====================================");
        
        return output.toString();
    }
    
    /**
     * Operation: downloadOutput()
     * Saves summary to a text file for download
     * @return true if download file created successfully
     */
    public boolean downloadOutput() {
        String fileName = "summary_" + summaryID + "_" + System.currentTimeMillis() + ".txt";
        
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(displayOutput());
            System.out.println("Summary saved to: " + fileName);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error creating download file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Operation: saveSummary()
     * Saves summary to database
     * @return true if saved successfully
     */
    public boolean saveSummary() {
        if (summaryText == null || summaryText.trim().isEmpty()) {
            System.err.println("Cannot save empty summary");
            return false;
        }
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO summaries (user_id, file_name, file_type, " +
                        "original_length, summary_text, summary_length, created_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, documentID);  // Using documentID as user_id for now
            stmt.setString(2, "summary_" + documentID);
            stmt.setString(3, "text");
            stmt.setInt(4, originalLength);
            stmt.setString(5, summaryText);
            stmt.setString(6, String.valueOf(summaryLength));
            stmt.setTimestamp(7, generatedDate);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    this.summaryID = rs.getInt(1);
                }
                System.out.println("Summary saved to database with ID: " + summaryID);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error saving summary to database: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Operation: exportToPDF()
     * Exports summary as PDF file (placeholder implementation)
     * @return true if export successful
     */
    public boolean exportToPDF() {
        // Placeholder - would require PDF library like iText or Apache PDFBox
        System.out.println("Exporting summary to PDF...");
        System.out.println("Note: PDF export requires additional library implementation");
        
        // For now, save as text file
        return downloadOutput();
    }
    
    /**
     * Additional helper method: Calculate summary quality score
     */
    public double getQualityScore() {
        // Simple quality metric based on compression ratio and keyword count
        double compressionScore = Math.min(compressionRatio, 30) / 30.0;  // Ideal 15-30%
        double keywordScore = Math.min(keywords.size(), 10) / 10.0;       // Ideal 5-10 keywords
        
        return (compressionScore + keywordScore) / 2.0 * 100;
    }
    
    /**
     * Additional helper method: Get short preview of summary
     */
    public String getPreview(int maxLength) {
        if (summaryText == null) {
            return "";
        }
        
        if (summaryText.length() <= maxLength) {
            return summaryText;
        }
        
        return summaryText.substring(0, maxLength) + "...";
    }
    
    // Getters and Setters
    
    public int getSummaryID() {
        return summaryID;
    }
    
    public void setSummaryID(int summaryID) {
        this.summaryID = summaryID;
    }
    
    public int getDocumentID() {
        return documentID;
    }
    
    public void setDocumentID(int documentID) {
        this.documentID = documentID;
    }
    
    public String getSummaryText() {
        return summaryText;
    }
    
    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
        if (summaryText != null) {
            this.summaryLength = summaryText.length();
        }
    }
    
    public List<String> getKeywords() {
        return keywords;
    }
    
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    
    public Timestamp getGeneratedDate() {
        return generatedDate;
    }
    
    public void setGeneratedDate(Timestamp generatedDate) {
        this.generatedDate = generatedDate;
    }
    
    public String getOriginalText() {
        return originalText;
    }
    
    public void setOriginalText(String originalText) {
        this.originalText = originalText;
        if (originalText != null) {
            this.originalLength = originalText.length();
        }
    }
    
    public int getOriginalLength() {
        return originalLength;
    }
    
    public void setOriginalLength(int originalLength) {
        this.originalLength = originalLength;
    }
    
    public int getSummaryLength() {
        return summaryLength;
    }
    
    public void setSummaryLength(int summaryLength) {
        this.summaryLength = summaryLength;
    }
    
    public double getCompressionRatio() {
        return compressionRatio;
    }
    
    public void setCompressionRatio(double compressionRatio) {
        this.compressionRatio = compressionRatio;
    }
    
    @Override
    public String toString() {
        return "SummaryResult{" +
                "summaryID=" + summaryID +
                ", documentID=" + documentID +
                ", originalLength=" + originalLength +
                ", summaryLength=" + summaryLength +
                ", compression=" + String.format("%.1f%%", compressionRatio) +
                ", keywords=" + keywords.size() +
                ", generatedDate=" + generatedDate +
                '}';
    }
}
