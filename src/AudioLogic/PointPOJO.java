/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AudioLogic;

import java.io.Serializable;

/**
 *
 * @author Kata
 */
public class PointPOJO implements Serializable {
    
    
    public int location;
    public String name;
    PointPOJO(int location,String name)
    {
        this.location=location;
        this.name=name;
    }
}