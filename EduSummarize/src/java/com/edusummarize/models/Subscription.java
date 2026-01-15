package com.edusummarize.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Subscription Model Class - Matches Class Diagram
 * 
 * CLASS DIAGRAM ATTRIBUTES:
 * - subscriptionID: int
 * - userID: int
 * - planType: String
 * - startDate: Date
 * - endDate: Date
 * - paymentStatus: String
 * 
 * CLASS DIAGRAM OPERATIONS:
 * - activatePlan(): boolean
 * - cancelPlan(): boolean
 * - checkExpiration(): boolean
 * - renewSubscription(): boolean
 * - upgradeSubscription(): boolean
 * - calculateRemainingDays(): int
 */
public class Subscription {
    // Attributes (from class diagram)
    private int subscriptionID;
    private int userID;
    private String planType;            // "Free", "Premium"
    private Timestamp startDate;
    private Timestamp endDate;
    private String paymentStatus;       // "Active", "Expired", "Pending", "Cancelled"
    
    // Additional attributes
    private double monthlyPrice;
    private int pdfLimit;
    
    // Plan constants
    public static final String PLAN_FREE = "Free";
    public static final String PLAN_PREMIUM = "Premium";
    public static final double PREMIUM_PRICE = 50.0;  // RM50/month
    public static final int FREE_PDF_LIMIT = 100;
    
    // Constructors
    public Subscription() {
        this.planType = PLAN_FREE;
        this.paymentStatus = "Active";
        this.pdfLimit = FREE_PDF_LIMIT;
        this.monthlyPrice = 0.0;
    }
    
    public Subscription(int subscriptionID, int userID, String planType) {
        this();
        this.subscriptionID = subscriptionID;
        this.userID = userID;
        this.planType = planType;
        
        if (planType.equals(PLAN_PREMIUM)) {
            this.monthlyPrice = PREMIUM_PRICE;
            this.pdfLimit = Integer.MAX_VALUE;  // Unlimited
        }
    }
    
    // CLASS DIAGRAM OPERATIONS
    
    /**
     * Operation: activatePlan()
     * Activates or starts a subscription plan
     * @return true if activation successful
     */
    public boolean activatePlan() {
        if (planType == null || planType.trim().isEmpty()) {
            System.err.println("Plan type is required");
            return false;
        }
        
        // Set start date to now
        this.startDate = Timestamp.valueOf(LocalDateTime.now());
        
        // Set end date based on plan type
        if (planType.equals(PLAN_PREMIUM)) {
            // Premium plan is monthly subscription
            this.endDate = Timestamp.valueOf(LocalDateTime.now().plusMonths(1));
            this.paymentStatus = "Active";
            System.out.println("Premium plan activated for user " + userID);
        } else {
            // Free plan has no end date
            this.endDate = null;
            this.paymentStatus = "Active";
            System.out.println("Free plan activated for user " + userID);
        }
        
        return true;
    }
    
    /**
     * Operation: cancelPlan()
     * Cancels the current subscription
     * @return true if cancellation successful
     */
    public boolean cancelPlan() {
        if (!planType.equals(PLAN_PREMIUM)) {
            System.err.println("Cannot cancel free plan");
            return false;
        }
        
        // Downgrade to free plan
        this.planType = PLAN_FREE;
        this.paymentStatus = "Cancelled";
        this.endDate = Timestamp.valueOf(LocalDateTime.now());
        this.monthlyPrice = 0.0;
        this.pdfLimit = FREE_PDF_LIMIT;
        
        System.out.println("Premium subscription cancelled for user " + userID);
        return true;
    }
    
