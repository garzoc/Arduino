import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

public class Map {
	LinkedList<LinkedList <Panel>> matrix;
	MovingObject car;
	Coordinates renderOffset;
	private final int maxDistance, minDistance;
	private final int axisOffset;
	private final int panelScale;
	private int  windowWidth,windowHeight;
	private int numPanelsOnScreenX,numPanelsOnScreenY;
	private int panelOnScreenWidth,panelOnScreenHeight;
	public int detectorRotation;

	Map(int maxDistanceCM,int axisOffsetCM,int panelScaleCM,int minDistanceCM,int windowWidth,int windowHeight){
		this.maxDistance=maxDistanceCM +axisOffsetCM;//added axisOffset remove?
		this.minDistance=minDistanceCM +axisOffsetCM;//added axisOffset remove?
		this.axisOffset=axisOffsetCM;
		this.panelScale=panelScaleCM;
		this.detectorRotation=0;
		this.windowWidth=windowWidth;
		this.windowHeight=windowHeight;
		this.panelOnScreenHeight=10;
		this.panelOnScreenWidth=10;
		this.numPanelsOnScreenX=windowWidth/panelOnScreenWidth;
		this.numPanelsOnScreenY=windowHeight/panelOnScreenHeight;
		System.out.println(windowWidth+" / "+panelOnScreenWidth+" = "+numPanelsOnScreenX);
		System.out.println(windowHeight+" / "+panelOnScreenHeight+" = "+numPanelsOnScreenY);

		//add start Lane
		matrix=new LinkedList<LinkedList <Panel>>();
		matrix.addFirst(new LinkedList<Panel>());
		//intiilize a start matrix
		if(panelScaleCM>0){
			int estimate=this.maxDistance+panelScaleCM;
			int numberOfPanes=(estimate/panelScaleCM)+((estimate%panelScaleCM)/panelScaleCM>0.5?1:0);
			System.out.println(numberOfPanes);
			int numberOfPanesX=numberOfPanes*2-2;
			int numberOfPanesY=numberOfPanes*2-1;
			this.car=new MovingObject(new Coordinates(numberOfPanes-1,numberOfPanes-1));
			renderOffset=new Coordinates(numberOfPanes-(numPanelsOnScreenX/2),numberOfPanes-(numPanelsOnScreenY/2));
			
			for(int y=0;y<numberOfPanesY;y++){//add panes to the first row
				matrix.get(0).addLast(new IncogPanel());
			}
			
			for(int x=0;x<numberOfPanesX;x++){//adds the rest to the start-matrix
				matrix.addLast(new LinkedList<Panel>());
				for(int y=0;y<numberOfPanesY;y++){
					matrix.getLast().addLast(new IncogPanel());
				}
			}



		}else{
			System.out.println("Initilisation failed");
		}
		
	}
	
	public void draw(Graphics g){
		for(int x=0;x<this.numPanelsOnScreenX;x++){
			for(int y=0;y<this.numPanelsOnScreenY;y++){
				int absoluteX=renderOffset.x()+x;
				int absoluteY=renderOffset.y()+y;
				if(absoluteX<matrix.size()&&absoluteY<matrix.get(x).size()&&absoluteX>=0&&absoluteY>=0){
					g.setColor(matrix.get(x+renderOffset.x()).get(y+renderOffset.y()).color);
					int height=this.panelOnScreenHeight;
					int width=this.panelOnScreenWidth;
					g.fillRect(x*height, y*width, height,width);
					
				}
			}
		}
	}
	
	
	private Coordinates panelPositionInScreen( Coordinates position){
		position=new Coordinates(((position.x()-renderOffset.x())*panelOnScreenWidth)+(panelOnScreenWidth/2),((position.y()-renderOffset.y())*panelOnScreenHeight)+(panelOnScreenHeight/2));
		return position;
	}
	
	public void drawDetector(Graphics g){
		
		boolean inXbounds=renderOffset.x()<=car.getPos().x()?(car.getPos().x()<(renderOffset.x()+numPanelsOnScreenX))?true:false:false;
		boolean inYbounds=renderOffset.y()<=car.getPos().y()?(car.getPos().y()<(renderOffset.y()+numPanelsOnScreenY))?true:false:false;
		if(inXbounds&&inYbounds){
			//g.setColor(Color.black);
			Graphics2D g2=((Graphics2D)g.create());
			GeneralPath carShape=car.getShape(panelPositionInScreen(this.car.getPos()),detectorRotation);
			g2.setPaint(Color.red);
	    	//g2.fill(carShape);
	    	g2.draw(carShape);
			g2.setPaint(Color.black);
	    	//scarShape.reset();		    
			g2.dispose();
		}
	}
	
