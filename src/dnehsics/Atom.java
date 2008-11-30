/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dnehsics;

import java.util.List;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3f;

/**
 *
 * @author henry
 */
//Using equation for charge density: 2xe^(-x^2)
//integral: -e^(-x^2)
public class Atom extends Body{
    protected int numElectrons;
    protected int numProtons;
    public Atom(Vector3f p, Vector3f v, float r, int nume, int nump, BranchGroup master, List<Body> bodyList)
    {
        super(p,v,r,(float)(nump*Body.PROTON_CHARGE+nume*Body.ELECTRON_CHARGE),master,bodyList);
        numElectrons = nume;
        numProtons = nump;
    }
    public float getCharge(double d)
    {
        d = d*15.0;
        double eFraction = 1-Math.pow(Math.E, -(d*d));
        return (float) (numProtons*Body.PROTON_CHARGE + numElectrons*eFraction*Body.ELECTRON_CHARGE);
    }
}
