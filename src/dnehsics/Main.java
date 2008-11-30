/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dnehsics;

import java.applet.Applet;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import java.util.List;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransparencyAttributes;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;

import com.sun.j3d.utils.geometry.ColorCube;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 *
 * @author henry
 */
public class Main extends Applet {
	public static final long serialVersionUID = 293841832419834234L;
    private static BoundingSphere myBounds = new BoundingSphere();
    private List<Body> bodies = new CopyOnWriteArrayList<Body>();
    public Main() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);

        BranchGroup scene = createSceneGraph();

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();
        OrbitBehavior orbit =
            new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0,0,0),100));
        ViewingPlatform vp = simpleU.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
        simpleU.addBranchGraph(scene);
    } // end of HelloJava3Da (constructor)
    public BranchGroup createSceneGraph() {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();

       /* new Body(new Vector3f(0,0.01f,.3f), new Vector3f(.0f,0,-.0003f),.02f,(float)Body.ELECTRON_CHARGE*1E10f,objRoot,bodies);
        new Body(new Vector3f(0,-.01f,-.3f), new Vector3f(.0f,.00f,.0003f),.02f,(float)Body.ELECTRON_CHARGE*1E10f,objRoot,bodies);*/
        
        new Atom(new Vector3f(0,0.01f,.3f), new Vector3f(.0f,0,-.0009f),.02f,2,1,(float)Body.ELECTRON_MASS,new Color3f(0,0,0),objRoot,bodies);
        new Atom(new Vector3f(0,-.01f,-.3f), new Vector3f(.0f,.00f,.0009f),.02f,2,1,(float)Body.ELECTRON_MASS,new Color3f(0,0,0),objRoot,bodies);
    /*    int count = 0;
        for(double i = -.3; i<=.3; i+=.1)
            if(count%2 == 0)
            {
                new Atom(new Vector3f(0,0.03f,(float)i), new Vector3f(.0f,0,0),.02f,18,17,new Color3f(0,0,0),objRoot,bodies);
                count++;
            }
            else
            {
                new Atom(new Vector3f(0,0.03f,(float)i), new Vector3f(.0f,0,0),.02f,8,11,new Color3f(0,255,0),objRoot,bodies);
                count++;
            }
        for(double i = -.3; i<=.3; i+=.1)
            if(count%2 == 1)
            {
                new Atom(new Vector3f(0,-0.03f,(float)i), new Vector3f(.0f,0,0),.02f,18,17,new Color3f(0,0,0),objRoot,bodies);
                count++;
            }
            else
            {
                new Atom(new Vector3f(0,-0.03f,(float)i), new Vector3f(.0f,0,0),.02f,10,11,new Color3f(255,0,0),objRoot,bodies);
                count++;
            }
        /*for(double i = -.3; i<=.3; i+=.1)
            if(count%2 == 0)
            {
                new Atom(new Vector3f(.03f,0.03f,(float)i), new Vector3f(.0f,0,0),.02f,18,17,new Color3f(0,0,0),objRoot,bodies);
                count++;
            }
            else
            {
                new Atom(new Vector3f(.03f,0.03f,(float)i), new Vector3f(.0f,0,0),.02f,10,11,new Color3f(255,0,0),objRoot,bodies);
                count++;
            }
        for(double i = -.3; i<=.3; i+=.1)
            if(count%2 == 1)
            {
                new Atom(new Vector3f(.03f,-0.03f,(float)i), new Vector3f(.0f,0,0),.02f,18,17,new Color3f(0,0,0),objRoot,bodies);
                count++;
            }
            else
            {
                new Atom(new Vector3f(.03f,-0.03f,(float)i), new Vector3f(.0f,0,0),.02f,10,11,new Color3f(255,0,0),objRoot,bodies);
                count++;
            }*/

        Appearance a = new Appearance();
        a.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST,.70f));
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        a.setPolygonAttributes(pa);
        Shape3D s = new ColorCube(.5f);
       s.setAppearance(a);
        objRoot.addChild(s);
        objRoot.addChild(lightScene());
        objRoot.addChild(createAxis());

        return objRoot;
    } // end of CreateSceneGraph method of HelloJava3Da

    /**
     * @param args the command line arguments
     */
            private BranchGroup lightScene()
  // One ambient light, 2 directional lights
  {
        BranchGroup sceneBG = new BranchGroup();
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    // Set up the ambient light
    AmbientLight ambientLightNode = new AmbientLight(white);
    ambientLightNode.setInfluencingBounds(myBounds);
    sceneBG.addChild(ambientLightNode);

    // Set up the directional lights
    Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
       // left, down, backwards
    Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);
       // right, down, forwards

    DirectionalLight light1 =
            new DirectionalLight(white, light1Direction);
    light1.setInfluencingBounds(myBounds);
    sceneBG.addChild(light1);

    DirectionalLight light2 =
        new DirectionalLight(white, light2Direction);
    light2.setInfluencingBounds(myBounds);
    sceneBG.addChild(light2);
    return sceneBG;
  }  // end of lightScene()
       public BranchGroup createAxis()
        {
                //the branchgroup to store the lines
                BranchGroup b = new BranchGroup();

                //the positive x line
                Point3d[] points = new Point3d[]{new Point3d(1,0,0)};//a point at (1,0,0)
                GeometryArray g = new LineArray(points.length*2,GeometryArray.COORDINATES);//make a linearray
                g.setCoordinates(0, points);//set the verticies of the array the the point
                Appearance a = new Appearance();//set the appearance to red and thikness (size 5) and solid
                a.setColoringAttributes(new ColoringAttributes(255,0,0,ColoringAttributes.NICEST));
                a.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_SOLID, false));
                Shape3D s = new Shape3D(g, a);//create a shape with a certain shape and appearance
                b.addChild(s);//add it to b

                //the positive y line
                points = new Point3d[]{new Point3d(0,1,0)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a2 = new Appearance();
                a2.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_SOLID, false));
                a2.setColoringAttributes(new ColoringAttributes(0,255,0,ColoringAttributes.NICEST));
                s = new Shape3D(g, a2);
                b.addChild(s);

                //the positive z line
                points = new Point3d[]{new Point3d(0,0,1)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a3 = new Appearance();
                a3.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_SOLID, false));
                a3.setColoringAttributes(new ColoringAttributes(0,0,255,ColoringAttributes.NICEST));
                s = new Shape3D(g, a3);
                b.addChild(s);

                //the negative x line
                points = new Point3d[]{new Point3d(-1,0,0)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a4 = new Appearance();
                a4.setColoringAttributes(new ColoringAttributes(255,0,0,ColoringAttributes.NICEST));
                a4.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_DASH, false));
                s = new Shape3D(g, a4);
                b.addChild(s);

                //the negative y line
                points = new Point3d[]{new Point3d(0,-1,0)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a5 = new Appearance();
                a5.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_DASH, false));
                a5.setColoringAttributes(new ColoringAttributes(0,255,0,ColoringAttributes.NICEST));
                s = new Shape3D(g, a5);
                b.addChild(s);

                //the negative z line
                points = new Point3d[]{new Point3d(0,0,-1)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a6 = new Appearance();
                a6.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_DASH, false));
                a6.setColoringAttributes(new ColoringAttributes(0,0,255,ColoringAttributes.NICEST));
                s = new Shape3D(g, a6);
                b.addChild(s);
                return b;
        }

    public static void main(String[] args) {
         new MainFrame(new Main(), 800, 800);
    }

}
