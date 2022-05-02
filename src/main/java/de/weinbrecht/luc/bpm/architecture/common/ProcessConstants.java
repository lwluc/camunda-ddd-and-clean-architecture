package de.weinbrecht.luc.bpm.architecture.common;

public class ProcessConstants {

    public class LoanAgreement {

        public static final String PROCESS_DEFINITION = "Loan_Agreement";
        public static final String START_EVENT_MESSAGE_REF = "loanAgreementReceivedMessage";
        public static final String LOAN_AGREEMENT_NUMBER = "loanAgreementNumber";
    }

    public class CrossSellingRecommendation {

        public static final String PROCESS_DEFINITION = "Cross_Selling_Recommendation";
        public static final String START_EVENT_MESSAGE_REF = "crossSellingPotentialDiscoveredMessage";
        public static final String CUSTOMER_NUMBER = "customerNumber";
        public static final String CONTENT_NUMBER = "contentNumber";
    }

}