	public void changeView(int x,int y){
		this.renderOffset.alter(renderOffset.x()+x, renderOffset.y()+y);
	}
	
	public int transformDegrees(int degrees){
		if(360<=degrees){
			degrees=degrees%360;
			degrees=180<degrees?-180+(degrees%180):degrees;
		}else if(degrees<=-360){
			degrees=degrees%-360;
			degrees=degrees<-180?180+(degrees%-180):degrees;
		}else if(degrees<-180){
			degrees=180+degrees%180;//deegrees is negative here
		}else if(180<degrees){
			degrees=-180+degrees%180;//deegrees is negative here
		}
		return degrees;
	}
	
	private Coordinates angleTransformDirection(int degrees){
		degrees=transformDegrees(degrees);
		int y=90<degrees||degrees<-90?-1:1;
		int x=degrees<0?-1:1;
		return new Coordinates(x,y);
	}
	
	public void detection(int hypotenuse){
		hypotenuse=hypotenuse+this.axisOffset;// recently added remove this axxis offset?
		
		boolean detectingObstacle=false;
		if(hypotenuse<maxDistance&& minDistance<hypotenuse){
			detectingObstacle=true;
		}
		double deltaX=Math.abs(hypotenuse*Math.sin(Math.toRadians(detectorRotation)));//-positionOnScreen.yF();//Math.abs correct?
		double deltaY=Math.sqrt(Math.pow(hypotenuse, 2)-Math.pow(deltaX, 2));
		Coordinates pos=car.getPos();//car coordinates
		deltaY=-deltaY/this.panelScale;
		deltaX=deltaX/this.panelScale;
		Coordinates direction=angleTransformDirection(detectorRotation);//directional coordinates uses to determine directions
		
		deltaY=deltaY*direction.y();//alter the direction
		deltaX=deltaX*direction.x();//alter the direction
		//System.out.println(deltaX);
		
		
		float scaledHypotenuse=hypotenuse;
		if(detectingObstacle){
			matrix.get((int)Math.round((pos.xF()+(deltaX)))).set((int)Math.round((pos.yF()+(deltaY))),new Obstacle());
			scaledHypotenuse-=this.panelScale;
		}//else{

		float scale=0;
		while(this.minDistance<scaledHypotenuse){
			//System.out.println(scaledHypotenuse);
			scale=scaledHypotenuse/hypotenuse;
			if((scaledHypotenuse<=this.maxDistance)){
				//System.out.println(Math.round((pos.yF()+(deltaY*scale))));
				matrix.get((int)Math.round((pos.xF()+(deltaX*scale)))).set((int)Math.round((pos.yF()+(deltaY*scale))),new Panel());
			}
			scaledHypotenuse-=0.5;//this.panelScale?
		
		}
	}
	int offsetRotation=0;
	public void rotateDetector(int rotation){
		
	}
	
	public void rotateDetectoBase(int rotation){
		rotation=this.transformDegrees(rotation);
		this.offsetRotation=rotation;
		Coordinates direction=this.angleTransformDirection(rotation);
		//9,5 &&2
		
	}
	
	
	/*public void check(int hypotenuse){
		this.detectorRotation=45;
		//System.out.println(transformDegrees(359));
		boolean detectingObstacle=false;
		if(hypotenuse<maxDistance&&minDistance<hypotenuse){
			detectingObstacle=true;
		}
		Coordinates pos=car.getPos();
		
		//double sinAngle=Math.sin(detectorRotation);
		
		double deltaY=Math.abs(hypotenuse*Math.sin(Math.toRadians(detectorRotation)))-pos.yF();//Math.abs correct?
		
		double deltaX=Math.sqrt(Math.pow(hypotenuse, 2)-Math.pow(deltaY, 2));
		deltaX=transformDegrees(detectorRotation)<0?-deltaX:deltaX;
		deltaY=deltaY/this.panelScale;
		deltaX=deltaX/this.panelScale;
		//System.out.println(deltaX);

		float scaledHypotenuse=hypotenuse;
		
		if(detectingObstacle){
			matrix.get((int)(pos.xF()+deltaX)).set((int)(pos.yF()+deltaY),new Panel());
			scaledHypotenuse-=this.panelScale;
		}else{
			matrix.get((int)(pos.xF()+deltaX)).set((int)(pos.yF()+deltaY),new Panel());
			scaledHypotenuse-=this.panelScale;
		}
		float scale=0;
		while(0<scaledHypotenuse){
			//System.out.println(scaledHypotenuse);
			scale=scaledHypotenuse/hypotenuse;
			matrix.get((int)(pos.xF()+(deltaX*scale))).set((int)(pos.yF()+(deltaY*scale)),new Panel());
			scaledHypotenuse-=this.panelScale;
		}
	}*/
}
