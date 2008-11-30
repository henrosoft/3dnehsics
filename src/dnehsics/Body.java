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
    public static final double K = 8.987551787E9;//supposed to be E9
    public static final double ELECTRON_CHARGE = -1.602176487E-19;
    public static final double PROTON_CHARGE = 1.602176487E-19;
    public static final double ELECTRON_MASS = 1;
    public static final double PROTON_MASS = 1E-20;
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
                        System.out.println(force + " " + accel);
            accel.scale(1/mass, force);
            System.out.println(force + " " + accel);
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
		if(Math.abs(position.x)>.5)
			velocity.x = -velocity.x;
		if(Math.abs(position.y)>.5)
			velocity.y = -velocity.y;
		if(Math.abs(position.z)>.5)
			velocity.z = -velocity.z;
		applyForce();
		t3d.setTranslation(position);
		group.setTransform(t3d);
    }
}
