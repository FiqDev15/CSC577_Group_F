package com.edusummarize.servlets;

import com.edusummarize.utils.AIService;
import com.edusummarize.utils.FileProcessor;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * SummarizeServlet - Handles Document Processing and Summarization
 * 
 * CLASS DIAGRAM RELATIONSHIPS:
 * ============================
 * 
 * 1. User → Document (Directed Association 1 to 1..*)
 *    - Each User can upload and process multiple Documents
 *    - Multiplicity: 1 User to 1..* Documents
 *    - Direction: User navigates to their Documents
 * 
 * Document Attributes:
 * - documentID: int (represented by summary ID in database)
 * - userID: String (foreign key to User)
 * - fileName: String (original uploaded filename)
 * - fileType: String (PDF, TXT)
 * - fileSize: double (file size in bytes)
 * - uploadDate: Date (created_at timestamp)
 * 
 * Document Operations:
 * - uploadFile(): Handles PDF/TXT file upload
 * - extractText(): Extracts text content from document
 * 
 * 2. Document → SummaryResult (Directed Association 1 to 1)
 *    - Each Document produces exactly one SummaryResult
 *    - Multiplicity: 1 Document to 1 SummaryResult
 *    - Direction: Document navigates to its SummaryResult
 * 
 * SummaryResult Attributes:
 * - summaryID: int (primary key in summaries table)
 * - documentID: int (foreign key to Document)
 * - summaryText: String (AI-generated summary)
 * - keywords: List<String> (extracted key points)
 * - generatedDate: Date (created_at timestamp)
 * 
 * SummaryResult Operations:
 * - generateSummary(): Creates summary using AIService
 * - displayOutput(): Shows summary and key points to user
 * - downloadOutput(): Allows user to download summary as text file
 * 
 * 3. SummaryResult → AIService (Dependency - dashed arrow)
 *    - SummaryResult depends on AIService to generate summaries
 *    - This is a «uses» relationship (dependency)
 *    - Direction: SummaryResult uses AIService methods
 *    - AIService is called temporarily to perform summarization
 * 
 * PDF LIMIT ENFORCEMENT:
 * - Checks user's subscription status (is_premium)
 * - Free users: Enforces 100 PDF limit (pdf_count <= 100)
 * - Premium users: Unlimited PDF processing
 * - Increments pdf_count after successful PDF summarization
 */
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 15,       // 15MB (increased to handle 10MB files + overhead)
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class SummarizeServlet extends HttpServlet {

    /**
     * Handles the HTTP POST request for file upload and summarization
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        
        // Get user session info for PDF limit checking
        javax.servlet.http.HttpSession session = request.getSession();
        Boolean isPremium = (Boolean) session.getAttribute("isPremium");
        Integer pdfCount = (Integer) session.getAttribute("pdfCount");
        
        if (isPremium == null) isPremium = false;
        if (pdfCount == null) pdfCount = 0;
        
        try {
            String extractedText = "";
            String fileName = "Text Input";
            String fileType = "TEXT";
            String courseName = request.getParameter("courseName"); // Get course name from form
            boolean isPdfUpload = false;
            
            // Check if user submitted text directly
            String textInput = request.getParameter("textContent");
            
            if (textInput != null && !textInput.trim().isEmpty()) {
                // User submitted text directly
                extractedText = textInput.trim();
                fileName = "Direct Text Input";
                fileType = "TEXT";
            } else {
                // CLASS DIAGRAM: User → Document (Directed Association, 1 to 1..*)
                // User uploaded a file
                Part filePart = request.getPart("file");
                
                // Validate file
                if (filePart == null || filePart.getSize() == 0) {
                    request.setAttribute("errorMessage", "Please either paste text in the text box or upload a file.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                
                fileName = getFileName(filePart);
                
                // Get file type
                fileType = getFileExtension(fileName);
                if (!fileType.equalsIgnoreCase("pdf") && !fileType.equalsIgnoreCase("txt")) {
                    request.setAttribute("errorMessage", "Invalid file type. Please upload a PDF or TXT file.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                
                // Check PDF limit for free users
                if (fileType.equalsIgnoreCase("pdf")) {
                    isPdfUpload = true;
                    if (!isPremium && pdfCount >= 100) {
                        request.setAttribute("errorMessage", 
                            "You've reached your free plan limit of 100 PDF summarizations. " +
                            "<a href='pricing.jsp' style='color: #000; text-decoration: underline;'>Upgrade to Premium</a> for unlimited access, " +
                            "or use the text input box to paste your content directly.");
                        request.getRequestDispatcher("error.jsp").forward(request, response);
                        return;
                    }
                }
                
                // Read file content
                InputStream fileContent = filePart.getInputStream();
                
                if (fileType.equalsIgnoreCase("pdf")) {
                    extractedText = FileProcessor.extractTextFromPDF(fileContent);
                } else {
                    extractedText = FileProcessor.extractTextFromTXT(fileContent);
                }
            }
            
            // Validate extracted text
            if (extractedText == null || extractedText.trim().isEmpty()) {
                request.setAttribute("errorMessage", "No text found. Please provide text content or upload a valid file.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Log extracted text length for debugging
            System.out.println("=== PDF/Text Extraction Debug ===");
            System.out.println("File Type: " + fileType);
            System.out.println("Extracted Text Length: " + extractedText.length() + " characters");
            System.out.println("First 200 characters: " + extractedText.substring(0, Math.min(200, extractedText.length())));
            System.out.println("================================");
            
            // CLASS DIAGRAM: Document → SummaryResult (Directed Association, 1 to 1)
            // Generate summary using AI (automatically optimized length)
            AIService aiService = new AIService();
            // CLASS DIAGRAM: SummaryResult → AIService (Dependency, <<uses>>)
            String summary = aiService.generateSummary(extractedText);
            List<String> keyPoints = aiService.extractKeyPoints(extractedText);
            
            // Validate that summary was successfully generated
            boolean summarySuccess = false;
            if (summary != null && !summary.trim().isEmpty() && 
                !summary.contains("Error") && !summary.contains("failed") &&
                summary.length() > 50) { // Minimum viable summary length
                summarySuccess = true;
            }
            
            // Increment PDF count for free users if this was a PDF upload
            if (isPdfUpload && !isPremium) {
                try {
                    Integer userId = (Integer) session.getAttribute("userId");
                    if (userId != null) {
                        com.edusummarize.utils.DatabaseUtil.incrementPdfCount(userId);
                        // Update session
                        session.setAttribute("pdfCount", pdfCount + 1);
                    }
                } catch (Exception dbError) {
                    System.err.println("Error updating PDF count: " + dbError.getMessage());
                    // Don't fail the request, just log the error
                }
            }
            
            // Save summary to database ONLY if successfully generated
            if (summarySuccess) {
                try {
                    Integer userId = (Integer) session.getAttribute("userId");
                    if (userId != null) {
                        Connection conn = com.edusummarize.utils.DatabaseUtil.getConnection();
                        String sql = "INSERT INTO summaries (user_id, file_name, file_type, course_name, original_length, summary_text, key_points, created_at) " +
                                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, userId);
                        stmt.setString(2, fileName);
                        stmt.setString(3, fileType.toUpperCase());
                        stmt.setString(4, courseName != null && !courseName.trim().isEmpty() ? courseName.trim() : null);
                        stmt.setInt(5, extractedText.length());
                        stmt.setString(6, summary);
                        
                        // Convert key points list to string
                        String keyPointsStr = keyPoints != null && !keyPoints.isEmpty() ? 
                            String.join("|||", keyPoints) : null;
                        stmt.setString(7, keyPointsStr);
                        
                        stmt.executeUpdate();
                        stmt.close();
                        conn.close();
                        System.out.println("Summary saved to database for user " + userId + " (Course: " + courseName + ")");
                    }
                } catch (Exception dbError) {
                    System.err.println("Error saving summary: " + dbError.getMessage());
                    dbError.printStackTrace();
                    // Don't fail the request, just log
                }
            } else {
                System.out.println("Summary not saved to database - generation failed or insufficient content");
            }
            
            // Calculate processing time
            long endTime = System.currentTimeMillis();
            double processingTime = (endTime - startTime) / 1000.0;
            
            // Set attributes for JSP
            request.setAttribute("fileName", fileName);
            request.setAttribute("fileType", fileType.toUpperCase());
            request.setAttribute("originalLength", extractedText.length());
            request.setAttribute("summary", summary);
            request.setAttribute("keyPoints", keyPoints);
            request.setAttribute("processingTime", String.format("%.2f", processingTime));
            
            // Forward to summary page
            request.getRequestDispatcher("summary.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Extracts the file name from the Part header
     */
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
    
    /**
     * Gets the file extension from filename
     */
    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Servlet for processing and summarizing educational content";
    }
}
