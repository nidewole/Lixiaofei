package com.ctvit.dev.exception;


public class CVException extends Exception{
  private static final long serialVersionUID = 5083446087625821329L;
  
  public CVException() {}
  
  public CVException(String paramString, Throwable paramThrowable){
    super(paramString, paramThrowable);
  }
  
  public CVException(String paramString){
    super(paramString);
  }
  
  public CVException(Throwable paramThrowable){
    super(paramThrowable);
  }
  
}
