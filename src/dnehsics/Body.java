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
         //   System.out.println(position + " " + b.position);
            Vector3f p = (Vector3f) position.clone();
         //   System.out.println(position + " " + b.position + " " + p);
            p.sub(b.position);
         //   System.out.println(position + " " + b.position + " " + p);
          //  float forceMagnitude = charge*b.charge/p.length();
            System.out.println(p.length());
            Vector3f force = new Vector3f(charge*b.charge*(10E-8f*p.x*Math.abs(p.x)/(p.length())),
                                        charge*b.charge*(10E-8f*p.y*Math.abs(p.y)/(p.length())),
                                        charge*b.charge*(10E-8f*p.z*Math.abs(p.z)/(p.length())));
            force = new Vector3f(charge*b.charge/(10E-9f*p.x*Math.abs(p.x)),
                                        charge*b.charge/(10E-9f*p.y*Math.abs(p.y)),
                                        charge*b.charge/(10E-9f*p.z*Math.abs(p.z)));
            float max = 1E-5f;
            if(force.x>max)
                force.x = max;
            if(force.x<-max)
                force.x = -max;
             if(force.y>max)
                force.y = max;
            if(force.y<-max)
                force.y = -max;
            if(force.z>max)
                force.z = max;
            if(force.z<-max)
                force.z = -max;
         //   System.out.println(force);
            velocity.add(force);
        }
    }
    public void applyForceOld()
    {
        Iterator<Body> iter = bodies.iterator();
        while(iter.hasNext())
        {
            Body b = iter.next();
         //   System.out.println(position + " " + b.position);
            Vector3f p = (Vector3f) position.clone();
         //   System.out.println(position + " " + b.position + " " + p);
            p.sub(b.position);
         //   System.out.println(position + " " + b.position + " " + p);
            
            Vector3f force = new Vector3f(charge*b.charge/(10E-7f*p.x*Math.abs(p.x)),
                                        charge*b.charge/(10E-7f*p.y*Math.abs(p.y)),
                                        charge*b.charge/(10E-7f*p.z*Math.abs(p.z)));
            float max = 1E-5f;
            if(force.x>max)
                force.x = max;
            if(force.x<-max)
                force.x = -max;
             if(force.y>max)
                force.y = max;
            if(force.y<-max)
                force.y = -max;
            if(force.z>max)
                force.z = max;
            if(force.z<-max)
                force.z = -max;
         //   System.out.println(force);
            velocity.add(force);
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
         //   System.out.println(position);
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
