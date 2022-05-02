package de.weinbrecht.luc.bpm.architecture.loan.agreement.adapter.out.process;

class ProcessInstanceNotFoundException extends RuntimeException {
    public ProcessInstanceNotFoundException(String message) {
        super(message);
    }
}
