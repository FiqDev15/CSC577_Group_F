package com.edusummarize.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import org.json.JSONArray;
import org.json.JSONObject;

public class AIService {
    
    // CLASS DIAGRAM ATTRIBUTES
    // SECURITY: API key should be set as environment variable HUGGINGFACE_API_KEY
    // Never commit actual API keys to version control
    private static final String apiKey = System.getenv("HUGGINGFACE_API_KEY");  // Hugging Face API key from environment
    private static final int requestLimit = 1000;  // Maximum requests per hour
    
    // Legacy constant for backward compatibility
    private static final String HF_API_KEY = apiKey;
    
    // AI Model - Using BART (Bidirectional and Auto-Regressive Transformers) by Facebook
    private static final String HF_API_ENDPOINT = "https://router.huggingface.co/hf-inference/models/facebook/bart-large-cnn";
   
    private static final boolean USE_DEMO_MODE = false;
    
 
    static {
        try {
            
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };
         
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            
            // Create all-trusting host name verifier
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Generates a summary of the provided text using AI
     * Automatically determines optimal length based on input
     * @param text The text to summarize
     * @return The generated summary
     */
    public String generateSummary(String text) {
        // Validate input text
        if (text == null || text.trim().isEmpty()) {
            return "Error: No text provided for summarization.";
        }
        
        // Check minimum length (at least 200 characters)
        if (text.trim().length() < 200) {
            return "Error: Text is too short for summarization. Please provide at least 200 characters.";
        }
        
        if (USE_DEMO_MODE) {
            return generateDemoSummary(text);
        }
        
        try {
            // Clean and prepare text
            String cleanedText = cleanText(text);
            
            // Adjust text length based on input size for better summaries
            int maxLength = 4000;
            if (cleanedText.length() > maxLength) {
                cleanedText = cleanedText.substring(0, maxLength);
            }
            
            // Try API call with retry on timeout
            String summary = null;
            int maxRetries = 2;
            
            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                try {
                    summary = callAIAPI(cleanedText);
                    break; // Success, exit retry loop
                } catch (java.net.SocketTimeoutException e) {
                    if (attempt == maxRetries) {
                        // Last attempt failed, throw error
                        System.err.println("API timeout after " + maxRetries + " attempts.");
                        throw new Exception("Request timed out. Please check your connection.");
                    }
                    System.err.println("API timeout on attempt " + attempt + ". Retrying...");
                    Thread.sleep(1000); // Wait 1 second before retry
                }
            }
            
            // CRITICAL: Remove any hallucinated content before returning
            if (summary != null && !summary.isEmpty()) {
                summary = removeHallucinatedContent(summary);
            }
            
            return summary;
        } catch (Exception e) {
            e.printStackTrace();
            // Return error message instead of fallback
            System.err.println("API error: " + e.getMessage());
            return "Error: Summarization failed - " + e.getMessage();
        }
    }
    
