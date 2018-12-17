/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ants;

/**
 *
 * @author Daud
 */
public class ExecutionTimer {
  public long start;
  public long end;

  public ExecutionTimer() {
    reset();
    start = System.currentTimeMillis();
  }

  public void end() {
    end = System.currentTimeMillis();
  }

  public long duration(){
    return (end-start);
  }

  public void reset() {
    start = 0;  
    end   = 0;
  }
}
