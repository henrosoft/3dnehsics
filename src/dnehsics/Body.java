/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dnehsics;

import com.sun.j3d.utils.geometry.Sphere;
import java.util.Iterator;
import java.util.LinkedList;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 *
 * @author henry
 */
public class Body implements Runnable{
    public static final double K = 8.987551787E9;
    public static final double ELECTRON_CHARGE = -1.602176487E-19;
    public static final double PROTON_CHARGE = 1.602176487E-19;
    protected Vector3f position;
    protected Vector3f velocity;
    protected float radius;
    protected Transform3D t3d;
    protected TransformGroup group;
    protected float charge;
    protected LinkedList<Body> bodies;
    public Body(Vector3f p, Vector3f v, float r, float c, BranchGroup master, LinkedList<Body> bodyList)
    {
        position = p;
        velocity = v;
        radius = r;
        charge = c;
        bodies = bodyList;
        bodies.add(this);
        BranchGroup bg = new BranchGroup();
        bg.addChild(new Sphere(radius));
        t3d = new Transform3D();
        t3d.setTranslation(position);
        group = new TransformGroup(t3d);
        group.addChild(bg);
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        master.addChild(group);
        Thread t = new Thread(this);
        t.start();
    }
    public TransformGroup getGroup()
    {
        return group;
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
            Vector3f force = new Vector3f((float)(charge*b.charge*K*p.x/Math.pow(p.length(),3)),
                                        (float)(charge*b.charge*K*p.y/Math.pow(p.length(),3)),
                                        (float)(charge*b.charge*K*p.z/Math.pow(p.length(),3)));
            velocity.add(force);
            }
        }
    }
    public void run() {
        while(true)
        {
            try{Thread.sleep(10);}
            catch(Exception e)
            {
                e.printStackTrace();
            }
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
}
