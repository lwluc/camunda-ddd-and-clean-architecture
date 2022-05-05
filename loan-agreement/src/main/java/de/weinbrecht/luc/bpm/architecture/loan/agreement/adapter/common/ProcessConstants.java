package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.common;

public class ProcessConstants {

    public static final String LOAN_START_EVENT_MESSAGE_REF = "loanAgreementReceivedMessage";
    public static final String LOAN_AGREEMENT_NUMBER = "loanAgreementNumber";
    public static final String RECOMMENDATION_START_EVENT_MESSAGE_REF = "crossSellingPotentialDiscoveredMessage";
    public static final String RECOMMENDATION_CUSTOMER_NUMBER = "customerNumber";

    public static final String LOAN_AGREEMENT_TASK = "approve-loan-agreement";
    public static final String LOAN_REJECTION_TASK = "reject-loan-agreement";
    public static final String SEND_CROSS_SELLING_RECOMMENDATION_TASK = "send-cross-selling-recommendation";

}
