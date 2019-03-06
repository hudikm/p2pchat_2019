/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.cpd;

/**
 *
 * @author Martin
 */
  public interface IDataReceived {
        void onReceived(String newLine, IServerHandler serverHandler);
       
    }
