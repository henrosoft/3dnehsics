/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dnehsics;
import javax.vecmath.Color3f;
import javax.media.j3d.Appearance;
import java.util.Iterator;
import java.util.List;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

/**
 *
 * @author henry
 */
public class Body implements Runnable{
    public static final double K = 8.987551787E32;//supposed to be E9 //Coulombs Constant //Nm^2/C^2 - Newton meter squared per coulomb squared
    public static final double ELECTRON_CHARGE = -1.602176487E-19; //Coubombs
    public static final double PROTON_CHARGE = 1.602176487E-19; //Coulombs
    public static final double ELECTRON_MASS = 9.10938215E-31; //Kilo Grams
    public static final double PROTON_MASS = 1.672621636E-27; //Kilo Grams
    public static final double UNIT_SIZE = 1E-9; //one java3d unit = one nanometer
    protected Vector3f position;
    protected Vector3f velocity;
    protected float mass;
    protected float radius;
    protected Transform3D t3d;
    protected TransformGroup group;
    protected float charge;
    protected List<Body> bodies;

	static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
		Runtime.getRuntime().availableProcessors()
	);

	static void schedule(Runnable command, long ms) {
		executor.scheduleAtFixedRate(command, 0, ms, TimeUnit.MILLISECONDS);
	}
    public Body(Vector3f p, Vector3f v, float r, float c, float m, Color3f color, BranchGroup master, List<Body> bodyList)
    {
        position = p;
        mass = m;
        velocity = v;
        radius = r;
        charge = c;
        bodies = bodyList;
        bodies.add(this);
        BranchGroup bg = new BranchGroup();
        Sphere sph = new Sphere(radius);
        Appearance a = sph.getAppearance();
        a.getMaterial().setEmissiveColor(color);
        sph.setAppearance(a);
        
        bg.addChild(sph);
        t3d = new Transform3D();
        t3d.setTranslation(position);
        group = new TransformGroup(t3d);
        group.addChild(bg);
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        master.addChild(group);
		schedule(this, 10);
    }
    public TransformGroup getGroup()
    {
        return group;
    }
    public float getCharge(double d)
    {
        return charge;
    }
    public void applyForce()
    {
        Iterator<Body> iter = bodies.iterator();
        while(iter.hasNext())
        {
            Body b = iter.next();
            if(b!=this)
            {
            Vector3f p = (Vector3f) position.clone();
            p.sub(b.position);
            Vector3f force = new Vector3f((float)(getCharge(p.length())*b.getCharge(p.length())*K*p.x/Math.pow(p.length(),3)),
                                        (float)(getCharge(p.length())*b.getCharge(p.length())*K*p.y/Math.pow(p.length(),3)),
                                        (float)(getCharge(p.length())*b.getCharge(p.length())*K*p.z/Math.pow(p.length(),3)));
            Vector3f accel = new Vector3f();
            accel.scale(1/mass, force);
            velocity.add(accel);
            }
        }
    }
    public void run() {
        /*while(true)
        {
            try{Thread.sleep(10);}
            catch(Exception e)
            {
                e.printStackTrace();
            }*/
		position.add(velocity);
		if(position.x>.5)
			velocity.x = -Math.abs(velocity.x);
		if(position.y>.5)
			velocity.y = -Math.abs(velocity.y);
		if(position.z>.5)
			velocity.z = -Math.abs(velocity.z);
                if(position.x<-.5)
			velocity.x = Math.abs(velocity.x);
		if(position.y<-.5)
			velocity.y = Math.abs(velocity.y);
		if(position.z<-.5)
			velocity.z = Math.abs(velocity.z);
                velocity.x *=.8;
                velocity.y *=.8;
                velocity.z *=.8;
                
		applyForce();
		t3d.setTranslation(position);
		group.setTransform(t3d);
    }
}
