import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

public class MovingObject {
	private Coordinates position;
	private int objectRotation;
	private float[] xPoints={0,0,0,0};
	private float[] yPoints={0,0,0,0};
	int size=20;
	GeneralPath objectShape = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
	MovingObject(Coordinates position){
		this.position=position;
		this.objectRotation=0;
	}
	
	public Coordinates getPos(){
		return this.position;
	}
	
	public int getRot(){
		return this.objectRotation;
	}
	
	public float[] getXPoints(){
		return xPoints;
		
	}
	
	public float[] getYPoints(){
		return yPoints;	
	}
	
	
	public GeneralPath getShape(Coordinates positionOnScreen,int rotation){
		this.objectShape.reset();
//		this.xPoints[0]=positionOnScreen.xF()-(this.size/2);
//		this.yPoints[0]=positionOnScreen.yF()+(this.size/2);
//		this.xPoints[1]=positionOnScreen.xF()+(this.size/2);
//		this.yPoints[1]=positionOnScreen.yF()+(this.size/2);
//		this.xPoints[2]=positionOnScreen.xF();
//		this.yPoints[2]=positionOnScreen.yF()-(this.size/2);
//		this.xPoints[3]=positionOnScreen.xF()-(this.size/2);
//		this.yPoints[3]=positionOnScreen.yF()+(this.size/2);
		float angle=(float)Math.toRadians(rotation);
		float x1=positionOnScreen.xF()-(this.size/2);
		float y1=positionOnScreen.yF()+(this.size/2);
		float x2=positionOnScreen.xF()+(this.size/2);
		float y2=positionOnScreen.yF()+(this.size/2);
		float x3=positionOnScreen.xF();
		float y3=positionOnScreen.yF()-(this.size/2);
		
//		this.xPoints[0] = (float) ((x1 - positionOnScreen.xF()) * Math.cos(Math.toRadians(rotation)) - (y1 - positionOnScreen.yF()) * Math.sin(Math.toRadians(rotation)) + positionOnScreen.xF());
//		this.yPoints[0] = (float) ((x1 - positionOnScreen.xF()) * Math.sin(Math.toRadians(rotation)) + (y1 - positionOnScreen.yF()) * Math.cos(Math.toRadians(rotation)) + positionOnScreen.yF());
//
//		this.xPoints[1] = (float) ((x2 - positionOnScreen.xF()) * Math.cos(Math.toRadians(rotation)) - (y2 - positionOnScreen.yF()) * Math.sin(Math.toRadians(rotation)) + positionOnScreen.xF());
//		this.yPoints[1] = (float) ((x2 - positionOnScreen.xF()) * Math.sin(Math.toRadians(rotation)) + (y2 - positionOnScreen.yF()) * Math.cos(Math.toRadians(rotation)) + positionOnScreen.yF());
//
//		this.xPoints[2] = (float) ((x3 - positionOnScreen.xF()) * Math.cos(Math.toRadians(rotation)) - (y3 - positionOnScreen.yF()) * Math.sin(Math.toRadians(rotation)) + positionOnScreen.xF());
//		this.yPoints[2] = (float) ((x3 - positionOnScreen.xF()) * Math.sin(Math.toRadians(rotation)) + (y3 - positionOnScreen.yF()) * Math.cos(Math.toRadians(rotation)) + positionOnScreen.yF());
//		
//		this.xPoints[3] = (float) ((x1 - positionOnScreen.xF()) * Math.cos(Math.toRadians(rotation)) - (y1 - positionOnScreen.yF()) * Math.sin(Math.toRadians(rotation)) + positionOnScreen.xF());
//		this.yPoints[3] = (float) ((x1 - positionOnScreen.xF()) * Math.sin(Math.toRadians(rotation)) + (y1 - positionOnScreen.yF()) * Math.cos(Math.toRadians(rotation)) + positionOnScreen.yF());

		
		this.xPoints[0] = (float) ((x1 - positionOnScreen.xF()) * Math.cos(angle) - (y1 - positionOnScreen.yF()) * Math.sin(angle) + positionOnScreen.xF());
		this.yPoints[0] = (float) ((x1 - positionOnScreen.xF()) * Math.sin(angle) + (y1 - positionOnScreen.yF()) * Math.cos(angle) + positionOnScreen.yF());

		this.xPoints[1] = (float) ((x2 - positionOnScreen.xF()) * Math.cos(angle) - (y2 - positionOnScreen.yF()) * Math.sin(angle) + positionOnScreen.xF());
		this.yPoints[1] = (float) ((x2 - positionOnScreen.xF()) * Math.sin(angle) + (y2 - positionOnScreen.yF()) * Math.cos(angle) + positionOnScreen.yF());

		this.xPoints[2] = (float) ((x3 - positionOnScreen.xF()) * Math.cos(angle) - (y3 - positionOnScreen.yF()) * Math.sin(angle) + positionOnScreen.xF());
		this.yPoints[2] = (float) ((x3 - positionOnScreen.xF()) * Math.sin(angle) + (y3 - positionOnScreen.yF()) * Math.cos(angle) + positionOnScreen.yF());
		
		this.xPoints[3] = (float) ((x1 - positionOnScreen.xF()) * Math.cos(angle) - (y1 - positionOnScreen.yF()) * Math.sin(angle) + positionOnScreen.xF());
		this.yPoints[3] = (float) ((x1 - positionOnScreen.xF()) * Math.sin(angle) + (y1 - positionOnScreen.yF()) * Math.cos(angle) + positionOnScreen.yF());
	
		
		this.objectShape.moveTo (xPoints[0], yPoints[0]);
		for (int index = 1; index < xPoints.length; index++) {
			objectShape.lineTo(xPoints[index], yPoints[index]);
		};
		this.objectShape.moveTo (positionOnScreen.xF(), positionOnScreen.yF());
		if(360<=rotation){
			rotation=rotation%360;
			rotation=180<rotation?-180+(rotation%180):rotation;
		}else if(rotation<=-360){
			rotation=rotation%-360;
			rotation=rotation<-180?180+(rotation%-180):rotation;
		}else if(rotation<-180){
			rotation=180+rotation%180;//deegrees is negative here
		}else if(180<rotation){
			rotation=-180+rotation%180;//deegrees is negative here
		}
		double deltaX=Math.abs(250*Math.sin(Math.toRadians(rotation)));//-positionOnScreen.yF();//Math.abs correct?
		
		double deltaY=Math.sqrt(Math.pow(250, 2)-Math.pow(deltaX, 2));
		//System.out.println(deltaY);
		//System.out.println(Math.sin(Math.toRadians(rotation)));
		deltaY=-deltaY/1;
		deltaX=deltaX/1;
		
		deltaY=deltaY*(90<rotation||rotation<-90?-1:1);
		deltaX=deltaX*(rotation<0?-1:1);
		//deltaX=deltaX*-1;
		this.objectShape.lineTo((positionOnScreen.xF()+deltaX), positionOnScreen.yF()+deltaY);
		
		//	matrix.get((int)(pos.xF()+deltaX)).set((int)(pos.yF()+deltaY),new Panel());
	

		return objectShape;
	}
	
		
}
