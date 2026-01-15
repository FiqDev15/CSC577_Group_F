package com.edusummarize.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Utility class for processing and extracting text from various file formats
 */
public class FileProcessor {
    
    /**
     * Extracts text content from a PDF file
     * @param inputStream The input stream of the PDF file
     * @return Extracted text content
     * @throws IOException if file cannot be read
     */
    public static String extractTextFromPDF(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            if (document.getNumberOfPages() == 0) {
                throw new IOException("PDF file has no pages");
            }
            
            PDFTextStripper stripper = new PDFTextStripper();
            
            // Extract text from all pages
            stripper.setStartPage(1);
            stripper.setEndPage(document.getNumberOfPages());
            
            // Preserve line breaks and formatting
            stripper.setSortByPosition(true);
            stripper.setLineSeparator("\n");
            stripper.setParagraphStart("\n");
            stripper.setPageEnd("\n\n");
            
            String text = stripper.getText(document);
            
            // Fix common encoding issues
            text = fixEncodingIssues(text);
            
            // Clean up the text
            text = text.replaceAll("\\s+", " "); // Multiple spaces to single space
            text = text.replaceAll("\\n\\s*\\n", "\n"); // Multiple newlines to single
            
            return text.trim();
        } catch (org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException e) {
            throw new IOException("This PDF is password-protected and cannot be processed. Please provide an unencrypted version.", e);
        } catch (Exception e) {
            throw new IOException("Error extracting text from PDF: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extracts text content from a TXT file
     * @param inputStream The input stream of the text file
     * @return Extracted text content
     * @throws IOException if file cannot be read
     */
    public static String extractTextFromTXT(InputStream inputStream) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line).append("\n");
            }
        }
        return textBuilder.toString().trim();
    }
    
    /**
     * Cleans and preprocesses text for better summarization
     * @param text The raw text to clean
     * @return Cleaned text
     */
    public static String cleanText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        // Remove multiple spaces
        text = text.replaceAll("\\s+", " ");
        
        // Remove special characters but keep punctuation
        text = text.replaceAll("[^\\p{L}\\p{N}\\p{P}\\s]", "");
        
        // Trim
        return text.trim();
    }
    
    /**
     * Validates if the text has minimum content for summarization
     * @param text The text to validate
     * @return true if text is valid for summarization
     */
    public static boolean isValidTextForSummarization(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        // Check minimum word count (at least 50 words)
        String[] words = text.trim().split("\\s+");
        return words.length >= 50;
    }
    
    /**
     * Fix common encoding issues in extracted text
     * @param text The text with potential encoding issues
     * @return Cleaned text
     */
    private static String fixEncodingIssues(String text) {
        if (text == null) {
            return "";
        }
        
        try {
            // Convert from Latin1 (ISO-8859-1) to UTF-8 if needed
            byte[] bytes = text.getBytes("ISO-8859-1");
            text = new String(bytes, "UTF-8");
        } catch (Exception e) {
            // If conversion fails, continue with original text
        }
        
        // Remove Unicode replacement characters
        text = text.replace("\uFFFD", "");
        
        // Fix smart quotes to regular quotes
        text = text.replace("\u2018", "'"); // Left single quote
        text = text.replace("\u2019", "'"); // Right single quote
        text = text.replace("\u201C", "\""); // Left double quote
        text = text.replace("\u201D", "\""); // Right double quote
        
        // Fix dashes
        text = text.replace("\u2013", "-"); // En dash
        text = text.replace("\u2014", "-"); // Em dash
        
        // Fix other special characters
        text = text.replace("\u2022", "*"); // Bullet
        text = text.replace("\u2026", "..."); // Ellipsis
        text = text.replace("\u00A0", " "); // Non-breaking space
        
        // Remove any remaining problematic characters (keep only ASCII + letters + common punctuation)
        text = text.replaceAll("[^\\x20-\\x7E\\p{L}\\p{N}]", "");
        
        // Clean up double spaces
        text = text.replaceAll("\\s+", " ");
        
        return text;
    }
}
