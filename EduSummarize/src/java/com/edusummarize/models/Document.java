package com.edusummarize.models;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Document Model Class - Matches Class Diagram
 * 
 * CLASS DIAGRAM ATTRIBUTES:
 * - documentID: int
 * - userID: int
 * - fileName: String
 * - fileType: String
 * - fileSize: double
 * - uploadDate: Date
 * 
 * CLASS DIAGRAM OPERATIONS:
 * - uploadFile(): boolean
 * - extractText(): String
 * - validateFileType(): boolean
 * - validateFileSize(): boolean
 * - deleteDocument(): boolean
 */
public class Document {
    // Attributes (from class diagram)
    private int documentID;
    private int userID;
    private String fileName;
    private String fileType;        // PDF, TXT, DOCX
    private double fileSize;        // in MB
    private Timestamp uploadDate;
    
    // Additional attributes for content
    private String extractedText;
    private int originalLength;
    
    // Maximum file size (10 MB)
    private static final double MAX_FILE_SIZE_MB = 10.0;
    private static final String[] ALLOWED_FILE_TYPES = {"pdf", "txt"};
    
    // Constructors
    public Document() {
        this.uploadDate = new Timestamp(System.currentTimeMillis());
    }
    
    public Document(int documentID, int userID, String fileName, String fileType) {
        this();
        this.documentID = documentID;
        this.userID = userID;
        this.fileName = fileName;
        this.fileType = fileType;
    }
    
    // CLASS DIAGRAM OPERATIONS
    
    /**
     * Operation: uploadFile()
     * Validates and prepares file for upload
     * @return true if file is valid and ready to upload
     */
    public boolean uploadFile() {
        // Validate file before upload
        if (fileName == null || fileName.trim().isEmpty()) {
            System.err.println("File name is required");
            return false;
        }
        
        if (!validateFileType()) {
            System.err.println("Invalid file type: " + fileType);
            return false;
        }
        
        if (!validateFileSize()) {
            System.err.println("File size exceeds limit: " + fileSize + " MB");
            return false;
        }
        
        return true;
    }
    
    /**
     * Operation: extractText()
     * Extracts text content from the document
     * @return extracted text content
     */
    public String extractText() {
        // This is a placeholder - actual extraction done by FileProcessor
        // In a proper implementation, this would call FileProcessor
        return this.extractedText;
    }
    
    /**
     * Operation: validateFileType()
     * Checks if file type is supported (PDF, TXT)
     * @return true if file type is valid
     */
    public boolean validateFileType() {
        if (fileType == null || fileType.trim().isEmpty()) {
            return false;
        }
        
        String type = fileType.toLowerCase().trim();
        for (String allowedType : ALLOWED_FILE_TYPES) {
            if (type.equals(allowedType)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Operation: validateFileSize()
     * Checks if file size is within allowed limit (max 10 MB)
     * @return true if file size is valid
     */
    public boolean validateFileSize() {
        if (fileSize <= 0) {
            return false;
        }
        
        return fileSize <= MAX_FILE_SIZE_MB;
    }
    
    /**
     * Operation: deleteDocument()
     * Marks document for deletion
     * @return true if document can be deleted
     */
    public boolean deleteDocument() {
        // Check if document exists
        if (documentID <= 0) {
            return false;
        }
        
        // In actual implementation, this would delete from database
        System.out.println("Document " + documentID + " marked for deletion");
        return true;
    }
    
    /**
     * Additional helper method: Get file extension from filename
     */
    public String getFileExtension() {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
    
    /**
     * Additional helper method: Get file size in readable format
     */
    public String getFileSizeFormatted() {
        if (fileSize < 1) {
            return String.format("%.2f KB", fileSize * 1024);
        }
        return String.format("%.2f MB", fileSize);
    }
    
    /**
     * Additional helper method: Check if file is PDF
     */
    public boolean isPDF() {
        return "pdf".equalsIgnoreCase(fileType);
    }
    
    /**
     * Additional helper method: Check if file is text
     */
    public boolean isTextFile() {
        return "txt".equalsIgnoreCase(fileType);
    }
    
    // Getters and Setters
    
    public int getDocumentID() {
        return documentID;
    }
    
    public void setDocumentID(int documentID) {
        this.documentID = documentID;
    }
    
    public int getUserID() {
        return userID;
    }
    
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
        // Auto-detect file type from extension
        if (fileName != null && fileName.contains(".")) {
            this.fileType = getFileExtension();
        }
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public double getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }
    
    public Timestamp getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    public String getExtractedText() {
        return extractedText;
    }
    
    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
        this.originalLength = extractedText != null ? extractedText.length() : 0;
    }
    
    public int getOriginalLength() {
        return originalLength;
    }
    
    public void setOriginalLength(int originalLength) {
        this.originalLength = originalLength;
    }
    
    @Override
    public String toString() {
        return "Document{" +
                "documentID=" + documentID +
                ", userID=" + userID +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize + " MB" +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