    /**
     * Clean and prepare text for better summarization
     */
    private String cleanText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }
        
        String cleaned = text.trim();
        
        // Remove null bytes and control characters that can break API
        cleaned = cleaned.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");
        
        // Normalize whitespace (replace tabs, newlines with spaces)
        cleaned = cleaned.replaceAll("[\\t\\n\\r]+", " ");
        
        // Remove excessive whitespace
        cleaned = cleaned.replaceAll("\\s+", " ");
        
        // Remove special unicode characters that might cause issues
        cleaned = cleaned.replaceAll("[^\\p{L}\\p{N}\\p{P}\\s]", "");
        
        // Fix spacing around punctuation
        cleaned = cleaned.replaceAll("\\s+([.,!?;:])", "$1");
        cleaned = cleaned.replaceAll("([.,!?;:])([^\\s])", "$1 $2");
        
        // Remove multiple consecutive punctuation
        cleaned = cleaned.replaceAll("([.,!?;:]){2,}", "$1");
        
        return cleaned.trim();
    }
    
    /**
     * Remove hallucinated content that AI models sometimes add
     * Filters out common phrases from training data that shouldn't appear in summaries
     */
    private String removeHallucinatedContent(String summary) {
        if (summary == null || summary.trim().isEmpty()) {
            return summary;
        }
        
        // List of common hallucinated phrases from BART model
        String[] hallucinatedPhrases = {
            "For confidential support",
            "call the Samaritans",
            "National Suicide Prevention",
            "suicide prevention",
            "suicidepreventionlifeline.org",
            "Samaritans on",
            "08457 90 90 90",
            "1-800-273-8255",
            "0800 555 111",
            "click here for details",
            "visit a local",
            "www.samaritans.org",
            "In the U.S. call",
            "call the helpline",
            "visitÂ the Samaritans",
            "or visitÂ",
            "orÂ click here"
        };
        
        String cleaned = summary;
        
        // Remove sentences containing these phrases
        for (String phrase : hallucinatedPhrases) {
            // Case-insensitive matching
            String pattern = "(?i)[^.!?]*" + java.util.regex.Pattern.quote(phrase) + "[^.!?]*[.!?]\\s*";
            cleaned = cleaned.replaceAll(pattern, "");
        }
        
        // Also remove any standalone URLs that don't belong
        cleaned = cleaned.replaceAll("\\s*https?://[^\\s]+\\s*", " ");
        cleaned = cleaned.replaceAll("\\s*www\\.[^\\s]+\\s*", " ");
        
        // Clean up any remaining artifacts
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        
        // Remove trailing incomplete sentences (safety check)
        if (cleaned.length() > 0 && !cleaned.matches(".*[.!?]\\s*$")) {
            int lastPunctuation = Math.max(
                Math.max(cleaned.lastIndexOf('.'), cleaned.lastIndexOf('!')),
                cleaned.lastIndexOf('?')
            );
            if (lastPunctuation > 0) {
                cleaned = cleaned.substring(0, lastPunctuation + 1).trim();
            }
        }
        
        return cleaned.trim();
    }
    
    /**
     * Extracts key points from the text
     * @param text The text to analyze
     * @return List of key points
     */
    public List<String> extractKeyPoints(String text) {
        // Validate text length (must be at least 200 characters like summary)
        if (text == null || text.trim().isEmpty() || text.trim().length() < 200) {
            List<String> error = new ArrayList<>();
            error.add("Text too short to extract key points. Please provide at least 200 characters.");
            return error;
        }
        
        // For key points, we'll use a simple extraction from sentences
        return generateDemoKeyPoints(text);
    }
    
    /**
     * Creates a summary prompt based on desired length
     */
    private String createSummaryPrompt(String text, String length) {
        String lengthInstruction = "";
        switch (length.toLowerCase()) {
            case "short":
                lengthInstruction = "in 2-3 sentences";
                break;
            case "long":
                lengthInstruction = "in 10-12 sentences with detailed explanations";
                break;
            case "medium":
            default:
                lengthInstruction = "in 5-7 sentences";
                break;
        }
        
        return String.format("Summarize the following educational content %s. Focus on the main concepts and key takeaways:\n\n%s", 
                           lengthInstruction, text);
    }
    
    /**
     * Calls the Hugging Face API for text summarization with automatic length optimization
     * THIS FUNCTION SENDS DATA TO AI - Makes HTTP POST request to Hugging Face API endpoint
     */
    private String callAIAPI(String text) throws Exception {
        URL url = new URL(HF_API_ENDPOINT);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + HF_API_KEY);
        conn.setDoOutput(true);
        conn.setConnectTimeout(45000); // 45 seconds - increased for longer documents
        conn.setReadTimeout(60000);    // 60 seconds - prevent timeout on slow API responses
        
        // Create request body for Hugging Face
        JSONObject requestBody = new JSONObject();
        
        // Ensure text is properly cleaned and has minimum length
        String inputText = text.trim();
        
        // Minimum length check
        if (inputText.length() < 200) {
            throw new Exception("Text is too short for AI summarization. Please provide at least 200 characters.");
        }
        
        // Check for actual content (not just whitespace/punctuation)
        String contentCheck = inputText.replaceAll("[^\\p{L}\\p{N}]", "");
        if (contentCheck.length() < 100) {
            throw new Exception("Text does not contain enough readable content. Please provide meaningful text.");
        }
        
        // BART model input length limit
        int maxInputLength = 4000; // BART optimal length for accurate summarization
        
        if (inputText.length() > maxInputLength) {
            inputText = inputText.substring(0, maxInputLength);
        }
        
        requestBody.put("inputs", inputText);
        
        // Automatic smart parameters based on input length
        int inputLength = inputText.length();
        int maxLength, minLength;
        
        // Dynamic length calculation for SHORTER, more concise summaries
        // Aim for 15-25% of original text length
        if (inputLength < 500) {
            // Short text: brief summary
            maxLength = Math.min(100, (int)(inputLength * 0.35));
            minLength = Math.max(40, (int)(inputLength * 0.20));
        } else if (inputLength < 1500) {
            // Medium text: concise summary
            maxLength = Math.min(150, (int)(inputLength * 0.25));
            minLength = Math.max(60, (int)(inputLength * 0.15));
        } else if (inputLength < 3000) {
            // Long text: very concise summary
            maxLength = Math.min(180, (int)(inputLength * 0.20));
            minLength = Math.max(80, (int)(inputLength * 0.12));
        } else {
            // Very long text: extremely concise
            maxLength = 200;
            minLength = 100;
        }
        
        // Ensure reasonable bounds
        if (maxLength < minLength + 20) {
            maxLength = minLength + 30;
        }
        
        JSONObject parameters = new JSONObject();
        parameters.put("max_length", maxLength);
        parameters.put("min_length", minLength);
        parameters.put("do_sample", false);
        parameters.put("early_stopping", true);
        parameters.put("length_penalty", 1.5);  // Reduced for shorter output
        parameters.put("no_repeat_ngram_size", 3);  // Prevents repetition
        parameters.put("num_beams", 4);  // Better quality with beam search
        requestBody.put("parameters", parameters);
        
        // Send request
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // Read response
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            // Parse Hugging Face response - it returns an array with summary_text
            String responseStr = response.toString();
            
            // Check if response is an array
            if (responseStr.trim().startsWith("[")) {
                JSONArray jsonArray = new JSONArray(responseStr);
                if (jsonArray.length() > 0) {
                    JSONObject firstResult = jsonArray.getJSONObject(0);
                    return firstResult.getString("summary_text");
                }
            } else {
                // Sometimes returns object directly
                JSONObject jsonResponse = new JSONObject(responseStr);
                if (jsonResponse.has("summary_text")) {
                    return jsonResponse.getString("summary_text");
                }
            }
            
            return "Unable to parse summary from API response.";
        } else if (responseCode == 503) {
            return "The AI model is currently loading. Please try again in a few moments.";
        } else {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorResponse.append(line);
            }
            throw new Exception("API request failed (Code " + responseCode + "): " + errorResponse.toString());
        }
    }
    
    /**
     * Parses key points from AI response
     */
    private List<String> parseKeyPoints(String response) {
        List<String> keyPoints = new ArrayList<>();
        String[] lines = response.split("\n");
        
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                // Remove numbering if present
                line = line.replaceFirst("^\\d+\\.\\s*", "");
                line = line.replaceFirst("^[-*]\\s*", "");
                if (!line.isEmpty()) {
                    keyPoints.add(line);
                }
            }
        }
        
        return keyPoints;
    }
    
    /**
     * Demo mode summary generator (for testing without API)
     */
    private String generateDemoSummary(String text) {
        // Get sentences from text
        String[] sentences = text.split("(?<=[.!?])\\s+");
        
        if (sentences.length == 0) {
            return "This document discusses educational content and provides important information for students.";
        }
        
        // Automatically determine optimal number of sentences based on input length
        int numSentences;
        if (text.length() < 500) {
            numSentences = Math.min(3, sentences.length);
        } else if (text.length() < 1500) {
            numSentences = Math.min(5, sentences.length);
        } else {
            numSentences = Math.min(7, sentences.length);
        }
        
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < numSentences; i++) {
            if (i > 0) summary.append(" ");
            String sentence = sentences[i].trim();
            summary.append(sentence);
            if (!sentence.matches(".*[.!?]$")) {
                summary.append(".");
            }
        }
        
        return summary.toString();
    }
    
    /**
     * ENHANCED key points generator with TF-IDF-like scoring
     * Uses smart algorithm to extract most important sentences
     */
    private List<String> generateDemoKeyPoints(String text) {
        List<String> keyPoints = new ArrayList<>();
        
        // Split by sentence endings with better regex
        String[] sentences = text.split("(?<=[.!?])\\s+");
        
        if (sentences.length == 0) {
            keyPoints.add("Unable to extract meaningful key points from this content.");
            return keyPoints;
        }
        
        // Calculate how many key points to extract (5-10 based on content length)
        int targetCount = Math.min(10, Math.max(5, sentences.length / 10));
        
        // Build word frequency map for TF-IDF-like scoring
        Map<String, Integer> wordFrequency = buildWordFrequency(text);
        
        // Score sentences by multiple factors
        List<ScoredSentence> scoredSentences = new ArrayList<>();
        
        for (int i = 0; i < sentences.length; i++) {
            String sentence = sentences[i].trim();
            
            // Skip very short or very long sentences
            if (sentence.length() < 30 || sentence.length() > 300) {
                continue;
            }
            
            double score = 0.0;
            String lowerSentence = sentence.toLowerCase();
            
            // 1. TF-IDF-like scoring: sentences with unique important words score higher
            String[] words = lowerSentence.split("\\s+");
            for (String word : words) {
                word = word.replaceAll("[^a-z0-9]", "");
                if (word.length() > 3 && wordFrequency.containsKey(word)) {
                    // Rare words (low frequency) are more important
                    int freq = wordFrequency.get(word);
                    if (freq == 1) score += 3.0;  // Unique words
                    else if (freq == 2) score += 2.0;  // Rare words
                    else if (freq <= 4) score += 1.0;  // Less common words
                }
            }
            
            // 2. Educational keyword boost
            if (lowerSentence.matches(".*(important|key|main|primary|significant|essential|fundamental|critical|conclude|therefore|thus|because|result).*")) {
                score += 5.0;
            }
            
            // 3. Contains numbers or data (often key facts)
            if (lowerSentence.matches(".*\\d+.*")) {
                score += 2.0;
            }
            
            // 4. Position-based scoring (early sentences often more important)
            if (i < 3) score += 4.0;
            else if (i < 5) score += 2.0;
            
            // 5. Contains action verbs (indicates main concepts)
            if (lowerSentence.matches(".*(demonstrate|show|indicate|suggest|prove|explain|define|describe|illustrate|reveal).*")) {
                score += 3.0;
            }
            
            // 6. Optimal length bonus (70-150 chars is ideal for key points)
            if (sentence.length() >= 70 && sentence.length() <= 150) {
                score += 3.0;
            } else if (sentence.length() >= 50 && sentence.length() < 200) {
                score += 1.0;
            }
            
            // 7. Contains colon or dash (often introduces key info)
            if (sentence.contains(":") || sentence.contains("—") || sentence.contains(" - ")) {
                score += 2.0;
            }
            
            // 8. Avoid questions and parenthetical remarks
            if (sentence.contains("?")) {
                score -= 2.0;
            }
            if (sentence.contains("(") && sentence.contains(")")) {
                score -= 1.0;
            }
            
            scoredSentences.add(new ScoredSentence(sentence, score, i));
        }
        
        // Sort by score (highest first)
        scoredSentences.sort((a, b) -> Double.compare(b.score, a.score));
        
        // Select diverse key points (avoid redundancy)
        Set<String> usedWords = new HashSet<>();
        int pointsAdded = 0;
        
        for (ScoredSentence scored : scoredSentences) {
            if (pointsAdded >= targetCount) break;
            
            // Check for diversity - avoid sentences with too many repeated key words
            String[] words = scored.sentence.toLowerCase().split("\\s+");
            Set<String> sentenceWords = new HashSet<>();
            for (String word : words) {
                word = word.replaceAll("[^a-z0-9]", "");
                if (word.length() > 4) {
                    sentenceWords.add(word);
                }
            }
            
            // Calculate overlap with already selected points
            int overlap = 0;
            for (String word : sentenceWords) {
                if (usedWords.contains(word)) {
                    overlap++;
                }
            }
            
            // Skip if too much overlap (more than 40% of words already used)
            if (pointsAdded > 0 && overlap > sentenceWords.size() * 0.4) {
                continue;
            }
            
            // Add this key point
            String point = scored.sentence;
            if (!point.matches(".*[.!?]$")) {
                point += ".";
            }
            
            keyPoints.add(point);
            usedWords.addAll(sentenceWords);
            pointsAdded++;
        }
        
        // Fallback if no good points found
        if (keyPoints.isEmpty()) {
            keyPoints.add("Unable to extract meaningful key points from this content. Consider providing more detailed text.");
        }
        
        return keyPoints;
    }
    
    /**
     * Build word frequency map for TF-IDF-like scoring
     */
    private Map<String, Integer> buildWordFrequency(String text) {
        Map<String, Integer> frequency = new HashMap<>();
        String[] words = text.toLowerCase().split("\\s+");
        
        for (String word : words) {
            word = word.replaceAll("[^a-z0-9]", "");
            if (word.length() > 3) {  // Ignore short words
                frequency.put(word, frequency.getOrDefault(word, 0) + 1);
            }
        }
        
        return frequency;
    }
    
    /**
     * Helper class to score sentences with position tracking
     */
    private static class ScoredSentence {
        String sentence;
        double score;
        int position;
        
        ScoredSentence(String sentence, double score, int position) {
            this.sentence = sentence;
            this.score = score;
        }
    }

    /**
     * CLASS DIAGRAM OPERATION: summarizeAndExtract(text: String): String
     * Main method that combines summarization and key point extraction
     * This is the primary interface method called by SummaryResult
     * 
     * @param text Input text to summarize and extract from
     * @return Combined summary with key points
     */
    public String summarizeAndExtract(String text) {
        if (!validateTextLength(text)) {
            return "Error: Text too short for summarization (minimum 200 characters required)";
        }
        
        try {
            // Generate summary
            String summary = generateSummary(text);
            
            // Extract key points
            List<String> keyPoints = extractKeyPoints(text);
            
            // Combine into single output
            StringBuilder result = new StringBuilder();
            result.append("SUMMARY:\n");
            result.append(summary);
            result.append("\n\nKEY POINTS:\n");
            
            for (int i = 0; i < keyPoints.size(); i++) {
                result.append((i + 1)).append(". ").append(keyPoints.get(i)).append("\n");
            }
            
            return result.toString();
            
        } catch (Exception e) {
            System.err.println("Error in summarizeAndExtract: " + e.getMessage());
            return "Error generating summary: " + e.getMessage();
        }
    }
    
    /**
     * CLASS DIAGRAM OPERATION: validateTextLength(text: String): boolean
     * Validates that text meets minimum length requirements for API
     * 
     * @param text Text to validate
     * @return true if text length is valid
     */
    public boolean validateTextLength(String text) {
        if (text == null || text.trim().isEmpty()) {
            System.err.println("Validation failed: Text is null or empty");
            return false;
        }
        
        int length = text.trim().length();
        
        // Minimum length for meaningful summarization
        if (length < 200) {
            System.err.println("Validation failed: Text too short (" + length + " chars). Minimum: 200 characters");
            return false;
        }
        
        // Maximum length check (API limitations)
        if (length > 500000) {
            System.err.println("Validation failed: Text too long (" + length + " chars). Maximum: 500,000 characters");
            return false;
        }
        
        System.out.println("Text validation passed: " + length + " characters");
        return true;
    }
    
    /**
     * CLASS DIAGRAM OPERATION: optimizeSummaryLength(text: String): int
     * Calculates optimal summary length based on input text length
     * Follows 15-25% compression ratio guideline
     * 
     * @param text Input text
     * @return Optimal maximum length for summary in tokens/words
     */
    public int optimizeSummaryLength(String text) {
        if (text == null || text.isEmpty()) {
            return 100;  // Default minimum
        }
        
        int inputLength = text.length();
        int wordCount = text.split("\\s+").length;
        
        // Calculate optimal summary length (20% of original is ideal)
        int optimalChars = (int) (inputLength * 0.20);
        int optimalWords = (int) (wordCount * 0.20);
        
        // Set reasonable bounds
        int minWords = Math.max(50, (int) (wordCount * 0.15));   // Minimum 15%
        int maxWords = Math.min(500, (int) (wordCount * 0.30));  // Maximum 30%
        
        // Ensure it's within reasonable bounds
        int targetWords = Math.max(minWords, Math.min(maxWords, optimalWords));
        
        System.out.println("Summary length optimization:");
        System.out.println("  Input: " + wordCount + " words");
        System.out.println("  Optimal: " + targetWords + " words (" + 
                          String.format("%.1f%%", (double) targetWords / wordCount * 100) + ")");
        
        return targetWords;
    }
    
    /**
     * CLASS DIAGRAM OPERATION: checkAPIStatus(): boolean
     * Verifies that the Hugging Face API is accessible and functioning
     * 
     * @return true if API is accessible
     */
    public boolean checkAPIStatus() {
        try {
            System.out.println("Checking API status...");
            System.out.println("API Endpoint: " + HF_API_ENDPOINT);
            System.out.println("Model: BART (facebook/bart-large-cnn)");
            
            // Try a minimal test request
            URL url = new URL(HF_API_ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + HF_API_KEY);
            conn.setConnectTimeout(5000);  // 5 second timeout
            conn.setReadTimeout(5000);
            
            int responseCode = conn.getResponseCode();
            
            conn.disconnect();
            
            if (responseCode == 200 || responseCode == 405) {
                // 405 (Method Not Allowed) is OK - means endpoint exists but doesn't support GET
                System.out.println("✓ API Status: ONLINE (Response code: " + responseCode + ")");
                return true;
            } else {
                System.err.println("✗ API Status: Unexpected response code: " + responseCode);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("✗ API Status: ERROR - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Additional helper: Get API configuration info
     */
    public String getAPIInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== AIService Configuration ===\n");
        info.append("Model: BART (facebook/bart-large-cnn)\n");
        info.append("Endpoint: ").append(HF_API_ENDPOINT).append("\n");
        info.append("API Key: ").append(apiKey.substring(0, 10)).append("...").append("\n");
        info.append("Request Limit: ").append(requestLimit).append(" per hour\n");
        info.append("Demo Mode: ").append(USE_DEMO_MODE ? "ENABLED" : "DISABLED").append("\n");
        info.append("Status: ").append(checkAPIStatus() ? "ONLINE" : "OFFLINE").append("\n");
        info.append("==============================");
        return info.toString();
    }
}