    /**
     * Operation: checkExpiration()
     * Checks if subscription has expired
     * @return true if subscription is expired
     */
    public boolean checkExpiration() {
        if (planType.equals(PLAN_FREE)) {
            return false;  // Free plan never expires
        }
        
        if (endDate == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = endDate.toLocalDateTime();
        
        boolean expired = now.isAfter(end);
        
        if (expired && !paymentStatus.equals("Expired")) {
            this.paymentStatus = "Expired";
            System.out.println("Subscription expired for user " + userID);
        }
        
        return expired;
    }
    
    /**
     * Operation: renewSubscription()
     * Renews an existing premium subscription
     * @return true if renewal successful
     */
    public boolean renewSubscription() {
        if (!planType.equals(PLAN_PREMIUM)) {
            System.err.println("Only premium plans can be renewed");
            return false;
        }
        
        // Extend subscription by one month
        if (checkExpiration()) {
            // If expired, start from now
            this.startDate = Timestamp.valueOf(LocalDateTime.now());
            this.endDate = Timestamp.valueOf(LocalDateTime.now().plusMonths(1));
        } else {
            // If still active, extend from current end date
            LocalDateTime newEndDate = endDate.toLocalDateTime().plusMonths(1);
            this.endDate = Timestamp.valueOf(newEndDate);
        }
        
        this.paymentStatus = "Active";
        System.out.println("Subscription renewed for user " + userID);
        return true;
    }
    
    /**
     * Operation: upgradeSubscription()
     * Upgrades from Free to Premium plan
     * @return true if upgrade successful
     */
    public boolean upgradeSubscription() {
        if (planType.equals(PLAN_PREMIUM)) {
            System.err.println("Already on premium plan");
            return false;
        }
        
        // Upgrade to premium
        this.planType = PLAN_PREMIUM;
        this.monthlyPrice = PREMIUM_PRICE;
        this.pdfLimit = Integer.MAX_VALUE;  // Unlimited
        
        // Activate the premium plan
        return activatePlan();
    }
    
    /**
     * Operation: calculateRemainingDays()
     * Calculates days remaining until subscription expires
     * @return number of days remaining (0 if expired or free plan)
     */
    public int calculateRemainingDays() {
        if (planType.equals(PLAN_FREE) || endDate == null) {
            return 0;  // Free plan has no expiration
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = endDate.toLocalDateTime();
        
        if (now.isAfter(end)) {
            return 0;  // Already expired
        }
        
        long daysRemaining = ChronoUnit.DAYS.between(now, end);
        return (int) daysRemaining;
    }
    
    /**
     * Additional helper method: Check if subscription is active
     */
    public boolean isActive() {
        return paymentStatus.equals("Active") && !checkExpiration();
    }
    
    /**
     * Additional helper method: Check if user is premium
     */
    public boolean isPremium() {
        return planType.equals(PLAN_PREMIUM) && isActive();
    }
    
    /**
     * Additional helper method: Get plan details as string
     */
    public String getPlanDetails() {
        if (planType.equals(PLAN_FREE)) {
            return "Free Plan - " + pdfLimit + " PDF limit";
        } else {
            return "Premium Plan - RM" + monthlyPrice + "/month - Unlimited PDFs - " + 
                   calculateRemainingDays() + " days remaining";
        }
    }
    
    // Getters and Setters
    
    public int getSubscriptionID() {
        return subscriptionID;
    }
    
    public void setSubscriptionID(int subscriptionID) {
        this.subscriptionID = subscriptionID;
    }
    
    public int getUserID() {
        return userID;
    }
    
    public void setUserID(int userID) {
        this.userID = userID;
    }
    
    public String getPlanType() {
        return planType;
    }
    
    public void setPlanType(String planType) {
        this.planType = planType;
        
        // Update price and limits based on plan
        if (planType.equals(PLAN_PREMIUM)) {
            this.monthlyPrice = PREMIUM_PRICE;
            this.pdfLimit = Integer.MAX_VALUE;
        } else {
            this.monthlyPrice = 0.0;
            this.pdfLimit = FREE_PDF_LIMIT;
        }
    }
    
    public Timestamp getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    
    public Timestamp getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public double getMonthlyPrice() {
        return monthlyPrice;
    }
    
    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }
    
    public int getPdfLimit() {
        return pdfLimit;
    }
    
    public void setPdfLimit(int pdfLimit) {
        this.pdfLimit = pdfLimit;
    }
    
    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionID=" + subscriptionID +
                ", userID=" + userID +
                ", planType='" + planType + '\'' +
                ", monthlyPrice=RM" + monthlyPrice +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", daysRemaining=" + calculateRemainingDays() +
                '}';
    }
}
