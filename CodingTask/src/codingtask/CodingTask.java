/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codingtask;

/**
 *
 * @author mahlet
 */
public class CodingTask {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Queue<String> q = new ImmutableQueue<>();
        q = q.enQueue("Pay");
        System.out.println(q.head());
        q = q.enQueue("Pay");
        System.out.println(q.head());
        q = q.deQueue();
        System.out.println(q.head());
    }

}
